package ru.iteco.fmhandroid.api

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import ru.iteco.fmhandroid.api.qualifier.Refresh
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module(includes = [NetworkModule::class])
object RefreshApiModule {
    @Provides
    @Singleton
    fun provideRefreshTokensApi(@Refresh retrofit: Retrofit): RefreshTokensApi {
        return retrofit
            .create(RefreshTokensApi::class.java)
    }
}
