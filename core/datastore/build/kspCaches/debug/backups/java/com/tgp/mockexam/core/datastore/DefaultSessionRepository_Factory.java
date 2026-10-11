package com.tgp.mockexam.core.datastore;

import androidx.datastore.core.DataStore;
import androidx.datastore.preferences.core.Preferences;
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
public final class DefaultSessionRepository_Factory implements Factory<DefaultSessionRepository> {
  private final Provider<DataStore<Preferences>> dataStoreProvider;

  private DefaultSessionRepository_Factory(Provider<DataStore<Preferences>> dataStoreProvider) {
    this.dataStoreProvider = dataStoreProvider;
  }

  @Override
  public DefaultSessionRepository get() {
    return newInstance(dataStoreProvider.get());
  }

  public static DefaultSessionRepository_Factory create(
      Provider<DataStore<Preferences>> dataStoreProvider) {
    return new DefaultSessionRepository_Factory(dataStoreProvider);
  }

  public static DefaultSessionRepository newInstance(DataStore<Preferences> dataStore) {
    return new DefaultSessionRepository(dataStore);
  }
}
