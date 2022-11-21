package ru.iteco.fmhandroid.api

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import ru.iteco.fmhandroid.api.qualifier.NonAuthorized
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module(includes = [NetworkModule::class])
object AuthApiModule {
    @Provides
    @Singleton
    fun provideAuthApi(@NonAuthorized retrofit: Retrofit): AuthApi {
        return retrofit
            .create(AuthApi::class.java)
    }
}
