package com.yulistic.pripic;

import android.app.Service;
import android.content.Intent;
import android.os.*;
import android.os.Process;
import android.widget.Toast;

/**
 * A Service that synchronizes photos in PhotoContentProvider with user's server.
 * It uses a working thread for synchronizing. Also, it dismisses progressDialog in MainActivity
 * when the sync job finishes.
 */
public class SyncService extends Service {
    private ServiceHandler mServiceHandler;

    private final class ServiceHandler extends Handler {
        public ServiceHandler(Looper looper){
            super(looper);
        }

        @Override
        public void handleMessage(Message msg){
            //sync with server.
            //Fake sync work. Wait 5 sec.
            long endTime = System.currentTimeMillis() + 5*1000;
            while (System.currentTimeMillis() < endTime) {
                synchronized (this) {
                    try {
                        wait(endTime - System.currentTimeMillis());
                    } catch (Exception e) {
                        // Interrupted Exception. Do nothing.
                    }
                }
            }

            // Destroy progress dialog.
            MainActivity.progressDialog.dismiss();

            // Stop SyncService.
            stopSelf(msg.arg1);
        }
    }

    public SyncService() {
    }

    @Override
    public void onCreate(){
        // A new thread that will do sync job.
        HandlerThread thread = new HandlerThread("SyncStartArguments", Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();

        Looper mServiceLooper = thread.getLooper();
        mServiceHandler = new ServiceHandler (mServiceLooper);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, getString(R.string.syncservice_start), Toast.LENGTH_SHORT).show();

        // Send message to the handler.
        Message msg = mServiceHandler.obtainMessage();
        msg.arg1 = startId;
        mServiceHandler.sendMessage(msg);

        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy(){
        Toast.makeText(this, getString(R.string.syncservice_done), Toast.LENGTH_SHORT).show();
    }
}
