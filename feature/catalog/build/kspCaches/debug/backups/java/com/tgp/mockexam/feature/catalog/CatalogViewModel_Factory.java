package com.tgp.mockexam.feature.catalog;

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
public final class CatalogViewModel_Factory implements Factory<CatalogViewModel> {
  private final Provider<PaperRepository> paperRepositoryProvider;

  private CatalogViewModel_Factory(Provider<PaperRepository> paperRepositoryProvider) {
    this.paperRepositoryProvider = paperRepositoryProvider;
  }

  @Override
  public CatalogViewModel get() {
    return newInstance(paperRepositoryProvider.get());
  }

  public static CatalogViewModel_Factory create(Provider<PaperRepository> paperRepositoryProvider) {
    return new CatalogViewModel_Factory(paperRepositoryProvider);
  }

  public static CatalogViewModel newInstance(PaperRepository paperRepository) {
    return new CatalogViewModel(paperRepository);
  }
}
