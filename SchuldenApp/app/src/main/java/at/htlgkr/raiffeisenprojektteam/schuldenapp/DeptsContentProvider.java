package at.htlgkr.raiffeisenprojektteam.schuldenapp;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Created by yg on 15/11/16.
 */

public class DeptsContentProvider extends ContentProvider {

    private static final String AUTH="at.htlgkr.raiffeisenprojektteam.schuldenapp.DeptsContentProvider";
    //public static final Uri URI=Uri.parse("content//:"+AUTH+"/"+TblClients);
    private static final int CLIENTS_VERZ=1;
    private static final int CLIENT_ID=2;
    private static final int STATUSES_VERZ=3;
    private static final int STATUS_ID=4;
    private static final int MYDEPTS_VERZ=5;
    private static final int MYDEPT_ID=6;
    private static final int WHOOWESME_VERZ=7;
    private static final int WHOOWESME_ID=8;
    private static final UriMatcher uriMatcher=new UriMatcher(UriMatcher.NO_MATCH);
    static{
        uriMatcher.addURI(AUTH,TblClients.TABLE_NAME,CLIENTS_VERZ);
        uriMatcher.addURI(AUTH,TblClients.TABLE_NAME+"/#",CLIENT_ID);
        uriMatcher.addURI(AUTH,TblStatus.TABLE_NAME,STATUSES_VERZ);
        uriMatcher.addURI(AUTH,TblStatus.TABLE_NAME+"/#",STATUS_ID);
        uriMatcher.addURI(AUTH,TblMyDepts.TABLE_NAME,MYDEPTS_VERZ);
        uriMatcher.addURI(AUTH,TblMyDepts.TABLE_NAME+"/#",MYDEPT_ID);
        uriMatcher.addURI(AUTH,TblWhoOwesMe.TABLE_NAME,WHOOWESME_VERZ);
        uriMatcher.addURI(AUTH,TblWhoOwesMe.TABLE_NAME+"/#",WHOOWESME_ID);
    }


    private SQLiteDatabase dbHelper;

    @Override
    public boolean onCreate() {
        dbHelper=new DeptsDbHelper(getContext()).getReadableDatabase();
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor cursor=null;

        switch (uriMatcher.match(uri)){
            case CLIENTS_VERZ:
                cursor=dbHelper.query(TblClients.TABLE_NAME,projection,selection,selectionArgs,null,null,sortOrder);
                break;
            case CLIENT_ID:
                if(selection != null) {
                    throw new UnsupportedOperationException("No Arguments plz");
                }
                selection = "_id = " + uri.getPathSegments().get(1);
                cursor = dbHelper.query(TblClients.TABLE_NAME,projection,selection,selectionArgs,null,null,sortOrder);
                break;
            case STATUSES_VERZ:
                cursor=dbHelper.query(TblStatus.TABLE_NAME,projection,selection,selectionArgs,null,null,sortOrder);
                break;
            case STATUS_ID:
                if(selection != null) {
                    throw new UnsupportedOperationException("No Arguments plz");
                }
                selection = "_id = " + uri.getPathSegments().get(1);
                cursor = dbHelper.query(TblStatus.TABLE_NAME,projection,selection,selectionArgs,null,null,sortOrder);
                break;
            case MYDEPTS_VERZ:
                cursor=dbHelper.query(TblMyDepts.TABLE_NAME,projection,selection,selectionArgs,null,null,sortOrder);
                break;
            case MYDEPT_ID:
                if(selection != null) {
                    throw new UnsupportedOperationException("No Arguments plz");
                }
                selection = "_id = " + uri.getPathSegments().get(1);
                cursor = dbHelper.query(TblMyDepts.TABLE_NAME,projection,selection,selectionArgs,null,null,sortOrder);
                break;
            case WHOOWESME_VERZ:
                cursor=dbHelper.query(TblWhoOwesMe.TABLE_NAME,projection,selection,selectionArgs,null,null,sortOrder);
                break;
            case WHOOWESME_ID:
                if(selection != null) {
                    throw new UnsupportedOperationException("No Arguments plz");
                }
                selection = "_id = " + uri.getPathSegments().get(1);
                cursor = dbHelper.query(TblWhoOwesMe.TABLE_NAME,projection,selection,selectionArgs,null,null,sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);

        }

        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        switch (uriMatcher.match(uri)){
            case CLIENTS_VERZ:
                dbHelper.insert(TblClients.TABLE_NAME,null,contentValues);
                break;
            case STATUSES_VERZ:
                dbHelper.insert(TblStatus.TABLE_NAME,null,contentValues);
                break;
            case MYDEPTS_VERZ:
                dbHelper.insert(TblMyDepts.TABLE_NAME,null,contentValues);
                break;
            case WHOOWESME_VERZ:
                dbHelper.insert(TblWhoOwesMe.TABLE_NAME,null,contentValues);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI for EntryContentProvider: " + uri);
        }
        return null;
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
    }
}
