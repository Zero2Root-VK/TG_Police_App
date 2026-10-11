package com.tgp.mockexam.core.data.repository;

import com.tgp.mockexam.core.database.dao.ExamSessionDao;
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
public final class RoomExamSessionRepository_Factory implements Factory<RoomExamSessionRepository> {
  private final Provider<ExamSessionDao> daoProvider;

  private RoomExamSessionRepository_Factory(Provider<ExamSessionDao> daoProvider) {
    this.daoProvider = daoProvider;
  }

  @Override
  public RoomExamSessionRepository get() {
    return newInstance(daoProvider.get());
  }

  public static RoomExamSessionRepository_Factory create(Provider<ExamSessionDao> daoProvider) {
    return new RoomExamSessionRepository_Factory(daoProvider);
  }

  public static RoomExamSessionRepository newInstance(ExamSessionDao dao) {
    return new RoomExamSessionRepository(dao);
  }
}
