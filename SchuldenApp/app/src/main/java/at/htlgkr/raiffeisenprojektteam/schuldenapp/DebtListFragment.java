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
        boolean iAmCreditor = !showMyDepts;
        int iAmCreditorInt = 0;
        if (iAmCreditor) iAmCreditorInt = 1;
        if (!iAmCreditor) iAmCreditorInt = 0;

        Cursor c = MainActivity.db.rawQuery("SELECT * FROM Debts WHERE iAmCreditor = " + iAmCreditorInt + ";",null);
        Log.e("*===", "onCreateView: " +c.hashCode() );
        while (c.moveToNext())
        {
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
            listItems.add(d);
        }
        c.close();


        /*if (args.getBoolean("showMyDepts")==true)
        {
            Cursor c = MainActivity.db.rawQuery("SELECT * FROM myDepts;",null);
            Log.e("*===", "onCreateView: " +c.hashCode() );
            while (c.moveToNext())
            {
                Debt d = new Debt(
                        c.getInt(c.getColumnIndex(TblMyDebts.ID)),
                        false,
                        c.getString(c.getColumnIndex(TblMyDebts.PERS_I_OWE_FIRSTNAME)),
                        c.getString(c.getColumnIndex(TblMyDebts.PERS_I_OWE_LASTNAME)),
                        c.getString(c.getColumnIndex(TblMyDebts.PERS_I_OWE_USUAGE)),
                        c.getString(c.getColumnIndex(TblMyDebts.PERS_I_OWE_IBAN)),
                        c.getString(c.getColumnIndex(TblMyDebts.STATUS)),
                        c.getDouble(c.getColumnIndex(TblMyDebts.PERS_I_OWE_VALUE)),
                        c.getString(c.getColumnIndex(TblMyDebts.PERS_I_OWE_DATE))
                );
                listItems.add(d);
            }
        }
        else
        {
            Cursor c = MainActivity.db.rawQuery("SELECT * FROM WhoOwesMe;",null);
            while (c.moveToNext())
            {
                Debt d = new Debt(
                        c.getInt(c.getColumnIndex(TblWhoOwesMe.ID)),
                        true,
                        c.getString(c.getColumnIndex(TblWhoOwesMe.PERS_WHO_OWES_ME_FIRSTNAME)),
                        c.getString(c.getColumnIndex(TblWhoOwesMe.PERS_WHO_OWES_ME_LASTNAME)),
                        c.getString(c.getColumnIndex(TblWhoOwesMe.PERS_WHO_OWES_ME_USUAGE)),
                        c.getString(c.getColumnIndex(TblWhoOwesMe.PERS_WHO_OWES_ME_IBAN)),
                        c.getString(c.getColumnIndex(TblWhoOwesMe.STATUS)),
                        c.getDouble(c.getColumnIndex(TblWhoOwesMe.PERS_WHO_OWES_ME_VALUE)),
                        c.getString(c.getColumnIndex(TblWhoOwesMe.PERS_WHO_OWES_ME_DATE))
                );
                listItems.add(d);
            }
        }
        */

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
