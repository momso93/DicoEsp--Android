package com.rygelouv.dicoesp.commons;

import android.app.Application;
import android.content.Context;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by rygelouv on 31/05/16.
 */
public class EspDicoApplication extends Application
{
    private static EspDicoApplication ourInstance = new EspDicoApplication();

    public static EspDicoApplication getInstance()
    {
        return ourInstance;
    }

    private static Context mApplicationContext;

    private EspDicoApplication()
    {
    }

    @Override
    public void onCreate()
    {
        super.onCreate();

        mApplicationContext = getApplicationContext();

        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(this).build();
        Realm.setDefaultConfiguration(realmConfiguration);
    }

    public static Context provideApplicaitonContext()
    {
        return mApplicationContext;
    }
}
