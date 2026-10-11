package com.tgp.mockexam.feature.admin;

import com.tgp.mockexam.core.domain.repository.PaperRepository;
import com.tgp.mockexam.core.domain.repository.UserPreferencesRepository;
import com.tgp.mockexam.core.network.AdminWriteClient;
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

  private final Provider<UserPreferencesRepository> preferencesProvider;

  private final Provider<AdminWriteClient> writeClientProvider;

  private AdminViewModel_Factory(Provider<PaperRepository> paperRepositoryProvider,
      Provider<UserPreferencesRepository> preferencesProvider,
      Provider<AdminWriteClient> writeClientProvider) {
    this.paperRepositoryProvider = paperRepositoryProvider;
    this.preferencesProvider = preferencesProvider;
    this.writeClientProvider = writeClientProvider;
  }

  @Override
  public AdminViewModel get() {
    return newInstance(paperRepositoryProvider.get(), preferencesProvider.get(), writeClientProvider.get());
  }

  public static AdminViewModel_Factory create(Provider<PaperRepository> paperRepositoryProvider,
      Provider<UserPreferencesRepository> preferencesProvider,
      Provider<AdminWriteClient> writeClientProvider) {
    return new AdminViewModel_Factory(paperRepositoryProvider, preferencesProvider, writeClientProvider);
  }

  public static AdminViewModel newInstance(PaperRepository paperRepository,
      UserPreferencesRepository preferences, AdminWriteClient writeClient) {
    return new AdminViewModel(paperRepository, preferences, writeClient);
  }
}
