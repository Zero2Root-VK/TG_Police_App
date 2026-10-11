package com.tgp.mockexam.core.database.di;

import android.content.Context;
import com.tgp.mockexam.core.database.TgpDatabase;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.Provider;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata("dagger.hilt.android.qualifiers.ApplicationContext")
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
public final class DatabaseModule_ProvideTgpDatabaseFactory implements Factory<TgpDatabase> {
  private final Provider<Context> contextProvider;

  private DatabaseModule_ProvideTgpDatabaseFactory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public TgpDatabase get() {
    return provideTgpDatabase(contextProvider.get());
  }

  public static DatabaseModule_ProvideTgpDatabaseFactory create(Provider<Context> contextProvider) {
    return new DatabaseModule_ProvideTgpDatabaseFactory(contextProvider);
  }

  public static TgpDatabase provideTgpDatabase(Context context) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideTgpDatabase(context));
  }
}
