package com.tgp.mockexam.feature.home;

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
public final class HomeViewModel_Factory implements Factory<HomeViewModel> {
  private final Provider<PaperRepository> paperRepositoryProvider;

  private HomeViewModel_Factory(Provider<PaperRepository> paperRepositoryProvider) {
    this.paperRepositoryProvider = paperRepositoryProvider;
  }

  @Override
  public HomeViewModel get() {
    return newInstance(paperRepositoryProvider.get());
  }

  public static HomeViewModel_Factory create(Provider<PaperRepository> paperRepositoryProvider) {
    return new HomeViewModel_Factory(paperRepositoryProvider);
  }

  public static HomeViewModel newInstance(PaperRepository paperRepository) {
    return new HomeViewModel(paperRepository);
  }
}
