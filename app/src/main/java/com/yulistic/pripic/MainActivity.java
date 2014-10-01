package com.yulistic.pripic;

import android.app.Activity;
import android.app.Fragment;
import android.app.LoaderManager;
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
import android.widget.BaseAdapter;
import android.widget.CursorAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ResourceCursorAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import java.util.ResourceBundle;


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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onSyncButtonClick(View view) {
        //TODO Sync with server using multi thread.
        Toast.makeText(this, "sync now!!", Toast.LENGTH_SHORT).show();
    }


    /**
     * A placeholder fragment containing a simple view.
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


            View rootView = inflater.inflate(R.layout.fragment_photo_list, container, false);
            return rootView;

        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState){
            super.onActivityCreated(savedInstanceState);

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
                    String[] projection = null;
                    String selection = null;
                    String[] arguments = null;
                    String sortOrder = null;

                    return new CursorLoader(getActivity(),
                            PhotoContentProvider.CONTENT_URI,
                            projection,
                            selection,
                            arguments,
                            sortOrder);
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

    public static class ImageAdapter extends ResourceCursorAdapter {
        private Context mContext;

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



        /*public int getCount() {
            return mThumbIds.length;
        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return 0;
        }

        // create a new ImageView for each item referenced by the Adapter
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView;
            if (convertView == null) {  // if it's not recycled, initialize some attributes
                imageView = new ImageView(mContext);
                imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setPadding(8, 8, 8, 8);
            } else {
                imageView = (ImageView) convertView;
            }

            imageView.setImageResource(mThumbIds[position]);
            return imageView;
        }*/

        /*@Override
        public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
            return null;
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {

        }

        // references to our images
        private Integer[] mThumbIds = {
                R.drawable.ic_launcher,R.drawable.ic_launcher,R.drawable.ic_launcher,R.drawable.ic_launcher
        };*/
    }
}
