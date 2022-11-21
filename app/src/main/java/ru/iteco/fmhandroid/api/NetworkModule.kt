package ru.iteco.fmhandroid.api

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.iteco.fmhandroid.BuildConfig
import ru.iteco.fmhandroid.api.qualifier.Authorized
import ru.iteco.fmhandroid.api.qualifier.NonAuthorized
import ru.iteco.fmhandroid.api.qualifier.Refresh
import ru.iteco.fmhandroid.auth.AppAuth
import ru.iteco.fmhandroid.repository.authRepository.AuthRepository
import javax.inject.Provider

@InstallIn(SingletonComponent::class)
@Module
object NetworkModule {
    @Provides
    fun loggingInterceptor() = HttpLoggingInterceptor()
        .apply {
            if (BuildConfig.DEBUG) {
                level = HttpLoggingInterceptor.Level.BODY
            }
        }

    @NonAuthorized
    @Provides
    fun provideNonAuthorizedRetrofit(@NonAuthorized client: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BuildConfig.BASE_URL)
            .client(client)
            .build()

    @Authorized
    @Provides
    fun provideAuthorizedRetrofit(@Authorized client: OkHttpClient): Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BuildConfig.BASE_URL)
        .client(client)
        .build()

    @Authorized
    @Provides
    fun authorizedOkhttp(
        interceptor: HttpLoggingInterceptor,
        appAuth: AppAuth,
        authRepositoryProvider: Provider<AuthRepository>
    ): OkHttpClient {
        val authInterceptor = AuthInterceptor(appAuth)
        val refreshAuthenticator = RefreshAuthenticator(authRepositoryProvider, appAuth)
        return OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .addInterceptor(authInterceptor)
            .authenticator(refreshAuthenticator)
            .build()
    }

    @NonAuthorized
    @Provides
    fun nonAuthorizedOkhttp(interceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()
    }

    @Refresh
    @Provides
    fun refreshOkhttp(interceptor: HttpLoggingInterceptor, appAuth: AppAuth): OkHttpClient {
        val refreshInterceptor = RefreshInterceptor(appAuth)
        return OkHttpClient.Builder()
            .addInterceptor(refreshInterceptor)
            .addInterceptor(interceptor)
            .build()
    }

    @Refresh
    @Provides
    fun provideRefreshRetrofit(@Refresh client: OkHttpClient): Retrofit =
        Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BuildConfig.BASE_URL)
            .client(client)
            .build()
}
