package com.yan.namecard.app;

import android.app.Application;

import com.yan.namecard.util.ToastHelper;


/**
 * Created by yan on 2016/10/5.
 */
public class MainApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ToastHelper.getInstance().setContext(getBaseContext());
    }

}
