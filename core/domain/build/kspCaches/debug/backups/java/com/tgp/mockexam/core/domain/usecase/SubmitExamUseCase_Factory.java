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
public final class SubmitExamUseCase_Factory implements Factory<SubmitExamUseCase> {
  private final Provider<ExamSessionRepository> examSessionRepositoryProvider;

  private SubmitExamUseCase_Factory(Provider<ExamSessionRepository> examSessionRepositoryProvider) {
    this.examSessionRepositoryProvider = examSessionRepositoryProvider;
  }

  @Override
  public SubmitExamUseCase get() {
    return newInstance(examSessionRepositoryProvider.get());
  }

  public static SubmitExamUseCase_Factory create(
      Provider<ExamSessionRepository> examSessionRepositoryProvider) {
    return new SubmitExamUseCase_Factory(examSessionRepositoryProvider);
  }

  public static SubmitExamUseCase newInstance(ExamSessionRepository examSessionRepository) {
    return new SubmitExamUseCase(examSessionRepository);
  }
}
