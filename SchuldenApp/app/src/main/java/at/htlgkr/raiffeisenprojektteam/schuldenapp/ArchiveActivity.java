package at.htlgkr.raiffeisenprojektteam.schuldenapp;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by michael on 08.02.17.
 */

public class ArchiveActivity extends AppCompatActivity {

    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_archive);
        listView = (ListView) findViewById(R.id.archiveListView);
        ArrayList<Debt> debts = new ArrayList<Debt>();
        Cursor c = MainActivity.db.rawQuery("SELECT * FROM "+TblDebts.TABLE_NAME+" WHERE "+TblDebts.STATUS+" = 'paid';",null);
    }
}
