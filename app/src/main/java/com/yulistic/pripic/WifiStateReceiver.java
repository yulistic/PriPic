package com.yulistic.pripic;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;


public class WifiStateReceiver extends BroadcastReceiver {

    private static boolean wifiConnected = false;
    public WifiStateReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager conMngr = (ConnectivityManager)context.getSystemService(context.CONNECTIVITY_SERVICE);
        android.net.NetworkInfo wifi = conMngr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo.State currentState = wifi.getState();
        if (!wifiConnected && currentState == NetworkInfo.State.CONNECTED){
            //TODO start syncing.
            Toast.makeText(context, "wifi connected", Toast.LENGTH_SHORT).show();
            Log.e("PriPic", "Wifi has been connected.");
        }
        else if (wifiConnected && currentState == NetworkInfo.State.DISCONNECTED){
            //TODO stop syncing.
            Toast.makeText(context, "wifi disconnected", Toast.LENGTH_SHORT).show();
            Log.e("PriPic", "Wifi disconnected.");
        }


    }

}
