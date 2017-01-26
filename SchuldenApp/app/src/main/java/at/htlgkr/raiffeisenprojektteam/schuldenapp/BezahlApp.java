package at.htlgkr.raiffeisenprojektteam.schuldenapp;

import android.app.Activity;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by yg on 21.12.16.
 */

public class BezahlApp extends AppCompatActivity {
    //Auf Button Bezahlen
    private ListView lv;
    private ArrayAdapter arrayAdapter=new ArrayAdapter(this,android.R.layout.simple_list_item_1);
    private ArrayList<String> list=new ArrayList<>();
    TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bezahl_layout);

        lv= (ListView) findViewById(R.id.list_view_bezahl);
        lv.setAdapter(arrayAdapter);
        tv= (TextView) findViewById(R.id.textPreisBezahlen);
        Bundle extras = getIntent().getExtras();
        String data="";

        if (extras != null) {
            data = extras.getString("BezahlApp");
            addToListView(data);
        }

        readPrices();//liest alle preise in der Liste und summiert sie

    }
    private void addToListView(String data){
        String[]split=data.split(";");
        arrayAdapter.add(split[0]+" "+split[1]+","+split[4]);
        arrayAdapter.setNotifyOnChange(true);
        list.add(split[0]+" "+split[1]+","+split[4]);
    }

    private void readPrices(){
        int sum=0;
        int num=0;
        for(String s : list){
            String [] splitted=s.split(",");
            num= Integer.parseInt(splitted[1]);
            sum=sum+num;
        }
        tv.setText(""+sum);

    }
}
