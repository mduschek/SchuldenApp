package at.htlgkr.raiffeisenprojektteam.schuldenapp;

import android.content.Context;
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
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Alexander on 21.11.16.
 */

public class CreateLoanActivity extends AppCompatActivity implements NfcAdapter.CreateNdefMessageCallback
{
    private Context context = this;
    TextView textViewCreateLoanDescription;
    Button buttonManualInput, buttonBluetooth, buttonNfc, buttonSms, buttonWhatsapp;
    public static final String LINK = "http://at.htlgkr.schuldenapp.createloan/schuldenapp";
    SharedPreferences sharedPreferences;
    //STRUKTUR: ?content=Michael;Duschek;Usuage;IBAN;30.65
    private static final String TAG = "CreateLoanActivity";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_create_loan);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        textViewCreateLoanDescription = (TextView) findViewById(R.id.textViewCreateLoanDescription);

        buttonManualInput = (Button) findViewById(R.id.buttonManualInput);
        buttonBluetooth = (Button) findViewById(R.id.buttonBluetooth);
        buttonNfc = (Button) findViewById(R.id.buttonNfc);
        buttonSms = (Button) findViewById(R.id.buttonSms);
        buttonWhatsapp= (Button) findViewById(R.id.buttonOther);
        if (!MainActivity.nfcIsAvailable) buttonNfc.setVisibility(View.GONE);
        MainActivity.nfcAdapter.setNdefPushMessageCallback(this,this);
    }

    public void onButtonPressed (View source){
        Intent intent;
        switch (source.getId()){
            case R.id.buttonManualInput:

                intent = new Intent(this,DetailActivity.class);

                //intent.putExtra("object", -1);
                startActivity(intent);
                break;
            case R.id.buttonBluetooth:

                intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT, "test msg");
                intent.setType("text/plain");

                startActivity(Intent.createChooser(intent, "Titel"));

                /*Uri bluetooth = Uri.parse("URL");  //URL parsen
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, bluetooth);
                sendIntent.setType("text/plain");
                startActivity((sendIntent, "Titel"));*/

                break;
            case R.id.buttonNfc:
                //an dieser Stelle NFC aktivieren
                //intent = new Intent(this,DetailActivity.class);
                //intent.putExtra("object", -1);
                //startActivity(intent);

                break;
            case R.id.buttonSms:
                //intent = new Intent(this,DetailActivity.class);
                //intent.putExtra("object", -1);
                //startActivity(intent);
                break;
            case R.id.buttonOther:


                Uri adress = Uri.parse("schuldenapp://createloan");  //URL parsen
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, LINK+"?content=Alexander;Perndorfer;Essen;AT34442566756567;30.65");
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
                Toast.makeText(context,stringOut,Toast.LENGTH_LONG).show();
            }
        });
        NdefRecord ndefRecord = NdefRecord.createMime("text/plain", stringOut.getBytes());
        NdefMessage ndefMessage = new NdefMessage(ndefRecord);
        return ndefMessage;
    }
}
