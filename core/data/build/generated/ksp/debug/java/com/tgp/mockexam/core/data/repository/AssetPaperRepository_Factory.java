package com.tgp.mockexam.core.data.repository;

import android.content.Context;
import com.tgp.mockexam.core.common.DispatcherProvider;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
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
public final class AssetPaperRepository_Factory implements Factory<AssetPaperRepository> {
  private final Provider<Context> contextProvider;

  private final Provider<DispatcherProvider> dispatchersProvider;

  private AssetPaperRepository_Factory(Provider<Context> contextProvider,
      Provider<DispatcherProvider> dispatchersProvider) {
    this.contextProvider = contextProvider;
    this.dispatchersProvider = dispatchersProvider;
  }

  @Override
  public AssetPaperRepository get() {
    return newInstance(contextProvider.get(), dispatchersProvider.get());
  }

  public static AssetPaperRepository_Factory create(Provider<Context> contextProvider,
      Provider<DispatcherProvider> dispatchersProvider) {
    return new AssetPaperRepository_Factory(contextProvider, dispatchersProvider);
  }

  public static AssetPaperRepository newInstance(Context context, DispatcherProvider dispatchers) {
    return new AssetPaperRepository(context, dispatchers);
  }
}
