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
public final class CurrentAffairsRepositoryImpl_Factory implements Factory<CurrentAffairsRepositoryImpl> {
  private final Provider<TablesClient> tablesClientProvider;

  private CurrentAffairsRepositoryImpl_Factory(Provider<TablesClient> tablesClientProvider) {
    this.tablesClientProvider = tablesClientProvider;
  }

  @Override
  public CurrentAffairsRepositoryImpl get() {
    return newInstance(tablesClientProvider.get());
  }

  public static CurrentAffairsRepositoryImpl_Factory create(
      Provider<TablesClient> tablesClientProvider) {
    return new CurrentAffairsRepositoryImpl_Factory(tablesClientProvider);
  }

  public static CurrentAffairsRepositoryImpl newInstance(TablesClient tablesClient) {
    return new CurrentAffairsRepositoryImpl(tablesClient);
  }
}
