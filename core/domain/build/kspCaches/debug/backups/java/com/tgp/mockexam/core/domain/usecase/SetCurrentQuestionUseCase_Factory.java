package com.tgp.mockexam.core.domain.usecase;

import com.tgp.mockexam.core.domain.repository.ExamSessionRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
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
public final class SetCurrentQuestionUseCase_Factory implements Factory<SetCurrentQuestionUseCase> {
  private final Provider<ExamSessionRepository> examSessionRepositoryProvider;

  private SetCurrentQuestionUseCase_Factory(
      Provider<ExamSessionRepository> examSessionRepositoryProvider) {
    this.examSessionRepositoryProvider = examSessionRepositoryProvider;
  }

  @Override
  public SetCurrentQuestionUseCase get() {
    return newInstance(examSessionRepositoryProvider.get());
  }

  public static SetCurrentQuestionUseCase_Factory create(
      Provider<ExamSessionRepository> examSessionRepositoryProvider) {
    return new SetCurrentQuestionUseCase_Factory(examSessionRepositoryProvider);
  }

  public static SetCurrentQuestionUseCase newInstance(ExamSessionRepository examSessionRepository) {
    return new SetCurrentQuestionUseCase(examSessionRepository);
  }
}
