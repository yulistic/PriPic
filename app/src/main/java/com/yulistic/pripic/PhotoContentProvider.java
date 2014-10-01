package com.yulistic.pripic;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.os.CancellationSignal;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

public class PhotoContentProvider extends ContentProvider {

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    public static String AUTH_URI = "com.yulistic.pripic.provider.photo";
    static final Uri CONTENT_URI = Uri.parse("content://" + AUTH_URI + "/photos");
    private static final int PHOTO = 1;
    private static final int PHOTO_ID = 2;

    private static HashMap<String, String> photoMap;


    static{
        sUriMatcher.addURI(AUTH_URI, "photos", PHOTO);
        sUriMatcher.addURI(AUTH_URI, "photos/#", PHOTO_ID);
    }

    private PhotoDatabaseHelper mOpenHelper;

    private static final String DBNAME = "photodb";
    private static final String TABLE_NAME = "photos";
    private static final int DATABASE_VERSION = 3;
    private SQLiteDatabase db;


    public PhotoContentProvider() {
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new PhotoDatabaseHelper(getContext());
        db = mOpenHelper.getWritableDatabase();

        if (db == null)
            return false;
        else
            return true;
    }

    @Override
    public String getType(Uri uri) {
        switch (sUriMatcher.match(uri)){
            case PHOTO:
                break;
            case PHOTO_ID:
                break;
        }
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // Insert to the DB.
        long newItemId = db.insert(TABLE_NAME, null, values);
        if (newItemId > 0){
            Uri newItemUri = ContentUris.withAppendedId(CONTENT_URI,newItemId);
            getContext().getContentResolver().notifyChange(newItemUri, null);
            return newItemUri;
        }
        throw new SQLException("Fail to add new photos into " + uri);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(TABLE_NAME);

        int match = sUriMatcher.match(uri);
        switch (match){
            case PHOTO:
                //TODO: To be checked!
                queryBuilder.setProjectionMap(photoMap);
                break;
            case PHOTO_ID:
                queryBuilder.appendWhere(COLUMN_ID +"="+uri.getLastPathSegment());
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        if (sortOrder == null || sortOrder == ""){
            sortOrder = COLUMN_ID;
        }
        Cursor cursor = queryBuilder.query(db, projection, selection, selectionArgs,
                null, null, sortOrder);
        return cursor;
    }


    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /* Handle requests to open a file blob. */
    @Override
    public ParcelFileDescriptor openFile(Uri uri, String mode, CancellationSignal signal) throws FileNotFoundException {
        //File directory = getContext().getDir(Environment.getDataDirectory(), Context.MODE_PRIVATE);
        File path = new File(Environment.getDataDirectory(), uri.getLastPathSegment());

        int iMode = 0;
        if (mode.contains("w")){
            iMode |= ParcelFileDescriptor.MODE_WRITE_ONLY;
            if (!path.exists()){
                try{
                    path.createNewFile();
                }catch (IOException e){
                    e.printStackTrace();
                    Log.e(getContext().getString(R.string.app_name),
                            "openFile Fail! cannot create New file for the path "+ path.getPath());
                }
            }
        }
        if (mode.contains("r")){
            iMode |= ParcelFileDescriptor.MODE_READ_ONLY;
        }

        return ParcelFileDescriptor.open(path, iMode);
    }


    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_ORIGINAL_PATH = "original_path";
    public static final String COLUMN_PRIV_PATH = "priv_path";
    public static final String COLUMN_THUMBNAIL = "thumbnail";


    private static final String SQL_CREATE_PHOTOS = "CREATE TABLE " +
            TABLE_NAME +                       // Table's name
            " (" +                           // The columns in the table
            " "+ COLUMN_ID +" INTEGER PRIMARY KEY, " +
            " " + COLUMN_ORIGINAL_PATH + " TEXT," +
            " " + COLUMN_PRIV_PATH + " TEXT," +
            " " + COLUMN_THUMBNAIL + " BLOB" +
            ")";

    protected static final class PhotoDatabaseHelper extends SQLiteOpenHelper {

        /*
         * Instantiates an open helper for the provider's SQLite data repository
         * Do not do database creation and upgrade here.
         */
        PhotoDatabaseHelper(Context context) {
            super(context, DBNAME, null, DATABASE_VERSION);
        }

        /*
         * Creates the data repository. This is called when the provider attempts to open the
         * repository and SQLite reports that it doesn't exist.
         */
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(SQL_CREATE_PHOTOS);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
            Log.w(PhotoDatabaseHelper.class.getName(), "Upgrading database from version " +
                    oldVersion + " to " + newVersion + ".");
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(sqLiteDatabase);
        }
    }
}
