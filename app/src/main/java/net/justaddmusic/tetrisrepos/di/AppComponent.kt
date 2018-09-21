package net.justaddmusic.tetrisrepos.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import net.justaddmusic.tetrisrepos.repository.GitHubRepository
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance fun context(context: Context): Builder
        fun build(): AppComponent
    }

    fun gitHubRepository(): GitHubRepository
}