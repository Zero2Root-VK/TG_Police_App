package com.tgp.mockexam.feature.auth;

import com.tgp.mockexam.core.domain.repository.AuthRepository;
import com.tgp.mockexam.core.domain.repository.ProfileRepository;
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
public final class AuthViewModel_Factory implements Factory<AuthViewModel> {
  private final Provider<AuthRepository> authRepositoryProvider;

  private final Provider<ProfileRepository> profileRepositoryProvider;

  private final Provider<UserPreferencesRepository> preferencesProvider;

  private AuthViewModel_Factory(Provider<AuthRepository> authRepositoryProvider,
      Provider<ProfileRepository> profileRepositoryProvider,
      Provider<UserPreferencesRepository> preferencesProvider) {
    this.authRepositoryProvider = authRepositoryProvider;
    this.profileRepositoryProvider = profileRepositoryProvider;
    this.preferencesProvider = preferencesProvider;
  }

  @Override
  public AuthViewModel get() {
    return newInstance(authRepositoryProvider.get(), profileRepositoryProvider.get(), preferencesProvider.get());
  }

  public static AuthViewModel_Factory create(Provider<AuthRepository> authRepositoryProvider,
      Provider<ProfileRepository> profileRepositoryProvider,
      Provider<UserPreferencesRepository> preferencesProvider) {
    return new AuthViewModel_Factory(authRepositoryProvider, profileRepositoryProvider, preferencesProvider);
  }

  public static AuthViewModel newInstance(AuthRepository authRepository,
      ProfileRepository profileRepository, UserPreferencesRepository preferences) {
    return new AuthViewModel(authRepository, profileRepository, preferences);
  }
}
