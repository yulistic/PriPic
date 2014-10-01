package com.yulistic.pripic;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
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

                // Get Thumbnail.
                //TODO The parameter of getThumbnail: origid should be checked.
                Bitmap thumbnailBitmap = MediaStore.Images.Thumbnails.getThumbnail(getContentResolver(),
                        Integer.parseInt(imageUri.getLastPathSegment()), MediaStore.Images.Thumbnails.MINI_KIND, (BitmapFactory.Options)null);
                ByteArrayOutputStream blob = new ByteArrayOutputStream();
                thumbnailBitmap.compress(Bitmap.CompressFormat.PNG, 0, blob);
                byte[] bitmapData = blob.toByteArray();
                values.put(PhotoContentProvider.COLUMN_THUMBNAIL, bitmapData);

                Uri uri = getContentResolver().insert(PhotoContentProvider.CONTENT_URI, values);
                Log.d(getString(R.string.app_name), uri.toString() + " inserted.");
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
