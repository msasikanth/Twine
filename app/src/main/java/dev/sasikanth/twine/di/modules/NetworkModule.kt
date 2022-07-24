package dev.sasikanth.twine.di.modules

import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.sasikanth.twine.auth.TwineAuthInterceptor
import dev.sasikanth.twine.common.moshi.LocalDateTimeMoshiAdapter
import dev.sasikanth.twine.common.utils.Constants.BASE_API_URL
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

  @Provides
  @Singleton
  fun providesOkHttpClient(
    twineAuthInterceptor: TwineAuthInterceptor
  ): OkHttpClient {
    return OkHttpClient.Builder()
      .addInterceptor(twineAuthInterceptor)
      .build()
  }

  @Provides
  @Singleton
  fun providesMoshi(): Moshi {
    return Moshi.Builder()
      .add(LocalDateTimeMoshiAdapter())
      .build()
  }

  @Provides
  @Singleton
  fun providesRetrofit(
    okHttpClient: OkHttpClient,
    moshi: Moshi
  ): Retrofit {
    return Retrofit.Builder()
      .baseUrl(BASE_API_URL)
      .client(okHttpClient)
      .addConverterFactory(MoshiConverterFactory.create(moshi))
      .addConverterFactory(ScalarsConverterFactory.create())
      .build()
  }
}
