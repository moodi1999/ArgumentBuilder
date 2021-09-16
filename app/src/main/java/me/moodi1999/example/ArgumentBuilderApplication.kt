package me.moodi1999.example

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex

class ArgumentBuilderApplication : Application() {
    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }
}