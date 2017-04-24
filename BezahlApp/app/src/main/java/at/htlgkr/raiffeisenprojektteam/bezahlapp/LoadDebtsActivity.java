package at.htlgkr.raiffeisenprojektteam.bezahlapp;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by qw on 23.03.17.
 */

public class LoadDebtsActivity extends Activity{
    private ListView lv;
    private ArrayList<Debt> debts = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.to_load_layout);
        ContentResolver contentResolver=getContentResolver();
        final Uri debtsUri=Uri.parse("content://at.htlgkr.raiffeisenprojektteam.schuldenapp.DebtsContentProvider/debts");
        Cursor cursor=contentResolver.query(debtsUri,null,null,null,null);
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
            debts.add(d);
        }
        cursor.close();
        ArrayAdapter<Debt> adapter = new ArrayAdapter<Debt>(this,android.R.layout.simple_list_item_1,debts);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });
    }

}
