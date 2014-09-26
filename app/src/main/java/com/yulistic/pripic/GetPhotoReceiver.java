package com.yulistic.pripic;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class GetPhotoReceiver extends BroadcastReceiver {
    public GetPhotoReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving

        Log.d("PriPic", "Intent received!");

        // an Intent broadcast.
        throw new UnsupportedOperationException("Not yet implemented");

    }
}
