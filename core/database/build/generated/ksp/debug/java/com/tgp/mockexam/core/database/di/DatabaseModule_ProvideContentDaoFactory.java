package com.tgp.mockexam.core.database.di;

import com.tgp.mockexam.core.database.TgpDatabase;
import com.tgp.mockexam.core.database.dao.ContentDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
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
public final class DatabaseModule_ProvideContentDaoFactory implements Factory<ContentDao> {
  private final Provider<TgpDatabase> databaseProvider;

  private DatabaseModule_ProvideContentDaoFactory(Provider<TgpDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public ContentDao get() {
    return provideContentDao(databaseProvider.get());
  }

  public static DatabaseModule_ProvideContentDaoFactory create(
      Provider<TgpDatabase> databaseProvider) {
    return new DatabaseModule_ProvideContentDaoFactory(databaseProvider);
  }

  public static ContentDao provideContentDao(TgpDatabase database) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideContentDao(database));
  }
}
