package at.htlgkr.raiffeisenprojektteam.schuldenapp;

import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcEvent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.nfc.NfcAdapter;
import android.util.Log;

/**
 * Created by Perndorfer on 11.02.2017.
 */

public class NFCSender extends AppCompatActivity implements NfcAdapter.CreateNdefMessageCallback {

    String date,firstname,lastname,iban,usuage,value;
    boolean partnerIsCreditor;
    private static final String TAG = "*=NFCSender";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nfcsender);
        NfcAdapter adapter = NfcAdapter.getDefaultAdapter(this);
        Intent i = getIntent();

        date = i.getStringExtra("date");
        firstname = i.getStringExtra("firstname");
        lastname = i.getStringExtra("lastname");
        usuage = i.getStringExtra("usuage");
        iban = i.getStringExtra("iban");
        value = i.getStringExtra("value");
        partnerIsCreditor = i.getBooleanExtra("partneriscreditor",false);

        Log.w(TAG, firstname+lastname+usuage+iban+value+partnerIsCreditor);

        adapter.setNdefPushMessageCallback(this,this);

    }

    @Override
    public NdefMessage createNdefMessage(NfcEvent event) {
        String stringOut = partnerIsCreditor + ";" + firstname + ";" + lastname + ";" + usuage + ";" + iban + ";" + value + ";" + date;
        Log.d(TAG, "createNdefMessage: ");
        NdefRecord ndefRecord = NdefRecord.createMime("text/plain", stringOut.getBytes());
        NdefMessage ndefMessage = new NdefMessage(ndefRecord);
        return ndefMessage;
    }
}
