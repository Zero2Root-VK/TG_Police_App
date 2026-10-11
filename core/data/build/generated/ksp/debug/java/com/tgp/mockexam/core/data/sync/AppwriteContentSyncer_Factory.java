package com.tgp.mockexam.core.data.sync;

import com.tgp.mockexam.core.common.DispatcherProvider;
import com.tgp.mockexam.core.database.dao.ContentDao;
import com.tgp.mockexam.core.network.TablesClient;
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
public final class AppwriteContentSyncer_Factory implements Factory<AppwriteContentSyncer> {
  private final Provider<TablesClient> clientProvider;

  private final Provider<ContentDao> daoProvider;

  private final Provider<DispatcherProvider> dispatchersProvider;

  private AppwriteContentSyncer_Factory(Provider<TablesClient> clientProvider,
      Provider<ContentDao> daoProvider, Provider<DispatcherProvider> dispatchersProvider) {
    this.clientProvider = clientProvider;
    this.daoProvider = daoProvider;
    this.dispatchersProvider = dispatchersProvider;
  }

  @Override
  public AppwriteContentSyncer get() {
    return newInstance(clientProvider.get(), daoProvider.get(), dispatchersProvider.get());
  }

  public static AppwriteContentSyncer_Factory create(Provider<TablesClient> clientProvider,
      Provider<ContentDao> daoProvider, Provider<DispatcherProvider> dispatchersProvider) {
    return new AppwriteContentSyncer_Factory(clientProvider, daoProvider, dispatchersProvider);
  }

  public static AppwriteContentSyncer newInstance(TablesClient client, ContentDao dao,
      DispatcherProvider dispatchers) {
    return new AppwriteContentSyncer(client, dao, dispatchers);
  }
}
