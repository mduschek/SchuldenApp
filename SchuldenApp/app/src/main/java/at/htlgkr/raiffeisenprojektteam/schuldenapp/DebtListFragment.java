package at.htlgkr.raiffeisenprojektteam.schuldenapp;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by Alexander on 09.11.16.
 */

public class DebtListFragment extends Fragment
{
    private View view;
    private ListView listView;
    private ArrayList<Debt> listItems = new ArrayList<>();
    private ArrayAdapter<Debt> adapter;
    private boolean showMyDepts;
    private int clickedIndex;
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout resource that'll be returned
        view = inflater.inflate(R.layout.fragment_debt_list, container, false);
        listView = (ListView) view.findViewById(R.id.debtListView);

        // Get the arguments that was supplied when
        // the fragment was instantiated in the
        // CustomPagerAdapter
        Bundle args = getArguments();
        showMyDepts = args.getBoolean("showMyDepts");

        //Database is going to be queried
        //Struktur  public Debt(int deptType, String deptorFirstName, String deptorLastName, String usuage, String iBan, String status, double value)
        if (args.getBoolean("showMyDepts")==true)
        {
            Cursor c = MainActivity.db.rawQuery("SELECT * FROM myDepts;",null);
            Log.e("*===", "onCreateView: " +c.hashCode() );
            while (c.moveToNext())
            {
                int id = c.getInt(c.getColumnIndex(TblMyDebts.ID));
                String firstname = c.getString(c.getColumnIndex(TblMyDebts.PERS_I_OWE_FIRSTNAME));
                String lastname = c.getString(c.getColumnIndex(TblMyDebts.PERS_I_OWE_LASTNAME));
                double value = c.getDouble(c.getColumnIndex(TblMyDebts.PERS_I_OWE_VALUE));
                String usuage = c.getString(c.getColumnIndex(TblMyDebts.PERS_I_OWE_USUAGE));
                String iban = c.getString(c.getColumnIndex(TblMyDebts.PERS_I_OWE_IBAN));
                String status = c.getString(c.getColumnIndex(TblMyDebts.STATUS));
                String date = c.getString(c.getColumnIndex(TblMyDebts.PERS_I_OWE_DATE));
                Log.d("*==", firstname);
                listItems.add(new Debt(id,false,firstname,lastname,usuage,iban,status,value, date));
            }
        }
        else
        {
            Cursor c = MainActivity.db.rawQuery("SELECT * FROM WhoOwesMe;",null);
            while (c.moveToNext())
            {
                int id = c.getInt(c.getColumnIndex(TblWhoOwesMe.ID));
                String firstname = c.getString(c.getColumnIndex(TblWhoOwesMe.PERS_WHO_OWES_ME_FIRSTNAME));
                String lastname = c.getString(c.getColumnIndex(TblWhoOwesMe.PERS_WHO_OWES_ME_LASTNAME));
                double value = c.getDouble(c.getColumnIndex(TblWhoOwesMe.PERS_WHO_OWES_ME_VALUE));
                String usuage = c.getString(c.getColumnIndex(TblWhoOwesMe.PERS_WHO_OWES_ME_USUAGE));
                String iban = c.getString(c.getColumnIndex(TblWhoOwesMe.PERS_WHO_OWES_ME_IBAN));
                String status = c.getString(c.getColumnIndex(TblWhoOwesMe.STATUS));
                String date = c.getString(c.getColumnIndex(TblWhoOwesMe.PERS_WHO_OWES_ME_DATE));
                Log.d("*==", firstname);
                listItems.add(new Debt(id,true,firstname,lastname,usuage,iban,status,value, date));
            }
        }

        adapter = new ArrayAdapter<Debt>(getActivity(),android.R.layout.simple_list_item_1,listItems);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                clickedIndex = i;
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra("object", adapter.getItem(i));
                startActivity(intent);
            }
        });

        return view;
    }
}
