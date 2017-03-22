package at.htlgkr.raiffeisenprojektteam.schuldenapp;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;

/**
 * Created by yg on 15/11/16.
 */

public class DebtsContentProvider extends ContentProvider {

    private static final String AUTH="at.htlgkr.raiffeisenprojektteam.schuldenapp.DebtsContentProvider";
    public static final Uri URI_mydebts=Uri.parse("content//:"+AUTH+"/"+TblMyDebts.TABLE_NAME);
    public static final Uri URI_whoowesme=Uri.parse("content//:"+AUTH+"/"+TblWhoOwesMe.TABLE_NAME);
    private static final int CLIENTS_VERZ=1;
    private static final int CLIENT_ID=2;
    private static final int STATUSES_VERZ=3;
    private static final int STATUS_ID=4;
    private static final int MYDEBTS_VERZ =5;
    private static final int MYDEPT_ID=6;
    private static final int WHOOWESME_VERZ=7;
    private static final int WHOOWESME_ID=8;
    private static final UriMatcher uriMatcher=new UriMatcher(UriMatcher.NO_MATCH);
    static{
        uriMatcher.addURI(AUTH,TblStatus.TABLE_NAME,STATUSES_VERZ);
        uriMatcher.addURI(AUTH,TblStatus.TABLE_NAME+"/#",STATUS_ID);
        uriMatcher.addURI(AUTH, TblMyDebts.TABLE_NAME, MYDEBTS_VERZ);
        uriMatcher.addURI(AUTH, TblMyDebts.TABLE_NAME+"/#",MYDEPT_ID);
        uriMatcher.addURI(AUTH,TblWhoOwesMe.TABLE_NAME,WHOOWESME_VERZ);
        uriMatcher.addURI(AUTH,TblWhoOwesMe.TABLE_NAME+"/#",WHOOWESME_ID);
    }


    private SQLiteDatabase dbHelper;

    @Override
    public boolean onCreate() {
        dbHelper=new DebtsDbHelper(getContext()).getReadableDatabase();
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor cursor=null;

        switch (uriMatcher.match(uri)){

           /* case STATUSES_VERZ:
                cursor=dbHelper.query(TblStatus.TABLE_NAME,projection,selection,selectionArgs,null,null,sortOrder);
                break;
            case STATUS_ID:
                if(selection != null) {
                    throw new UnsupportedOperationException("No Arguments plz");
                }
                selection = "_id = " + uri.getPathSegments().get(1);
                cursor = dbHelper.query(TblStatus.TABLE_NAME,projection,selection,selectionArgs,null,null,sortOrder);
                break;*/
            case MYDEBTS_VERZ:
                cursor=dbHelper.query(TblMyDebts.TABLE_NAME,projection,selection,selectionArgs,null,null,sortOrder);
                break;
            case MYDEPT_ID:
                if(selection != null) {
                    throw new UnsupportedOperationException("No Arguments plz");
                }
                selection = "_id = " + uri.getPathSegments().get(1);
                cursor = dbHelper.query(TblMyDebts.TABLE_NAME,projection,selection,selectionArgs,null,null,sortOrder);
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
        switch (uriMatcher.match(uri))
        {
           /* case STATUSES_VERZ:
                dbHelper.insert(TblStatus.TABLE_NAME,null,contentValues);
                break;*/
            case MYDEBTS_VERZ:
                dbHelper.insert(TblMyDebts.TABLE_NAME,null,contentValues);
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
        int count = 0;
        String id;
        switch (uriMatcher.match(uri)){

           /* case STATUSES_VERZ:
                count = dbHelper.delete(TblStatus.TABLE_NAME, s, strings);
                break;
            case STATUS_ID:
                String id = uri.getPathSegments().get(1);
                count = dbHelper.delete( TblStatus.TABLE_NAME, TblStatus.ID +  " = " + id +
                        (!TextUtils.isEmpty(s) ? " AND (" + s + ')' : ""), strings);
                break;*/
            case MYDEBTS_VERZ:
                count = dbHelper.delete(TblMyDebts.TABLE_NAME, s, strings);
                break;
            case MYDEPT_ID:
                id = uri.getPathSegments().get(1);
                count = dbHelper.delete( TblMyDebts.TABLE_NAME, TblMyDebts.ID +  " = " + id +
                        (!TextUtils.isEmpty(s) ? " AND (" + s + ')' : ""), strings);
                break;
            case WHOOWESME_VERZ:
                count = dbHelper.delete(TblWhoOwesMe.TABLE_NAME, s, strings);
                break;
            case WHOOWESME_ID:
                id = uri.getPathSegments().get(1);
                count = dbHelper.delete( TblWhoOwesMe.TABLE_NAME, TblWhoOwesMe.ID +  " = " + id +
                        (!TextUtils.isEmpty(s) ? " AND (" + s + ')' : ""), strings);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);

        }

        return count;

    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        int count=0;
        switch (uriMatcher.match(uri)){
/*
            case STATUSES_VERZ:

                break;
            case STATUS_ID:

                break;*/
            case MYDEBTS_VERZ:
                count = dbHelper.update(TblMyDebts.TABLE_NAME, contentValues, s, strings);
                break;
            case MYDEPT_ID:
                count = dbHelper.update(TblMyDebts.TABLE_NAME, contentValues, TblMyDebts.ID + " = " + uri.getPathSegments().get(1) +
                        (!TextUtils.isEmpty(s) ? " AND (" +s + ')' : ""), strings);
                break;
            case WHOOWESME_VERZ:
                count = dbHelper.update(TblWhoOwesMe.TABLE_NAME, contentValues, s, strings);
                break;
            case WHOOWESME_ID:
                count = dbHelper.update(TblWhoOwesMe.TABLE_NAME, contentValues, TblWhoOwesMe.ID + " = " + uri.getPathSegments().get(1) +
                        (!TextUtils.isEmpty(s) ? " AND (" +s + ')' : ""), strings);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);

        }
        return count;

    }
}
