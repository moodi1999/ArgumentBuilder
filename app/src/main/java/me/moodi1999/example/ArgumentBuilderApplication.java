package me.moodi1999.example;

import android.app.Application;
import android.content.Context;
import androidx.multidex.MultiDex;

public class ArgumentBuilderApplication extends Application {

    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
