package com.yulistic.pripic;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

/**
 * A BroadcastReceiver that checks Wifi connection. It will be used to skip sync job if Wifi is
 * disconnected.
 */
public class WifiStateReceiver extends BroadcastReceiver {

    private static boolean wifiConnected = false;
    public WifiStateReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager conMngr = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        android.net.NetworkInfo wifi = conMngr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo.State currentState = wifi.getState();
        if (!wifiConnected && currentState == NetworkInfo.State.CONNECTED){
            //TODO start syncing. To be implemented.
            Toast.makeText(context, "wifi connected", Toast.LENGTH_SHORT).show();
            Log.e("PriPic", "Wifi has been connected.");
        }
        else if (wifiConnected && currentState == NetworkInfo.State.DISCONNECTED){
            //TODO stop syncing. To be implemented.
            Toast.makeText(context, "wifi disconnected", Toast.LENGTH_SHORT).show();
            Log.e("PriPic", "Wifi disconnected.");
        }


    }

}
