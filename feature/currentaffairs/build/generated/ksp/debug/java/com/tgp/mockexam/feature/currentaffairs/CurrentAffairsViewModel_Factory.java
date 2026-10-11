package com.tgp.mockexam.feature.currentaffairs;

import com.tgp.mockexam.core.domain.repository.CurrentAffairsRepository;
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
public final class CurrentAffairsViewModel_Factory implements Factory<CurrentAffairsViewModel> {
  private final Provider<CurrentAffairsRepository> repositoryProvider;

  private CurrentAffairsViewModel_Factory(Provider<CurrentAffairsRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public CurrentAffairsViewModel get() {
    return newInstance(repositoryProvider.get());
  }

  public static CurrentAffairsViewModel_Factory create(
      Provider<CurrentAffairsRepository> repositoryProvider) {
    return new CurrentAffairsViewModel_Factory(repositoryProvider);
  }

  public static CurrentAffairsViewModel newInstance(CurrentAffairsRepository repository) {
    return new CurrentAffairsViewModel(repository);
  }
}
