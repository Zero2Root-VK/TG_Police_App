package com.tgp.mockexam.core.data.repository;

import com.tgp.mockexam.core.common.DispatcherProvider;
import com.tgp.mockexam.core.data.sync.AppwriteContentSyncer;
import com.tgp.mockexam.core.database.dao.ContentDao;
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
public final class CachedPaperRepository_Factory implements Factory<CachedPaperRepository> {
  private final Provider<AssetPaperRepository> assetRepositoryProvider;

  private final Provider<ContentDao> daoProvider;

  private final Provider<AppwriteContentSyncer> syncerProvider;

  private final Provider<DispatcherProvider> dispatchersProvider;

  private CachedPaperRepository_Factory(Provider<AssetPaperRepository> assetRepositoryProvider,
      Provider<ContentDao> daoProvider, Provider<AppwriteContentSyncer> syncerProvider,
      Provider<DispatcherProvider> dispatchersProvider) {
    this.assetRepositoryProvider = assetRepositoryProvider;
    this.daoProvider = daoProvider;
    this.syncerProvider = syncerProvider;
    this.dispatchersProvider = dispatchersProvider;
  }

  @Override
  public CachedPaperRepository get() {
    return newInstance(assetRepositoryProvider.get(), daoProvider.get(), syncerProvider.get(), dispatchersProvider.get());
  }

  public static CachedPaperRepository_Factory create(
      Provider<AssetPaperRepository> assetRepositoryProvider, Provider<ContentDao> daoProvider,
      Provider<AppwriteContentSyncer> syncerProvider,
      Provider<DispatcherProvider> dispatchersProvider) {
    return new CachedPaperRepository_Factory(assetRepositoryProvider, daoProvider, syncerProvider, dispatchersProvider);
  }

  public static CachedPaperRepository newInstance(AssetPaperRepository assetRepository,
      ContentDao dao, AppwriteContentSyncer syncer, DispatcherProvider dispatchers) {
    return new CachedPaperRepository(assetRepository, dao, syncer, dispatchers);
  }
}
