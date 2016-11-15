package at.htlgkr.raiffeisenprojektteam.schuldenapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by yg on 15/11/16.
 */

public class DeptsDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME="Depts.db";
    public static final int DATABASE_VERSION=1;

    public DeptsDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TblClients.SQL_CREATE_TABEL);
        db.execSQL(TblStatus.SQL_CREATE_TABEL);
        db.execSQL(TblMyDepts.SQL_CREATE_TABLE);
        db.execSQL(TblWhoOwesMe.SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL(TblClients.SQL_DROP_TABLE);
        db.execSQL(TblStatus.SQL_DROP_TABLE);
        db.execSQL(TblMyDepts.SQL_DROP_TABLE);
        db.execSQL(TblWhoOwesMe.SQL_DROP_TABLE);

        db.execSQL(TblClients.SQL_CREATE_TABEL);
        db.execSQL(TblStatus.SQL_CREATE_TABEL);
        db.execSQL(TblMyDepts.SQL_CREATE_TABLE);
        db.execSQL(TblWhoOwesMe.SQL_CREATE_TABLE);
    }
}
