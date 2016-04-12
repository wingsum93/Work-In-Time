package com.ericho.workintime;

import android.app.Application;
import android.content.Context;

import com.facebook.stetho.Stetho;

import org.xutils.x;

/**
 * Created by steve_000 on 2016/4/12.
 * for project Work In Time
 * package name PACKAGE_NAME
 */
public class MyApplication extends Application {
    public static Context temp ;
    public static Context getContext(){
        return temp;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        temp = getContext();
        Stetho.initializeWithDefaults(this);// browser debug
        x.Ext.init(this);
        x.Ext.setDebug(true); // ?臬颲debug?亙?

    }
}
