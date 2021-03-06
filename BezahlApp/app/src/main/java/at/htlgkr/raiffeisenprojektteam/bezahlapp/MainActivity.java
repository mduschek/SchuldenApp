package at.htlgkr.raiffeisenprojektteam.bezahlapp;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    private static final String TRANSACTION_KEY = "transactionKey";

    Button buttonTransaction;
    TextView textViewCredit, textViewBic, textViewCreditor, textViewIban, textViewAmount, textViewReason, textViewReference, textViewText, textViewMessage;
    SharedPreferences sharedPreferences;
    private static final String TAG = "*=MainActivity";
    private Transaction transaction;
    public static final int PICK_DEBT_REQUEST = 1;
    //private Debt selectedDebt;
    private final Uri debtsUri = Uri.parse("content://at.htlgkr.raiffeisenprojektteam.schuldenapp.DebtsContentProvider/debts");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {
            if (savedInstanceState.getSerializable(TRANSACTION_KEY) != null) {
                transaction = (Transaction) savedInstanceState.getSerializable(TRANSACTION_KEY);
                Log.e(TAG, "onCreate: " + transaction.getCreditor());
            }
        }


        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        textViewCredit = (TextView) findViewById(R.id.textViewCredit);
        textViewBic = (TextView) findViewById(R.id.textViewBic);
        textViewCreditor = (TextView) findViewById(R.id.textViewCreditor);
        textViewIban = (TextView) findViewById(R.id.textViewIban);
        textViewAmount = (TextView) findViewById(R.id.textViewAmount);
        textViewReason = (TextView) findViewById(R.id.textViewReason);
        textViewReference = (TextView) findViewById(R.id.textViewReference);
        textViewText = (TextView) findViewById(R.id.textViewText);
        textViewMessage = (TextView) findViewById(R.id.textViewMessage);
        buttonTransaction = (Button) findViewById(R.id.buttonTransaction);

        //Intent intent = getIntent();
        //String action = intent.getAction();
        //Uri data = intent.getData();

        if (getIntent().getExtras() != null) {
            String data1 = getIntent().getExtras().getString("BezahlApp");
            //Log.d("debug", data1);
            stuzzaStringToTransactionConverter(data1);
            updateViews();
            //Toast.makeText(this,"Data: " + data1,Toast.LENGTH_LONG).show();
        }
        updateViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
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
                IntentIntegrator integrator = new IntentIntegrator(this);
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

        if (requestCode == 1 && data != null) {
            if (resultCode == AppCompatActivity.RESULT_OK) {
                transaction = (Transaction) data.getSerializableExtra("result");
                updateViews();
            }
            if (resultCode == AppCompatActivity.RESULT_CANCELED) {
                Toast.makeText(this, "Kein Eintrag ausgewählt.", Toast.LENGTH_SHORT).show();
            }
        } else {
            IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            if (result == null) {
                super.onActivityResult(requestCode, resultCode, data);
            } else {
                if (result.getContents() == null) {
                    Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, "result.getContents()", Toast.LENGTH_LONG).show();
                    //stringToTransactionConverter(result.getContents());
                    stuzzaStringToTransactionConverter(result.getContents());
                    updateViews();
                }
            }
        }
    }

    public void stuzzaStringToTransactionConverter(String transactionString) {
        try {
            Log.d("Stuzza", transactionString.trim());
            final String splitArr1[] = transactionString.trim().split(System.lineSeparator());
            String splitArr[] = new String[12];

            for (int i = 0; i < splitArr1.length; i++) {
                splitArr[i] = splitArr1[i];
            }

            transaction = new Transaction(
                    splitArr[4],
                    splitArr[5],
                    splitArr[6],
                    Float.parseFloat(splitArr[7].substring(3)),
                    splitArr[8],
                    splitArr[9],
                    splitArr[10],
                    splitArr[11]);

        } catch (Exception e) {
            //Toast.makeText(this, "Fehler stuzzaStringToTransactionConverter", Toast.LENGTH_LONG).show();
            Log.e("Error", e.toString());
        }
    }

    @Deprecated
    public String transactionToStuzzaStringConverter() {
        return "BCD1\n" +
                "001\n" +
                "1\n" +
                "SCT\n" +
                transaction.getBic() + "\n" +
                transaction.getCreditor() + "\n" +
                transaction.getIban() + "\n" +
                transaction.getAmount() + "\n" +
                transaction.getReason() + "\n" +
                transaction.getReference() + "\n" +
                transaction.getText() + "\n" +
                transaction.getMessage();
    }

    public void updateViews() {
        textViewCredit.setText(sharedPreferences.getString("pref_userdata_credit", null));

        if (transaction != null) {

            textViewBic.setText(transaction.getBic());
            textViewCreditor.setText(transaction.getCreditor());
            textViewIban.setText(transaction.getIban());
            textViewAmount.setText(new DecimalFormat("0.00").format(transaction.getAmount()));
            textViewReason.setText(transaction.getReason());
            textViewReference.setText(transaction.getReference());
            textViewText.setText(transaction.getText());
            textViewMessage.setText(transaction.getMessage());
        } else {
            textViewBic.setText("");
            textViewCreditor.setText("");
            textViewIban.setText("");
            textViewAmount.setText("");
            textViewReason.setText("");
            textViewReference.setText("");
            textViewText.setText("");
            textViewMessage.setText("");
        }
    }

    public void buttonLoadTransactionClicked(View view) {
//        Intent intent = new Intent();
        ContentResolver contentResolver = getContentResolver();
        Cursor cursor = contentResolver.query(debtsUri, null, null, null, null);
        //Log.d(TAG, "buttonLoadTransactionClicked: " + cursor.getCount());
        startActivityForResult(new Intent(this, LoadDebtsActivity.class), PICK_DEBT_REQUEST);
        updateViews();
    }

    public void buttonDeleteTransactionClicked(View view) {
        transaction = null;
        updateViews();
    }

    public void buttonTransactionClicked(View view) {
        if (transaction == null || transaction.getAmount() <= 0) {
            Toast.makeText(this, "Ungültige Transaktion", Toast.LENGTH_LONG).show();
            return;
        }

        double newVal = (Double.parseDouble(sharedPreferences.getString("pref_userdata_credit", null)) - transaction.getAmount());

        if (newVal < 0) {
            Toast.makeText(this, "Zu wenig Guthaben vorhanden!", Toast.LENGTH_LONG).show();
            //Geld wird nicht wieder gutgeschrieben da es nur eine Simulation ist
            return;
        }

        sharedPreferences.edit().putString("pref_userdata_credit", newVal + "").apply();
        ContentValues cv = new ContentValues();
        cv.put(Debt.STATUS, "paid");
        String selection = "_id = " + transaction.getID() + " OR (" + Debt.USAGE + " = '" + transaction.getMessage() + "' AND " + Debt.I_AM_CREDITOR + " = 0 AND " + Debt.FIRSTNAME + " = '" + transaction.getCreditor().split(" ")[0] + "' AND " + Debt.LASTNAME + " = '" + transaction.getCreditor().split(" ")[1] + "')";
        int rows = getContentResolver().update(debtsUri, cv, selection, null);
        if (rows > 1) Log.w(TAG, "buttonTransactionClicked: too many rows updated");
        transaction = null;
        Toast.makeText(this, "Transaktion erfolgreich!", Toast.LENGTH_LONG).show();
        updateViews();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (transaction != null) {
            outState.putSerializable(TRANSACTION_KEY, transaction);
        } else {
            //outState.putString(TRANSACTION_KEY, null);
        }
        super.onSaveInstanceState(outState);
    }
}
