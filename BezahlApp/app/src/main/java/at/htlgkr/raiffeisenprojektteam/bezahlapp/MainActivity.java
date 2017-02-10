package at.htlgkr.raiffeisenprojektteam.bezahlapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class MainActivity extends AppCompatActivity {

    Button buttonTransaction;
    TextView textViewCredit, textViewValue, textViewUsage, textViewIban, textViewFirstname, textViewLastname, textViewDate;
    SharedPreferences sharedPreferences;

    Transaction transaction = null;

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
        updateViews();
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
                //Toast.makeText(this,result.getContents(),Toast.LENGTH_LONG).show();
                stringToTransactionConverter(result.getContents());
                updateViews();
            }
        }
    }

    public void stringToTransactionConverter(String transactionString) {

        // String example = iban;Philip;Frauscher;Drogen;9.2.2017;500

        final String splitArr[] = transactionString.split(";");
        transaction = new Transaction(splitArr[0], splitArr[1], splitArr[2], splitArr[3], splitArr[4], Double.parseDouble(splitArr[5]));
    }

    public void updateViews(){
        textViewCredit.setText(sharedPreferences.getString("pref_userdata_credit", null));

        if (transaction != null){
            textViewIban.setText(transaction.getIban());
            textViewFirstname.setText(transaction.getPartnerFirstname());
            textViewLastname.setText(transaction.getPartnerLastname());
            textViewUsage.setText(transaction.getUsage());
            textViewDate.setText(transaction.getDate());
            textViewValue.setText(transaction.getValue() + "");
        }

        else{
            textViewIban.setText("");
            textViewFirstname.setText("");
            textViewLastname.setText("");
            textViewUsage.setText("");
            textViewDate.setText("");
            textViewValue.setText("");
        }
    }

    public void buttonTransactionClicked(View view){
        if (transaction == null) return;

        double newVal = (Double.parseDouble(sharedPreferences.getString("pref_userdata_credit", null)) - transaction.getValue());
        //Toast.makeText(this,"Value " + newVal,Toast.LENGTH_LONG).show();
        sharedPreferences.edit().putString("pref_userdata_credit", newVal+"").apply();
        transaction = null;
        updateViews();
    }
}
