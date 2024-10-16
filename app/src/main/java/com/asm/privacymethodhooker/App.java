package com.asm.privacymethodhooker;

import static com.asm.privacymethodhooker.MainActivityKt.SVR_PUB_KEY;
import static glog.android.Glog.InternalLogLevel.InternalLogLevelDebug;
import static glog.android.Glog.InternalLogLevel.InternalLogLevelInfo;

import android.app.Application;
import android.content.Context;
import android.os.Environment;

import com.dianping.logan.Logan;
import com.dianping.logan.LoganConfig;
import com.getkeepsafe.relinker.ReLinker;
import com.tencent.mars.xlog.Log;
import com.tencent.mars.xlog.Xlog;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import glog.android.Glog;

public class App extends Application {

    public static Glog glog;

    public static Context appContext;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        appContext = base;

//        LoganConfig config = new LoganConfig.Builder()
//                .setCachePath(getApplicationContext().getFilesDir().getAbsolutePath())
//                .setPath(getApplicationContext().getExternalFilesDir(null).getAbsolutePath()
//                        + File.separator + "logan_v1")
//                .setEncryptKey16("0123456789012345".getBytes())
//                .setEncryptIV16("0123456789012345".getBytes())
//                .build();
//        Logan.init(config);

    }

    @Override
    public void onCreate() {
        super.onCreate();
//        registerReceiver(new ConnectReceiver(), new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        setupGlog();

    }

    private void setupGlog(){
        Glog.initialize(Glog.InternalLogLevel.InternalLogLevelDebug, new Glog.LibraryLoader() {
            @Override
            public void loadLibrary(String libName) {
                ReLinker.loadLibrary(appContext,libName);
            }
        });

    }

}
