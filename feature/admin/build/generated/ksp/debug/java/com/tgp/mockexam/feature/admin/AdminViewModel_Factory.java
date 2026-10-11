package com.tgp.mockexam.feature.admin;

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
public final class AdminViewModel_Factory implements Factory<AdminViewModel> {
  private final Provider<PaperRepository> paperRepositoryProvider;

  private AdminViewModel_Factory(Provider<PaperRepository> paperRepositoryProvider) {
    this.paperRepositoryProvider = paperRepositoryProvider;
  }

  @Override
  public AdminViewModel get() {
    return newInstance(paperRepositoryProvider.get());
  }

  public static AdminViewModel_Factory create(Provider<PaperRepository> paperRepositoryProvider) {
    return new AdminViewModel_Factory(paperRepositoryProvider);
  }

  public static AdminViewModel newInstance(PaperRepository paperRepository) {
    return new AdminViewModel(paperRepository);
  }
}
