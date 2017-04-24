package at.htlgkr.raiffeisenprojektteam.bezahlapp;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

/**
 * Created by qw on 23.03.17.
 */

public class LoadDebtsActivity extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.to_load_layout);
        ContentResolver contentResolver=getContentResolver();
        final Uri debtsUri=Uri.parse("content://at.htlgkr.raiffeisenprojektteam.schuldenapp.DebtsContentProvider/debts");
        Cursor cursor=contentResolver.query(debtsUri,new String[]{"pers_i_owe_iban","pers_i_owe_date","person_i_owe_value"},null,null,null);
        Toast.makeText(this,"!!!!!!!"+cursor.getColumnCount()+"!!!!!!!",Toast.LENGTH_LONG).show();
    }

}
