package at.htlgkr.raiffeisenprojektteam.schuldenapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Alexander on 09.11.16.
 */

public class DeptsFragment extends Fragment
{
    private View view;
    private ListView listView;
    private ArrayList<String> listItems = new ArrayList<String>();
    private ArrayAdapter<String> adapter;
    private boolean showMyDepts;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout resource that'll be returned
        view = inflater.inflate(R.layout.fragment_debt_list, container, false);
        listView = (ListView) view.findViewById(R.id.debtListView);

        // Get the arguments that was supplied when
        // the fragment was instantiated in the
        // CustomPagerAdapter
        Bundle args = getArguments();
        showMyDepts = args.getBoolean("showMyDepts");

        //Database is going to be queried
        if (args.getBoolean("showMyDepts")==true)
        {
            for(int i=0;i<20;i++) listItems.add("Ich schulde");
        }
        else
        {
            for(int i=0;i<20;i++)listItems.add("Mir schuldet");
        }

        adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,listItems);
        listView.setAdapter(adapter);
        return view;
    }
}
