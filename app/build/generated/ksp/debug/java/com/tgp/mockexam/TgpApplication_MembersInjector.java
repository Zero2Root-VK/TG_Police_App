package com.tgp.mockexam;

import com.tgp.mockexam.core.data.sync.AppwriteContentSyncer;
import dagger.MembersInjector;
import dagger.internal.DaggerGenerated;
import dagger.internal.InjectedFieldSignature;
import dagger.internal.Provider;
import dagger.internal.QualifierMetadata;
import javax.annotation.processing.Generated;

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
public final class TgpApplication_MembersInjector implements MembersInjector<TgpApplication> {
  private final Provider<AppwriteContentSyncer> syncerProvider;

  private TgpApplication_MembersInjector(Provider<AppwriteContentSyncer> syncerProvider) {
    this.syncerProvider = syncerProvider;
  }

  @Override
  public void injectMembers(TgpApplication instance) {
    injectSyncer(instance, syncerProvider.get());
  }

  public static MembersInjector<TgpApplication> create(
      Provider<AppwriteContentSyncer> syncerProvider) {
    return new TgpApplication_MembersInjector(syncerProvider);
  }

  @InjectedFieldSignature("com.tgp.mockexam.TgpApplication.syncer")
  public static void injectSyncer(TgpApplication instance, AppwriteContentSyncer syncer) {
    instance.syncer = syncer;
  }
}
