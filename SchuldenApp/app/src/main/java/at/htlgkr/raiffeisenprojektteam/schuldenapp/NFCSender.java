package at.htlgkr.raiffeisenprojektteam.schuldenapp;

import android.content.ContentValues;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcEvent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.nfc.NfcAdapter;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Perndorfer on 11.02.2017.
 */

public class NFCSender extends AppCompatActivity implements NfcAdapter.CreateNdefMessageCallback {

    private String date, firstname, lastname, iban, usage, value;
    boolean partnerIsCreditor;
    private static final String TAG = "*=NFCSender";
    private ProgressBar progressBar;
    private TextView tv;
    private boolean inserted = false;
    private boolean isNewEntry;
    private int updateId=-1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nfcsender);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        tv = (TextView) findViewById(R.id.statusNfc);

        NfcAdapter adapter = NfcAdapter.getDefaultAdapter(this);
        Intent i = getIntent();

        date = i.getStringExtra("date");
        firstname = i.getStringExtra("firstname");
        lastname = i.getStringExtra("lastname");
        usage = i.getStringExtra("usage");
        iban = i.getStringExtra("iban");
        value = i.getStringExtra("value");
        isNewEntry = i.getBooleanExtra("isNewEntry",true);
        String picstr = i.getStringExtra("partneriscreditor");
        Log.e(TAG, "usage "+usage);
        if(picstr.equals("true")) partnerIsCreditor=true;
        else partnerIsCreditor = false;

        if(!isNewEntry) updateId = i.getIntExtra("updateId",-1);

        Toast.makeText(this,TAG+"isNewEntry "+isNewEntry+"ID:"+updateId,Toast.LENGTH_LONG).show();
        Log.w(TAG, firstname + lastname + usage + iban + value + partnerIsCreditor+ isNewEntry);

        adapter.setNdefPushMessageCallback(this, this);

    }

    @Override
    public NdefMessage createNdefMessage(NfcEvent event) {
        String stringOut = partnerIsCreditor + ";" + firstname + ";" + lastname + ";" + usage + ";" + iban + ";" + value + ";" + date;
        Log.d(TAG, "createNdefMessage: ");
        NdefRecord ndefRecord = NdefRecord.createMime("text/plain", stringOut.getBytes());
        NdefMessage ndefMessage = new NdefMessage(ndefRecord);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.GONE);
                findViewById(R.id.ivNFC).setVisibility(View.VISIBLE);
                tv.setText("Fertig");
            }
        });
        if (inserted == false&&isNewEntry) {
            insert("not_paid");
        }
        else
        {
            if (partnerIsCreditor)
            {
                MainActivity.db.execSQL("UPDATE "+TblMyDebts.TABLE_NAME + " SET "+TblMyDebts.STATUS+" = 'not_paid' WHERE "+TblMyDebts.ID+" = "+updateId+";");
            }
            else
            {
                MainActivity.db.execSQL("UPDATE "+TblWhoOwesMe.TABLE_NAME + " SET "+TblWhoOwesMe.STATUS+" = 'not_paid' WHERE "+TblWhoOwesMe.ID+" = "+updateId+";");

            }
        }
        return ndefMessage;
    }

    private void insert(String status) {
        inserted = true;
        Log.d(TAG, inserted+"");
        if (!partnerIsCreditor) {
            ContentValues cv = new ContentValues();
            cv.put(TblWhoOwesMe.PERS_WHO_OWES_ME_DATE, date);
            cv.put(TblWhoOwesMe.PERS_WHO_OWES_ME_FIRSTNAME, DBData.firstname);
            cv.put(TblWhoOwesMe.PERS_WHO_OWES_ME_IBAN, DBData.iban);
            cv.put(TblWhoOwesMe.PERS_WHO_OWES_ME_USUAGE, DBData.usuage);
            cv.put(TblWhoOwesMe.PERS_WHO_OWES_ME_LASTNAME, DBData.lastname);
            cv.put(TblWhoOwesMe.PERS_WHO_OWES_ME_VALUE, DBData.value);
            cv.put(TblWhoOwesMe.STATUS, status);
            MainActivity.db.insert(TblWhoOwesMe.TABLE_NAME, null, cv);
        } else {
            ContentValues cv = new ContentValues();
            cv.put(TblMyDebts.PERS_I_OWE_DATE, date);
            cv.put(TblMyDebts.PERS_I_OWE_FIRSTNAME, DBData.firstname);
            cv.put(TblMyDebts.PERS_I_OWE_IBAN, DBData.iban);
            cv.put(TblMyDebts.PERS_I_OWE_USUAGE, DBData.usuage);
            cv.put(TblMyDebts.PERS_I_OWE_LASTNAME, DBData.lastname);
            cv.put(TblMyDebts.PERS_I_OWE_VALUE, DBData.value);
            cv.put(TblMyDebts.STATUS, status);
            MainActivity.db.insert(TblMyDebts.TABLE_NAME, null, cv);
        }
    }
}
