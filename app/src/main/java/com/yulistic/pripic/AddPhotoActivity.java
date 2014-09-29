package com.yulistic.pripic;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;


public class AddPhotoActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_photo);

        Intent incomingIntent = getIntent();
        String incomingAction = incomingIntent.getAction();

        // Check whether the incoming action is SEND or SEND_MULTIPLE
        if (incomingAction.equals(Intent.ACTION_SEND)){
            Uri imageUri = (Uri) incomingIntent.getParcelableExtra(Intent.EXTRA_STREAM);
            if (imageUri != null) {
                //TODO make it private.
                ContentValues values = new ContentValues();
                values.put(PhotoContentProvider.COLUMN_ORIGINAL_PATH, imageUri.getPath());
                Uri uri = getContentResolver().insert(PhotoContentProvider.CONTENT_URI, values);
                Log.d(getString(R.string.app_name), uri.toString() + "inserted.");
            }
            Toast.makeText(this, "Selected photo is saved in Pripic.", Toast.LENGTH_SHORT).show();
        }else if (incomingAction.equals(Intent.ACTION_SEND_MULTIPLE)){
            ArrayList<Uri> imageUris = incomingIntent.getParcelableArrayListExtra(Intent.EXTRA_STREAM);
            if (imageUris != null){
                //TODO make them private.
            }
            Toast.makeText(this, "Selected photo is saved in Pripic.", Toast.LENGTH_SHORT).show();
        }else {
            Log.e(getString(R.string.app_name), "Invalid action from incoming Intent");
        }
        finish();
    }
}
