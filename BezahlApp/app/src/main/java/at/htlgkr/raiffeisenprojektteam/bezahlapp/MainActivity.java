package at.htlgkr.raiffeisenprojektteam.bezahlapp;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    Button button;
    //ListView listView;
    TextView textView;
    ArrayAdapter adap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button= (Button) findViewById(R.id.button);
        //listView= (ListView) findViewById(R.id.list_view);
        textView= (TextView) findViewById(R.id.text);
        //adap=new ArrayAdapter(this,android.R.layout.simple_expandable_list_item_1);
        //listView.setAdapter(adap);

        Intent intent = getIntent();
        String action = intent.getAction();
        Uri data = intent.getData();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option_menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Intent intent;
        switch (id) {
            case R.id.option_menu_qr_scanner:
//                IntentIntegrator integrator=new IntentIntegrator(this);
//                integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
//                integrator.setPrompt("Scan");
//                integrator.setCameraId(0);
//                integrator.setBeepEnabled(false);
//                integrator.setBarcodeImageEnabled(false);
//                integrator.initiateScan();
//                return true;

            case R.id.option_menu_preferences:
                intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
