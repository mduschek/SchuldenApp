package at.htlgkr.raiffeisenprojektteam.bezahlapp;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    Button button;
    ListView listView;
    TextView textView;
    ArrayAdapter adap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button= (Button) findViewById(R.id.button);
        listView= (ListView) findViewById(R.id.list_view);
        textView= (TextView) findViewById(R.id.text);
        adap=new ArrayAdapter(this,android.R.layout.simple_expandable_list_item_1);
        listView.setAdapter(adap);

        Intent intent = getIntent();
        String action = intent.getAction();
        Uri data = intent.getData();

    }
}
