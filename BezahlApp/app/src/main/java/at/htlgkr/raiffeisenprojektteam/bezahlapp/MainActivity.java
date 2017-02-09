package at.htlgkr.raiffeisenprojektteam.bezahlapp;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class MainActivity extends AppCompatActivity {

    Button buttonTransaction;
    TextView textViewCredit, textViewValue, textViewUsage, textViewIban, textViewFirstname, textViewLastname, textViewDate;
    SharedPreferences sharedPreferences;

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
                IntentIntegrator integrator=new IntentIntegrator(this);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                integrator.setPrompt("Scan");
                integrator.setCameraId(0);
                integrator.setBeepEnabled(false);
                integrator.setBarcodeImageEnabled(false);
                integrator.initiateScan();
            return true;

            case R.id.option_menu_preferences:
                intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result=IntentIntegrator.parseActivityResult(requestCode,resultCode,data);

        if(result==null) {
            super.onActivityResult(requestCode, resultCode, data);
        }
        else{
            if (result.getContents()==null){
                Toast.makeText(this,"Cancelled",Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(this,result.getContents(),Toast.LENGTH_LONG).show();
                Transaction transaction = StringToTransactionConverter(result.getContents());
            }
        }
    }

    public Transaction StringToTransactionConverter(String transactionString) {

        final String splitArr[] = transactionString.split(";");

        Transaction transaction = new Transaction(splitArr[0], splitArr[1], splitArr[2], splitArr[3], splitArr[4], Double.parseDouble(splitArr[5]));
        return transaction;
    }

    public void buttonTransactionClicked(){

    }
}
