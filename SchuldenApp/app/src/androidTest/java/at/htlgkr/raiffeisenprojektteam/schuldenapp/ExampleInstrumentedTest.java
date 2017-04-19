package at.htlgkr.raiffeisenprojektteam.schuldenapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

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
        cv.put(TblDebts.VALUE,666.6);
        cv.put(TblDebts.LASTNAME,"Test");
        cv.put(TblDebts.FIRSTNAME,"Unit");
        cv.put(TblDebts.DATE, new Date().toString());
        cv.put(TblDebts.IBAN,"UnitTestIban");
        cv.put(TblDebts.USAGE,"Testverwendung");
        cv.put(TblDebts.STATUS, "not_paid");
        appContext.getContentResolver().insert(DebtsContentProvider.DEBT_URI,cv);
        Cursor c =  MainActivity.db.rawQuery("SELECT * FROM "+TblDebts.TABLE_NAME+" ORDER BY "+TblDebts.ID+";",null);
        c.moveToLast();


        assertEquals(666.6,c.getDouble(c.getColumnIndex(TblDebts.VALUE)));
    }
}
