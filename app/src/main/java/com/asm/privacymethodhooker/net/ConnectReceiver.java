package com.asm.privacymethodhooker.net;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.asm.privacymethodhooker.utils.NetWorkUtil;

public class ConnectReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false)) {
            Log.i("TAG", "netWork has lost");
        }

        NetWorkUtil.mNetworkInfo = (NetworkInfo)
                intent.getExtras().get(ConnectivityManager.EXTRA_NETWORK_INFO);
        Log.i("TAG", NetWorkUtil.mNetworkInfo.toString() + " {isConnected = " + NetWorkUtil.mNetworkInfo.isConnected() + "}");
    }
}
