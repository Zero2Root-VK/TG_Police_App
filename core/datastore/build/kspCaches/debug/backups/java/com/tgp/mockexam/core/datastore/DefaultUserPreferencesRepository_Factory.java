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
public final class DefaultUserPreferencesRepository_Factory implements Factory<DefaultUserPreferencesRepository> {
  private final Provider<DataStore<Preferences>> dataStoreProvider;

  private DefaultUserPreferencesRepository_Factory(
      Provider<DataStore<Preferences>> dataStoreProvider) {
    this.dataStoreProvider = dataStoreProvider;
  }

  @Override
  public DefaultUserPreferencesRepository get() {
    return newInstance(dataStoreProvider.get());
  }

  public static DefaultUserPreferencesRepository_Factory create(
      Provider<DataStore<Preferences>> dataStoreProvider) {
    return new DefaultUserPreferencesRepository_Factory(dataStoreProvider);
  }

  public static DefaultUserPreferencesRepository newInstance(DataStore<Preferences> dataStore) {
    return new DefaultUserPreferencesRepository(dataStore);
  }
}
