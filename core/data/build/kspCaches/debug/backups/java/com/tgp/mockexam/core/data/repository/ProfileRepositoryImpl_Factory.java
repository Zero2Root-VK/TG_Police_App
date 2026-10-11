package com.tgp.mockexam.core.data.repository;

import com.tgp.mockexam.core.network.TablesClient;
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
public final class ProfileRepositoryImpl_Factory implements Factory<ProfileRepositoryImpl> {
  private final Provider<TablesClient> tablesClientProvider;

  private ProfileRepositoryImpl_Factory(Provider<TablesClient> tablesClientProvider) {
    this.tablesClientProvider = tablesClientProvider;
  }

  @Override
  public ProfileRepositoryImpl get() {
    return newInstance(tablesClientProvider.get());
  }

  public static ProfileRepositoryImpl_Factory create(Provider<TablesClient> tablesClientProvider) {
    return new ProfileRepositoryImpl_Factory(tablesClientProvider);
  }

  public static ProfileRepositoryImpl newInstance(TablesClient tablesClient) {
    return new ProfileRepositoryImpl(tablesClient);
  }
}
