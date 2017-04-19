package at.htlgkr.raiffeisenprojektteam.schuldenapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by yg on 15/11/16.
 */

public class DebtsDbHelper extends SQLiteOpenHelper
{
    private java.lang.String TAG = "*=";
    public static final String DATABASE_NAME="Debts.db";
    public static final int DATABASE_VERSION=3;

    public DebtsDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        //Log.d(TAG, "dbHelperConstructor");
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        Log.d(TAG, "dbHelperOnCreate");
        //db.execSQL(TblStatus.SQL_CREATE_TABEL);

        db.execSQL(TblDebts.SQL_CREATE_TABLE);

        //seed(db);
    }

    private void seed(SQLiteDatabase db)
    {
        //db.execSQL("INSERT INTO statuses VALUES ('paid'); INSERT INTO statuses VALUES ('open'); INSERT INTO statuses VALUES('not_paid');");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL(TblDebts.SQL_DROP_TABLE);
        db.execSQL(TblDebts.SQL_CREATE_TABLE);
    }
}
