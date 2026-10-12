package com.tgp.mockexam.feature.exam;

import com.tgp.mockexam.core.domain.usecase.ObserveExamRunUseCase;
import com.tgp.mockexam.core.domain.usecase.ResetExamUseCase;
import com.tgp.mockexam.core.domain.usecase.SaveAnswerUseCase;
import com.tgp.mockexam.core.domain.usecase.SetCurrentQuestionUseCase;
import com.tgp.mockexam.core.domain.usecase.StartExamUseCase;
import com.tgp.mockexam.core.domain.usecase.SubmitExamUseCase;
import dagger.internal.DaggerGenerated;
import dagger.internal.Provider;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast",
    "deprecation",
    "nullness:initialization.field.uninitialized"
})
public final class ExamViewModel_Factory {
  private final Provider<ObserveExamRunUseCase> observeExamRunProvider;

  private final Provider<StartExamUseCase> startExamProvider;

  private final Provider<SaveAnswerUseCase> saveAnswerProvider;

  private final Provider<SetCurrentQuestionUseCase> setCurrentQuestionProvider;

  private final Provider<SubmitExamUseCase> submitExamProvider;

  private final Provider<ResetExamUseCase> resetExamProvider;

  private ExamViewModel_Factory(Provider<ObserveExamRunUseCase> observeExamRunProvider,
      Provider<StartExamUseCase> startExamProvider, Provider<SaveAnswerUseCase> saveAnswerProvider,
      Provider<SetCurrentQuestionUseCase> setCurrentQuestionProvider,
      Provider<SubmitExamUseCase> submitExamProvider,
      Provider<ResetExamUseCase> resetExamProvider) {
    this.observeExamRunProvider = observeExamRunProvider;
    this.startExamProvider = startExamProvider;
    this.saveAnswerProvider = saveAnswerProvider;
    this.setCurrentQuestionProvider = setCurrentQuestionProvider;
    this.submitExamProvider = submitExamProvider;
    this.resetExamProvider = resetExamProvider;
  }

  public ExamViewModel get(String paperId) {
    return newInstance(paperId, observeExamRunProvider.get(), startExamProvider.get(), saveAnswerProvider.get(), setCurrentQuestionProvider.get(), submitExamProvider.get(), resetExamProvider.get());
  }

  public static ExamViewModel_Factory create(Provider<ObserveExamRunUseCase> observeExamRunProvider,
      Provider<StartExamUseCase> startExamProvider, Provider<SaveAnswerUseCase> saveAnswerProvider,
      Provider<SetCurrentQuestionUseCase> setCurrentQuestionProvider,
      Provider<SubmitExamUseCase> submitExamProvider,
      Provider<ResetExamUseCase> resetExamProvider) {
    return new ExamViewModel_Factory(observeExamRunProvider, startExamProvider, saveAnswerProvider, setCurrentQuestionProvider, submitExamProvider, resetExamProvider);
  }

  public static ExamViewModel newInstance(String paperId, ObserveExamRunUseCase observeExamRun,
      StartExamUseCase startExam, SaveAnswerUseCase saveAnswer,
      SetCurrentQuestionUseCase setCurrentQuestion, SubmitExamUseCase submitExam,
      ResetExamUseCase resetExam) {
    return new ExamViewModel(paperId, observeExamRun, startExam, saveAnswer, setCurrentQuestion, submitExam, resetExam);
  }
}
