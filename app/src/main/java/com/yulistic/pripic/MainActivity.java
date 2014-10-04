package com.yulistic.pripic;

import android.app.Activity;
import android.app.Fragment;
import android.app.LoaderManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ResourceCursorAdapter;


/**
 * MainActivity of the application (PriPic). It is composed of a Fragment that contains a GridView.
 * In the GridView, photos that are saved in this application are listed. You can sync photos in your
 * mobile device with your server by clicking Sync Button. When you click menu key, you can go into
 * SettingsActivity.
 */
public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        // The case when Settings option is clicked. SettingsActivity will be started
        // as the option is clicked.
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public static ProgressDialog progressDialog;

    public void onSyncButtonClick(View view) {
        // Create progress dialog.
        progressDialog = ProgressDialog.show(this, getString(R.string.progress_title), getString(R.string.progress_msg), true);

        // Start SyncService.
        Intent intent = new Intent (this, SyncService.class);
        startService(intent);
    }


    /**
     * A placeholder fragment containing a <code>GridView</code> that display photo list.
     */
    public static class PlaceholderFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
        //Loader id.
        private static final int PHOTO_LOADER = 0;
        ImageAdapter photoAdapter;

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {

            /* CursorLoader initialization.*/
            getLoaderManager().initLoader(PHOTO_LOADER, null, this);

            return inflater.inflate(R.layout.fragment_photo_list, container, false);
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState){
            super.onActivityCreated(savedInstanceState);

            // Link photoAdapter to gridview.
            GridView mGridView = (GridView) getActivity().findViewById(R.id.fragment_photo_list_grid);
            photoAdapter = new ImageAdapter(getActivity(), R.layout.gridview_item, null, 0);    //No FLAG_REGISTER_CONTENT_OBSERVER.
            mGridView.setAdapter(photoAdapter);
        }

        @Override
        public void onCreate(Bundle savedInstanceState){
            super.onCreate(savedInstanceState);
        }

        /* LoadManager related implements. */
        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
            switch(id){
                case PHOTO_LOADER:
                    return new CursorLoader(getActivity(),
                            PhotoContentProvider.CONTENT_URI,
                            null,
                            null,
                            null,
                            null);
                default:
                    return null;
            }
        }

        @Override
        public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
            photoAdapter.changeCursor(cursor);
        }

        @Override
        public void onLoaderReset(Loader<Cursor> cursorLoader) {
            photoAdapter.changeCursor(null);
        }
    }

    /**
     * ImageAdapter which deals with ImageView containing a photo thumbnail.
     */
    public static class ImageAdapter extends ResourceCursorAdapter {
        public ImageAdapter(Context context, int layout, Cursor c, int flags) {
            super(context, layout, c, flags);
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            ImageView iv = (ImageView) view.findViewById(R.id.id_gridview_item_imageview);
            int photoColumnIndex = cursor.getColumnIndexOrThrow(PhotoContentProvider.COLUMN_THUMBNAIL);
            byte[] thumbnailBytes = cursor.getBlob(photoColumnIndex);
            Bitmap mThumbnailBitmap = BitmapFactory.decodeByteArray(thumbnailBytes, 0, thumbnailBytes.length);
            iv.setImageBitmap(mThumbnailBitmap);
        }
    }
}
