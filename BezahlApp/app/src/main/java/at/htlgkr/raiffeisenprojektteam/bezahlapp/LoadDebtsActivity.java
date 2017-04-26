package at.htlgkr.raiffeisenprojektteam.bezahlapp;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by qw on 23.03.17.
 */

public class LoadDebtsActivity extends AppCompatActivity{
    private ListView lv;
    private ArrayList<Transaction> transactions = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.to_load_layout);
        ContentResolver contentResolver=getContentResolver();
        final Uri debtsUri=Uri.parse("content://at.htlgkr.raiffeisenprojektteam.schuldenapp.DebtsContentProvider/debts");
        Cursor cursor=contentResolver.query(debtsUri,null,"("+Debt.STATUS +" = 'not_paid' OR status = 'open') AND "+Debt.I_AM_CREDITOR +" = "+0,null,Debt.ID);
        Toast.makeText(this,"Geladen",Toast.LENGTH_LONG).show();
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

            //Debt d = new Debt(id,iAmCreditor,firstname,lastname,usage,iban,status,value,date);
            Transaction transaction = new Transaction("",firstname+ " "+lastname,iban,(float)value,"","",usage,usage);
            transaction.setID(id);
            transactions.add(transaction);
        }
        cursor.close();
        ArrayAdapter<Transaction> adapter = new ArrayAdapter<Transaction>(this,android.R.layout.simple_list_item_1,transactions);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent returnIntent = new Intent();
                Log.d("*=", "onItemClick: "+i);
                returnIntent.putExtra("result",transactions.get(i));
                setResult(RESULT_OK,returnIntent);
                finish();
            }
        });
    }

}
