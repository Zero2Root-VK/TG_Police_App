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
public final class SaveAnswerUseCase_Factory implements Factory<SaveAnswerUseCase> {
  private final Provider<ExamSessionRepository> examSessionRepositoryProvider;

  private SaveAnswerUseCase_Factory(Provider<ExamSessionRepository> examSessionRepositoryProvider) {
    this.examSessionRepositoryProvider = examSessionRepositoryProvider;
  }

  @Override
  public SaveAnswerUseCase get() {
    return newInstance(examSessionRepositoryProvider.get());
  }

  public static SaveAnswerUseCase_Factory create(
      Provider<ExamSessionRepository> examSessionRepositoryProvider) {
    return new SaveAnswerUseCase_Factory(examSessionRepositoryProvider);
  }

  public static SaveAnswerUseCase newInstance(ExamSessionRepository examSessionRepository) {
    return new SaveAnswerUseCase(examSessionRepository);
  }
}
