package at.htlgkr.raiffeisenprojektteam.schuldenapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

public class   DeptListFragment extends Fragment
{
    private View view;
    private ListView listView;
    private ArrayList<Dept> listItems = new ArrayList<>();
    private ArrayAdapter<Dept> adapter;
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
        if (args.getBoolean("showMyDepts")==true)
        {
            for(int i=0;i<20;i++) listItems.add(new Dept(Dept.OWN_DEPT,"Michael", "Duschek", "Essen", "AT 98 34442 8899 8908", i));
        }
        else
        {
            for(int i=0;i<20;i++)listItems.add(new Dept(Dept.SBDY_OWES_ME_DEPT,"Flo", "Huemer", "Proteine", "AT 98 34442 8899 1233", i));
        }

        adapter = new ArrayAdapter<Dept>(getActivity(),android.R.layout.simple_list_item_1,listItems);
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
