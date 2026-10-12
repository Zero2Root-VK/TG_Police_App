package com.tgp.mockexam.feature.exam;

import dagger.internal.DaggerGenerated;
import dagger.internal.InstanceFactory;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
public final class ExamViewModel_Factory_Impl implements ExamViewModel.Factory {
  private final ExamViewModel_Factory delegateFactory;

  ExamViewModel_Factory_Impl(ExamViewModel_Factory delegateFactory) {
    this.delegateFactory = delegateFactory;
  }

  @Override
  public ExamViewModel create(String paperId) {
    return delegateFactory.get(paperId);
  }

  public static Provider<ExamViewModel.Factory> create(ExamViewModel_Factory delegateFactory) {
    return InstanceFactory.create(new ExamViewModel_Factory_Impl(delegateFactory));
  }

  public static dagger.internal.Provider<ExamViewModel.Factory> createFactoryProvider(
      ExamViewModel_Factory delegateFactory) {
    return InstanceFactory.create(new ExamViewModel_Factory_Impl(delegateFactory));
  }
}
