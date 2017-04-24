package at.htlgkr.raiffeisenprojektteam.bezahlapp;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by qw on 23.03.17.
 */

public class LoadDebtsActivity extends Activity{
    ListView lv;
    ArrayList<Debt> debts = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.to_load_layout);
        ContentResolver contentResolver=getContentResolver();
        final Uri debtsUri=Uri.parse("content://at.htlgkr.raiffeisenprojektteam.schuldenapp.DebtsContentProvider/debts");
        Cursor cursor=contentResolver.query(debtsUri,new String[]{"pers_i_owe_iban","pers_i_owe_date","person_i_owe_value"},null,null,null);
        Toast.makeText(this,"!!!!!!!"+cursor.getColumnCount()+"!!!!!!!",Toast.LENGTH_LONG).show();
        lv = (ListView) findViewById(R.id.lstAllDebts);

        while(cursor.moveToNext())
        {
            int id = cursor.getInt(cursor.getColumnIndex(Debt.ID));
            String firstname = cursor.getString(cursor.getColumnIndex(Debt.FIRSTNAME));
            String lastname = cursor.getString(cursor.getColumnIndex(Debt.LASTNAME));
            String usage = cursor.getString(cursor.getColumnIndex(Debt.USAGE));
            String date = cursor.getString(cursor.getColumnIndex(Debt.DATE));
            String iban = cursor.getString(cursor.getColumnIndex(Debt.IBAN));
            String status = cursor.getString(cursor.getColumnIndex(Debt.STATUS));
            double value = cursor.getDouble(cursor.getColumnIndex(Debt.VALUE));
            boolean iAmCreditor = false;

            Debt d = new Debt(id,iAmCreditor,firstname,lastname,usage,iban,status,value,date);
        }
    }

}
