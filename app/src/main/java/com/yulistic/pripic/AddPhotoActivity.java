package com.yulistic.pripic;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

/**
 * An Activity that saves selected photos to PhotoContentProvider that is a private ContentProvider
 * in this application.
 */
public class AddPhotoActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_photo);

        Intent incomingIntent;
        incomingIntent = getIntent();
        String incomingAction = incomingIntent.getAction();

        // Check whether the incoming action is SEND or SEND_MULTIPLE
        if (incomingAction.equals(Intent.ACTION_SEND)){
            Uri imageUri = incomingIntent.getParcelableExtra(Intent.EXTRA_STREAM);
            if (imageUri != null) {
                ContentValues values = new ContentValues();
                values.put(PhotoContentProvider.COLUMN_ORIGINAL_PATH, imageUri.getPath());

                // Get Thumbnail of the selected photo.
                Bitmap thumbnailBitmap = MediaStore.Images.Thumbnails.getThumbnail(getContentResolver(),
                        Integer.parseInt(imageUri.getLastPathSegment()), MediaStore.Images.Thumbnails.MINI_KIND, null);
                ByteArrayOutputStream blob = new ByteArrayOutputStream();
                thumbnailBitmap.compress(Bitmap.CompressFormat.PNG, 0, blob);
                byte[] bitmapData = blob.toByteArray();
                values.put(PhotoContentProvider.COLUMN_THUMBNAIL, bitmapData);

                // Save selected photo to PhotoContentProvider.
                Uri uri = getContentResolver().insert(PhotoContentProvider.CONTENT_URI, values);
                Log.d(getString(R.string.app_name), uri.toString() + " inserted.");
            }
            Toast.makeText(this, "Selected photo is saved in Pripic.", Toast.LENGTH_SHORT).show();

        }else if (incomingAction.equals(Intent.ACTION_SEND_MULTIPLE)){
            // When multiple photos are selected. It has not implemented yet.
            //Also, SEND_MULTIPLE action in intent filter is disabled (in manifest file).
            ArrayList<Uri> imageUris = incomingIntent.getParcelableArrayListExtra(Intent.EXTRA_STREAM);
            if (imageUris != null){
                //TODO To be implemented.
            }
            Toast.makeText(this, "Selected photos are saved in Pripic.", Toast.LENGTH_SHORT).show();
        }else {
            Log.e(getString(R.string.app_name), "Invalid action from incoming Intent");
        }

        //After saving selected photo, finish.
        finish();
    }
}
