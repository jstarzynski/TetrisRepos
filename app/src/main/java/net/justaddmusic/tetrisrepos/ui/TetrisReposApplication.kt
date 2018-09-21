package net.justaddmusic.tetrisrepos.ui

import android.app.Application
import net.justaddmusic.tetrisrepos.di.AppComponent
import net.justaddmusic.tetrisrepos.di.DaggerAppComponent

class TetrisReposApplication : Application() {

    val appComponent: AppComponent by lazy {
        DaggerAppComponent
                .builder()
                .context(applicationContext)
                .build()
    }

}