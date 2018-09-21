package net.justaddmusic.tetrisrepos.di

import dagger.Module
import dagger.Provides
import net.justaddmusic.tetrisrepos.network.GitHubSearchService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class AppModule {

    @Provides
    @Singleton
    fun provideRetrofit() = Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun provideGitHubSearchService(retrofit: Retrofit) =
            retrofit.create(GitHubSearchService::class.java)

}

