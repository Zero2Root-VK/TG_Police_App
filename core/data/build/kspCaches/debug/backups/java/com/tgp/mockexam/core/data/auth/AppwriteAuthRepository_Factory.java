package com.tgp.mockexam.core.data.auth;

import com.tgp.mockexam.core.domain.repository.SessionRepository;
import com.tgp.mockexam.core.network.AccountClient;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Provider;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata("javax.inject.Singleton")
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
public final class AppwriteAuthRepository_Factory implements Factory<AppwriteAuthRepository> {
  private final Provider<AccountClient> accountClientProvider;

  private final Provider<SessionRepository> sessionRepositoryProvider;

  private AppwriteAuthRepository_Factory(Provider<AccountClient> accountClientProvider,
      Provider<SessionRepository> sessionRepositoryProvider) {
    this.accountClientProvider = accountClientProvider;
    this.sessionRepositoryProvider = sessionRepositoryProvider;
  }

  @Override
  public AppwriteAuthRepository get() {
    return newInstance(accountClientProvider.get(), sessionRepositoryProvider.get());
  }

  public static AppwriteAuthRepository_Factory create(Provider<AccountClient> accountClientProvider,
      Provider<SessionRepository> sessionRepositoryProvider) {
    return new AppwriteAuthRepository_Factory(accountClientProvider, sessionRepositoryProvider);
  }

  public static AppwriteAuthRepository newInstance(AccountClient accountClient,
      SessionRepository sessionRepository) {
    return new AppwriteAuthRepository(accountClient, sessionRepository);
  }
}
