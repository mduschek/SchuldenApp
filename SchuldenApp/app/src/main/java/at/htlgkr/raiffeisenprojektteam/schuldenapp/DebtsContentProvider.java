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
    public static final String PROVIDER_NAME ="at.htlgkr.raiffeisenprojektteam.schuldenapp.DebtsContentProvider";
    public static final String DEBT_URL = "content://" + PROVIDER_NAME + "/debts";  // + TblDebts.TABLE_NAME; --> Geht so nicht - uri matcher geht nicht
    public static final Uri DEBT_URI =Uri.parse(DEBT_URL);

    public class Debts{
        public static final String DEBT_ID = TblDebts.ID;
        public static final String I_AM_CREDITOR = TblDebts.I_AM_CREDITOR;
        public static final String FIRSTNAME = TblDebts.FIRSTNAME;
        public static final String LASTNAME = TblDebts.LASTNAME;
        public static final String USAGE = TblDebts.USAGE;
        public static final String IBAN = TblDebts.IBAN;
        public static final String STATUS = TblDebts.STATUS;
        public static final String VALUE = TblDebts.VALUE;
        public static final String DATE = TblDebts.DATE;

    }

    private static final int DEBTS =1;
    private static final int DEBT_ID=2;

    static final UriMatcher uriMatcher=new UriMatcher(UriMatcher.NO_MATCH);

    SQLiteDatabase db;

    @Override
    public boolean onCreate() {
        uriMatcher.addURI(PROVIDER_NAME, "debts", DEBTS);
        uriMatcher.addURI(PROVIDER_NAME, "debts/#", DEBT_ID);

        db =new DebtsDbHelper(getContext()).getWritableDatabase();
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor cursor=null;

        switch (uriMatcher.match(uri)){

            case DEBTS:
                cursor=db.query(TblDebts.TABLE_NAME,projection,selection,selectionArgs,null,null,sortOrder);
                break;
            case DEBT_ID:
                if(selection != null) {
                    throw new UnsupportedOperationException("No Arguments plz");
                }
                selection = TblDebts.ID + " = " + uri.getPathSegments().get(1);
                cursor = db.query(TblDebts.TABLE_NAME,projection,selection,selectionArgs,null,null,sortOrder);
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
            case DEBTS:
                db.insert(TblDebts.TABLE_NAME,null,contentValues);
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

            case DEBTS:
                count = db.delete(TblDebts.TABLE_NAME, s, strings);
                break;
            case DEBT_ID:
                db.delete(TblDebts.TABLE_NAME, TblDebts.ID + " = " + uri.getPathSegments().get(1), null);
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
            case DEBTS:
                count = db.update(TblDebts.TABLE_NAME, contentValues, s, strings);
                break;
            case DEBT_ID:
                count = db.update(TblDebts.TABLE_NAME, contentValues, TblDebts.ID + " = " + uri.getPathSegments().get(1) +
                        (!TextUtils.isEmpty(s) ? " AND (" +s + ')' : ""), strings);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);

        }
        return count;
    }
}
