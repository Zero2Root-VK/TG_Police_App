package com.tgp.mockexam.core.data.di;

import com.tgp.mockexam.core.network.AppwriteConfig;
import com.tgp.mockexam.core.network.TablesClient;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
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
public final class DataModule_Companion_ProvideTablesClientFactory implements Factory<TablesClient> {
  private final Provider<AppwriteConfig> configProvider;

  private DataModule_Companion_ProvideTablesClientFactory(Provider<AppwriteConfig> configProvider) {
    this.configProvider = configProvider;
  }

  @Override
  public TablesClient get() {
    return provideTablesClient(configProvider.get());
  }

  public static DataModule_Companion_ProvideTablesClientFactory create(
      Provider<AppwriteConfig> configProvider) {
    return new DataModule_Companion_ProvideTablesClientFactory(configProvider);
  }

  public static TablesClient provideTablesClient(AppwriteConfig config) {
    return Preconditions.checkNotNullFromProvides(DataModule.Companion.provideTablesClient(config));
  }
}
