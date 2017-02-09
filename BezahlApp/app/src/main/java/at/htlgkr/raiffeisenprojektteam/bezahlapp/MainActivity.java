package at.htlgkr.raiffeisenprojektteam.bezahlapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
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

    Button buttonTransaction, buttonScanQr;
    TextView textViewCredit, textViewValue, textViewUsage, textViewIban, textViewFirstname, textViewLastname, textViewDate;
    SharedPreferences sharedPreferences;
    //ArrayAdapter adap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        textViewCredit= (TextView) findViewById(R.id.textViewCredit);
        textViewValue= (TextView) findViewById(R.id.textViewValue);
        textViewUsage= (TextView) findViewById(R.id.textViewUsage);
        textViewCredit= (TextView) findViewById(R.id.textViewCredit);
        textViewIban= (TextView) findViewById(R.id.textViewIban);
        textViewFirstname= (TextView) findViewById(R.id.textViewFirstname);
        textViewLastname= (TextView) findViewById(R.id.textViewLastname);
        textViewDate= (TextView) findViewById(R.id.textViewDate);

        buttonTransaction= (Button) findViewById(R.id.buttonTransaction);
        buttonScanQr= (Button) findViewById(R.id.buttonScanQr);



        //adap=new ArrayAdapter(this,android.R.layout.simple_expandable_list_item_1);
        //listView.setAdapter(adap);

        Intent intent = getIntent();
        String action = intent.getAction();
        Uri data = intent.getData();

    }

    @Override
    protected void onResume() {
        super.onResume();
        textViewCredit.setText(sharedPreferences.getString("pref_userdata_credit", null));
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

    public void buttonTransactionClicked(){

    }

    public void buttonScanQrClicked(){

    }
}
