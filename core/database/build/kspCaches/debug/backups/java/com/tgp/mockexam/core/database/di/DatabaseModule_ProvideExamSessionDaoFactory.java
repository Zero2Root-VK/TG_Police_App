package com.tgp.mockexam.core.database.di;

import com.tgp.mockexam.core.database.TgpDatabase;
import com.tgp.mockexam.core.database.dao.ExamSessionDao;
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
public final class DatabaseModule_ProvideExamSessionDaoFactory implements Factory<ExamSessionDao> {
  private final Provider<TgpDatabase> databaseProvider;

  private DatabaseModule_ProvideExamSessionDaoFactory(Provider<TgpDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public ExamSessionDao get() {
    return provideExamSessionDao(databaseProvider.get());
  }

  public static DatabaseModule_ProvideExamSessionDaoFactory create(
      Provider<TgpDatabase> databaseProvider) {
    return new DatabaseModule_ProvideExamSessionDaoFactory(databaseProvider);
  }

  public static ExamSessionDao provideExamSessionDao(TgpDatabase database) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideExamSessionDao(database));
  }
}
