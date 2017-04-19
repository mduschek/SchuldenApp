package at.htlgkr.raiffeisenprojektteam.schuldenapp;

import android.content.ContentProvider;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by michael on 08.02.17.
 */

public class ArchiveActivity extends AppCompatActivity {

    ListView listView;
    private ArrayList<Debt> debts;
    //private ArrayAdapter<Debt> adapter;
    private DebtsArchiveAdapter adapter;
    private int clickedIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_archive);
        listView = (ListView) findViewById(R.id.archiveListView);
        debts = new ArrayList<Debt>();

        loadDbEntries();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getMenuInflater().inflate(R.menu.context_menu_archive_list, menu);
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.context_menu_archive_list_delete:
                //SQL für DELETE
        }
        return super.onContextItemSelected(item);
    }

    private void loadDbEntries() {
        //Cursor c = MainActivity.db.rawQuery("SELECT * FROM  "+TblDebts.TABLE_NAME+ ";", null);
        //Cursor c = MainActivity.db.rawQuery("SELECT * FROM "+TblDebts.TABLE_NAME+" WHERE "+TblDebts.STATUS+" = 'paid';",null);
        //Cursor c = getContentResolver().query(DebtsContentProvider.DEBT_URI, null, null, null, DebtsContentProvider.Debts.DATE);
        Cursor c = getContentResolver().query(
                DebtsContentProvider.DEBT_URI,
                null,
                DebtsContentProvider.Debts.STATUS + " = ?",
                new String[]{"paid"},
                DebtsContentProvider.Debts.DATE + " DESC");

        c.moveToFirst();

        while (c.moveToNext()) {
            boolean iAmCreditor = false;
            if (c.getInt(c.getColumnIndex(TblDebts.I_AM_CREDITOR)) == 1) iAmCreditor = true;

            Debt d = new Debt(
                    c.getInt(c.getColumnIndex(TblDebts.ID)),
                    iAmCreditor,
                    c.getString(c.getColumnIndex(TblDebts.FIRSTNAME)),
                    c.getString(c.getColumnIndex(TblDebts.LASTNAME)),
                    c.getString(c.getColumnIndex(TblDebts.USAGE)),
                    c.getString(c.getColumnIndex(TblDebts.IBAN)),
                    c.getString(c.getColumnIndex(TblDebts.STATUS)),
                    c.getDouble(c.getColumnIndex(TblDebts.VALUE)),
                    c.getString(c.getColumnIndex(TblDebts.DATE))
            );
            debts.add(d);
        }
        c.close();

        adapter = new DebtsArchiveAdapter(this, debts);
        //adapter = new ArrayAdapter<Debt>(this, android.R.layout.simple_list_item_1, debts);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                clickedIndex = i;
                Intent intent = new Intent(ArchiveActivity.this, DetailActivity.class);
                intent.putExtra("object", adapter.getItem(i));
                startActivity(intent);
            }
        });
    }
}
