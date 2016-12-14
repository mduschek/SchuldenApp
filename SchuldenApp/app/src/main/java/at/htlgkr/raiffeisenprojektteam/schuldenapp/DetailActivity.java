package at.htlgkr.raiffeisenprojektteam.schuldenapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by michael on 24.11.16.
 */

public class DetailActivity extends AppCompatActivity implements NfcAdapter.CreateNdefMessageCallback {
    private TextView textViewCreateLoanDescription;
    private Button buttonManualInput, buttonBluetooth, buttonNfc, buttonOther;
    private EditText edSum, edUsuage, edIban, edFirstname, edLastname;
    public static final String LINK = "http://at.htlgkr.schuldenapp.createloan/schuldenapp";
    //STRUKTUR: ?content=Michael;Duschek;Usuage;IBAN;30.65
    private static final String TAG = "DetailActivity";
    private String nfcString="";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_detail_create);

        textViewCreateLoanDescription = (TextView) findViewById(R.id.textViewCreateLoanDescription);

        buttonBluetooth = (Button) findViewById(R.id.buttonBluetooth);
        buttonNfc = (Button) findViewById(R.id.buttonNfc);
        buttonOther = (Button) findViewById(R.id.buttonOther);

        edSum = (EditText) findViewById(R.id.editTextSum);
        edUsuage = (EditText) findViewById(R.id.editTextSum);
        edFirstname = (EditText) findViewById(R.id.editTextFirstName);



        //NFC
        if (!MainActivity.nfcIsAvailable) buttonNfc.setVisibility(View.GONE);
        MainActivity.nfcAdapter.setNdefPushMessageCallback(this, this);
    }

    public void onButtonPressed(View source) {
        Intent intent;
        switch (source.getId()) {
            case R.id.buttonManualInput:

                intent = new Intent(this, DetailActivity.class);

                //intent.putExtra("object", -1);
                startActivity(intent);
                break;
            case R.id.buttonBluetooth:


                break;
            case R.id.buttonNfc:

                if (MainActivity.nfcAdapter.isEnabled() == false) {

                    Toast.makeText(getApplicationContext(), "Bitte aktivieren Sie NFC und drücken Sie dann zurück, um hierher zurückzukehren!", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
                }


                break;
            case R.id.buttonOther:


                Uri adress = Uri.parse("schuldenapp://createloan");  //URL parsen
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, LINK + "?content=Alexander;Perndorfer;Essen;AT34442566756567;30.65");
                //sendIntent.putExtra(Intent.EXTRA_ORIGINATING_URI, adress); //geändert
                sendIntent.setType("text/plain");
                startActivity(Intent.createChooser(sendIntent, "App zum Senden auswählen"));

                /*Intent sendIntent = new Intent();
                sendIntent.setData (Uri.parse("schuldenapp://createloan"));
                startActivity(Intent.createChooser(sendIntent, "Titel"));*/

                break;
        }
    }

    @Override
    public NdefMessage createNdefMessage(NfcEvent nfcEvent) {
        final String stringOut = "Michael;Duschek;Usuage;IBAN;30.65";
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), stringOut, Toast.LENGTH_LONG).show();
            }
        });
        NdefRecord ndefRecord = NdefRecord.createMime("text/plain", stringOut.getBytes());
        NdefMessage ndefMessage = new NdefMessage(ndefRecord);
        return ndefMessage;
    }
}
