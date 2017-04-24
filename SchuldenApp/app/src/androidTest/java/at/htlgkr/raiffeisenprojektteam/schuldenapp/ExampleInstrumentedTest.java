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

import dalvik.annotation.TestTargetClass;

import static org.junit.Assert.*;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

    SQLiteDatabase db;
    Context appContext = InstrumentationRegistry.getTargetContext();

    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();
        db = new DebtsDbHelper(appContext).getWritableDatabase();
        assertEquals("at.htlgkr.raiffeisenprojektteam.schuldenapp", appContext.getPackageName());
        ContentValues cv = new ContentValues();
        cv.put(TblDebts.I_AM_CREDITOR, true);
        cv.put(TblDebts.VALUE, 666);
        cv.put(TblDebts.LASTNAME, "Test");
        cv.put(TblDebts.FIRSTNAME, "Unit");
        cv.put(TblDebts.DATE, new Date().toString());
        cv.put(TblDebts.IBAN, "UnitTestIban");
        cv.put(TblDebts.USAGE, "Testverwendung");
        cv.put(TblDebts.STATUS, "open");
        appContext.getContentResolver().insert(DebtsContentProvider.DEBT_URI, cv);
        Cursor c = db.rawQuery("SELECT * FROM " + TblDebts.TABLE_NAME + " ORDER BY " + TblDebts.ID + ";", null);
        c.moveToLast();

        Log.w("*=TESTEN=*", c.getInt(c.getColumnIndex(TblDebts.VALUE)) + "");
        assertEquals(666, c.getInt(c.getColumnIndex(TblDebts.VALUE)));

    }

    @Test
    public void updateTest() throws Exception {
        Cursor c;
        ContentValues cv;

        db = new DebtsDbHelper(appContext).getWritableDatabase();
        c = db.rawQuery("SELECT * FROM " + TblDebts.TABLE_NAME + " ORDER BY " + TblDebts.ID + ";", null);
        c.moveToLast();
        int id = c.getInt(c.getColumnIndex(TblDebts.ID));
        c.close();
        cv = new ContentValues();
        cv.put(TblDebts.STATUS, "not_paid");
        appContext.getContentResolver().update(DebtsContentProvider.DEBT_URI, cv, TblDebts.ID + " = " + id, null);
        c = db.rawQuery("SELECT * FROM " + TblDebts.TABLE_NAME + " ORDER BY " + TblDebts.ID + ";", null);
        c.moveToLast();
        String status = c.getString(c.getColumnIndex(TblDebts.STATUS));
        assertEquals("not_paid", status);
    }

    @Test
    public void queryTest () throws Exception
    {
        Cursor c = appContext.getContentResolver().query(DebtsContentProvider.DEBT_URI,null,null,null,null);

        boolean b = false;
        if(c.getCount()>0) b=true;

        assertTrue(b);
    }


}
