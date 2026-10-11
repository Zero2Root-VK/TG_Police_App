package com.tgp.mockexam.bootstrap;

import com.tgp.mockexam.core.domain.repository.UserPreferencesRepository;
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
public final class BootstrapViewModel_Factory implements Factory<BootstrapViewModel> {
  private final Provider<UserPreferencesRepository> preferencesProvider;

  private BootstrapViewModel_Factory(Provider<UserPreferencesRepository> preferencesProvider) {
    this.preferencesProvider = preferencesProvider;
  }

  @Override
  public BootstrapViewModel get() {
    return newInstance(preferencesProvider.get());
  }

  public static BootstrapViewModel_Factory create(
      Provider<UserPreferencesRepository> preferencesProvider) {
    return new BootstrapViewModel_Factory(preferencesProvider);
  }

  public static BootstrapViewModel newInstance(UserPreferencesRepository preferences) {
    return new BootstrapViewModel(preferences);
  }
}
