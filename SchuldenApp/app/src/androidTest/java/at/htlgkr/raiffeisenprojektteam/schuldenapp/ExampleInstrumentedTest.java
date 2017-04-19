package at.htlgkr.raiffeisenprojektteam.schuldenapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Date;

import static org.junit.Assert.*;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("at.htlgkr.raiffeisenprojektteam.schuldenapp", appContext.getPackageName());
        ContentValues cv = new ContentValues();
        cv.put(TblDebts.I_AM_CREDITOR,true);
        cv.put(TblDebts.VALUE,666);
        cv.put(TblDebts.LASTNAME,"Test");
        cv.put(TblDebts.FIRSTNAME,"Unit");
        cv.put(TblDebts.DATE, new Date().toString());
        cv.put(TblDebts.IBAN,"UnitTestIban");
        cv.put(TblDebts.USAGE,"Testverwendung");
        cv.put(TblDebts.STATUS, "not_paid");
        appContext.getContentResolver().insert(DebtsContentProvider.DEBT_URI,cv);
        SQLiteDatabase db = new DebtsDbHelper(appContext).getWritableDatabase();
        Cursor c =  db.rawQuery("SELECT * FROM "+TblDebts.TABLE_NAME+" ORDER BY "+TblDebts.ID+";",null);
        c.moveToLast();

        Log.w("*=TESTEN=*",c.getInt(c.getColumnIndex(TblDebts.VALUE))+"");
        assertEquals(666,c.getInt(c.getColumnIndex(TblDebts.VALUE)));
    }

    @Test
    public void update_dataBase(){
        Context appContext= InstrumentationRegistry.getContext();
        TblDebts tbltoinsert=new TblDebts();
        ContentValues cv=new ContentValues();
        cv.put(tbltoinsert.FIRSTNAME,"swaggy");
        cv.put(tbltoinsert.LASTNAME,"P.");
        cv.put(tbltoinsert.USAGE,"hookers and drugs");
        cv.put(tbltoinsert.IBAN,"666USA69LAL");
        cv.put(tbltoinsert.STATUS,"paid");
        cv.put(tbltoinsert.VALUE,"100000");
        cv.put(tbltoinsert.DATE,"4-7-1776");
        appContext.getContentResolver().insert(DebtsContentProvider.DEBT_URI,cv);

        ContentValues cv1=new ContentValues();
        cv1.put(tbltoinsert.STATUS,"notpaid");
        appContext.getContentResolver().update(DebtsContentProvider.DEBT_URI,cv1,"FIRSTNAME='swaggy' AND LASTNAME='P.'",null);
        SQLiteDatabase db = new DebtsDbHelper(appContext).getWritableDatabase();
        Cursor c =  db.rawQuery("SELECT * FROM "+TblDebts.TABLE_NAME+" WHERE STATUS=notpaid ORDER BY "+TblDebts.ID+";",null);
        c.moveToLast();

        assertEquals("notpaid",TblDebts.STATUS);

    }
}
