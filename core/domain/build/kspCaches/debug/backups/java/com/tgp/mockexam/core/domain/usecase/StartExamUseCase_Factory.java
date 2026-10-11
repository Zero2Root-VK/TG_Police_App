package com.tgp.mockexam.core.domain.usecase;

import com.tgp.mockexam.core.domain.repository.ExamSessionRepository;
import com.tgp.mockexam.core.domain.repository.PaperRepository;
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
public final class StartExamUseCase_Factory implements Factory<StartExamUseCase> {
  private final Provider<PaperRepository> paperRepositoryProvider;

  private final Provider<ExamSessionRepository> examSessionRepositoryProvider;

  private StartExamUseCase_Factory(Provider<PaperRepository> paperRepositoryProvider,
      Provider<ExamSessionRepository> examSessionRepositoryProvider) {
    this.paperRepositoryProvider = paperRepositoryProvider;
    this.examSessionRepositoryProvider = examSessionRepositoryProvider;
  }

  @Override
  public StartExamUseCase get() {
    return newInstance(paperRepositoryProvider.get(), examSessionRepositoryProvider.get());
  }

  public static StartExamUseCase_Factory create(Provider<PaperRepository> paperRepositoryProvider,
      Provider<ExamSessionRepository> examSessionRepositoryProvider) {
    return new StartExamUseCase_Factory(paperRepositoryProvider, examSessionRepositoryProvider);
  }

  public static StartExamUseCase newInstance(PaperRepository paperRepository,
      ExamSessionRepository examSessionRepository) {
    return new StartExamUseCase(paperRepository, examSessionRepository);
  }
}
