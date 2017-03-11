package com.aaa.gerrix.volleycache;

import android.app.Application;

import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;


public class VolleyCache extends Application {

    @Override
    public void onCreate() {

        FlowManager.init(new FlowConfig.Builder(this).build());
    }
}
