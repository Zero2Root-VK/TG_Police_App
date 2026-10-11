package com.tgp.mockexam.core.data.di;

import com.tgp.mockexam.core.network.AppwriteConfig;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
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
public final class DataModule_Companion_ProvideAppwriteConfigFactory implements Factory<AppwriteConfig> {
  @Override
  public AppwriteConfig get() {
    return provideAppwriteConfig();
  }

  public static DataModule_Companion_ProvideAppwriteConfigFactory create() {
    return InstanceHolder.INSTANCE;
  }

  public static AppwriteConfig provideAppwriteConfig() {
    return Preconditions.checkNotNullFromProvides(DataModule.Companion.provideAppwriteConfig());
  }

  private static final class InstanceHolder {
    static final DataModule_Companion_ProvideAppwriteConfigFactory INSTANCE = new DataModule_Companion_ProvideAppwriteConfigFactory();
  }
}
