package ru.iteco.fmhandroid.api

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import ru.iteco.fmhandroid.api.qualifier.Authorized
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module(includes = [NetworkModule::class])
object ApiModule {
    @Provides
    @Singleton
    fun provideClaimApi(@Authorized retrofit: Retrofit): ClaimApi {
        return retrofit
            .create(ClaimApi::class.java)
    }

    @Provides
    @Singleton
    fun provideUserApi(@Authorized retrofit: Retrofit): UserApi {
        return retrofit
            .create(UserApi::class.java)
    }

    @Provides
    @Singleton
    fun provideNewsApi(@Authorized retrofit: Retrofit): NewsApi {
        return retrofit
            .create(NewsApi::class.java)
    }
}
