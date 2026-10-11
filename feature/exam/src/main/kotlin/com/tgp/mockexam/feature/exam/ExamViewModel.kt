package com.tgp.mockexam.feature.exam

import android.os.SystemClock
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tgp.mockexam.core.domain.engine.ExamTime
import com.tgp.mockexam.core.domain.engine.ScoreCalculator
import com.tgp.mockexam.core.domain.usecase.ExamObservation
import com.tgp.mockexam.core.domain.usecase.ObserveExamRunUseCase
import com.tgp.mockexam.core.domain.usecase.ResetExamUseCase
import com.tgp.mockexam.core.domain.usecase.SaveAnswerUseCase
import com.tgp.mockexam.core.domain.usecase.SetCurrentQuestionUseCase
import com.tgp.mockexam.core.domain.usecase.StartExamUseCase
import com.tgp.mockexam.core.domain.usecase.SubmitExamUseCase
import com.tgp.mockexam.core.model.AttemptAnswer
import com.tgp.mockexam.core.model.ContentLanguage
import com.tgp.mockexam.core.model.ExamStatus
import com.tgp.mockexam.core.model.ExamSession
import com.tgp.mockexam.core.model.Paper
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.concurrent.atomic.AtomicBoolean
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = ExamViewModel.Factory::class)
class ExamViewModel @AssistedInject constructor(
    @Assisted val paperId: String,
    private val observeExamRun: ObserveExamRunUseCase,
    private val startExam: StartExamUseCase,
    private val saveAnswer: SaveAnswerUseCase,
    private val setCurrentQuestion: SetCurrentQuestionUseCase,
    private val submitExam: SubmitExamUseCase,
    private val resetExam: ResetExamUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(ExamUiState(paperId = paperId))
    val uiState: StateFlow<ExamUiState> = _uiState.asStateFlow()

    private val _language = MutableStateFlow(ContentLanguage.ENGLISH)
    private val answerEvents = MutableSharedFlow<AttemptAnswer>(extraBufferCapacity = 64)
    private val autoSubmitted = AtomicBoolean(false)

    init {
        viewModelScope.launch {
            answerEvents.debounce(250).collectLatest { answer ->
                saveAnswer(paperId, answer)
            }
        }

        observeExamRun(paperId).flatMapLatest { observation ->
            val paper = observation.paper
            val session = observation.session
            when {
                paper == null -> flow {
                    emit(ExamUiState(paperId = paperId, loading = false, error = "Paper unavailable"))
                }

                session == null -> flow {
                    emit(ExamUiState(paperId = paperId, loading = false, paper = paper))
                }

                else -> flow {
                    clockFlow().collect { tick ->
                        val remaining = ExamTime.remainingMs(
                            startedAtRealtimeMs = session.startedAtRealtimeMs,
                            startedAtEpochMs = session.startedAtEpochMs,
                            durationMins = session.durationMins,
                            pausedAccumulatedMs = session.pausedAccumulatedMs,
                            nowRealtimeMs = tick.first,
                            nowEpochMs = tick.second,
                        )
                        emit(stateFrom(paper, session, remaining))
                        if (remaining == 0L &&
                            session.status == ExamStatus.IN_PROGRESS &&
                            autoSubmitted.compareAndSet(false, true)
                        ) {
                            submitExam(paperId)
                        }
                    }
                }
            }
        }.onEach { _uiState.value = it }.launchIn(viewModelScope)
    }

    fun start() {
        viewModelScope.launch { startExam(paperId) }
    }

    fun retake() {
        autoSubmitted.set(false)
        viewModelScope.launch {
            resetExam(paperId)
            startExam(paperId)
        }
    }

    fun selectOption(optionIndex: Int) {
        val question = _uiState.value.currentQuestion ?: return
        val previous = _uiState.value.session?.answers?.get(question.number)
        updateAnswer(
            AttemptAnswer(
                questionNumber = question.number,
                selectedOptionIndex = optionIndex,
                markedForReview = previous?.markedForReview ?: false,
                visited = true,
            ),
        )
    }

    fun toggleMarkForReview() {
        val question = _uiState.value.currentQuestion ?: return
        val previous = _uiState.value.session?.answers?.get(question.number)
        updateAnswer(
            AttemptAnswer(
                questionNumber = question.number,
                selectedOptionIndex = previous?.selectedOptionIndex,
                markedForReview = !(previous?.markedForReview ?: false),
                visited = true,
            ),
        )
    }

    fun next() = moveTo(1)

    fun previous() = moveTo(-1)

    fun jumpTo(questionNumber: Int) {
        val state = _uiState.value
        val session = state.session ?: return
        val total = state.paper?.totalQuestions ?: return
        val target = questionNumber.coerceIn(1, total)
        if (target == session.currentQuestionNumber) {
            closePalette()
            return
        }
        _uiState.update {
            it.copy(
                showPalette = false,
                session = it.session?.copy(currentQuestionNumber = target),
            )
        }
        viewModelScope.launch { setCurrentQuestion(paperId, target) }
    }

    fun toggleLanguage() {
        val current = _language.value
        val next = if (current == ContentLanguage.ENGLISH) ContentLanguage.TELUGU else ContentLanguage.ENGLISH
        _language.value = next
        _uiState.update { it.copy(language = next) }
    }

    fun openPalette() = _uiState.update { it.copy(showPalette = true) }

    fun closePalette() = _uiState.update { it.copy(showPalette = false) }

    fun requestSubmit() = _uiState.update { it.copy(showSubmitDialog = true) }

    fun dismissSubmit() = _uiState.update { it.copy(showSubmitDialog = false) }

    fun confirmSubmit() {
        _uiState.update { it.copy(showSubmitDialog = false) }
        submit()
    }

    fun requestExit() = _uiState.update { it.copy(showExitDialog = true) }

    fun dismissExit() = _uiState.update { it.copy(showExitDialog = false) }

    fun confirmExit() {
        _uiState.update { it.copy(showExitDialog = false) }
        submit()
    }

    private fun submit() {
        autoSubmitted.compareAndSet(false, true)
        viewModelScope.launch { submitExam(paperId) }
    }

    private fun moveTo(delta: Int) {
        val state = _uiState.value
        val session = state.session ?: return
        val total = state.paper?.totalQuestions ?: return
        val target = (session.currentQuestionNumber + delta).coerceIn(1, total)
        if (target == session.currentQuestionNumber) return
        jumpTo(target)
    }

    private fun updateAnswer(answer: AttemptAnswer) {
        _uiState.update { state ->
            val session = state.session ?: return@update state
            state.copy(session = session.copy(answers = session.answers + (answer.questionNumber to answer)))
        }
        answerEvents.tryEmit(answer)
    }

    private fun stateFrom(paper: Paper, session: ExamSession, remainingMs: Long): ExamUiState {
        val previous = _uiState.value
        return ExamUiState(
            paperId = paperId,
            loading = false,
            error = null,
            paper = paper,
            session = session,
            language = previous.language,
            remainingMs = remainingMs,
            showPalette = previous.showPalette,
            showSubmitDialog = previous.showSubmitDialog,
            showExitDialog = previous.showExitDialog,
            result = if (session.status == ExamStatus.SUBMITTED) {
                ScoreCalculator.compute(
                    paper = paper,
                    answers = session.answers,
                    timeTakenMs = session.submittedAtEpochMs
                        ?.minus(session.startedAtEpochMs)
                        ?.coerceAtLeast(0L) ?: 0L,
                )
            } else null,
        )
    }

    private fun clockFlow(): Flow<Pair<Long, Long>> = flow {
        while (true) {
            emit(SystemClock.elapsedRealtime() to System.currentTimeMillis())
            delay(1_000)
        }
    }

    companion object {
        const val KEY_PAPER_ID = "paperId"
    }

    @AssistedFactory
    interface Factory {
        fun create(paperId: String): ExamViewModel
    }
}