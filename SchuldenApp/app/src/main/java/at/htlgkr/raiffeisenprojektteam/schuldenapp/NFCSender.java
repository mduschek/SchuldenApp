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

    private String date, firstname, lastname, iban, usage, value, bic;
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
        bic = i.getStringExtra("bic");
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
            MainActivity.db.execSQL("UPDATE "+TblDebts.TABLE_NAME + " SET "+TblDebts.STATUS+" = 'not_paid' WHERE "+TblDebts.ID+" = "+updateId+";");
            /*
            if (partnerIsCreditor)
            {
                MainActivity.db.execSQL("UPDATE "+TblDebts.TABLE_NAME + " SET "+TblDebts.STATUS+" = 'not_paid' WHERE "+TblDebts.ID+" = "+updateId+";");
            }
            else
            {
                MainActivity.db.execSQL("UPDATE "+TblDebts.TABLE_NAME + " SET "+TblDebts.STATUS+" = 'not_paid' WHERE "+TblDebts.ID+" = "+updateId+";");

            }*///
        }
        return ndefMessage;
    }

    private void insert(String status) {
        inserted = true;
        Log.d(TAG, inserted+"");
        if (!partnerIsCreditor) {
            ContentValues cv = new ContentValues();
            cv.put(TblDebts.DATE, date);
            cv.put(TblDebts.FIRSTNAME, DBData.firstname);
            cv.put(TblDebts.IBAN, DBData.iban);
            cv.put(TblDebts.USAGE, DBData.usuage);
            cv.put(TblDebts.LASTNAME, DBData.lastname);
            cv.put(TblDebts.VALUE, DBData.value);
            cv.put(TblDebts.STATUS, status);
            cv.put(TblDebts.I_AM_CREDITOR,true);
            MainActivity.db.insert(TblDebts.TABLE_NAME, null, cv);
        } else {
            Log.w(TAG, status+ " Status" +
                    DBData.lastname+ " "+ DBData.iban+ " " + DBData.value+ " " +date);
            ContentValues cv = new ContentValues();
            cv.put(TblDebts.DATE, date);
            cv.put(TblDebts.FIRSTNAME, DBData.firstname);
            cv.put(TblDebts.IBAN, DBData.iban);
            cv.put(TblDebts.USAGE, DBData.usuage);
            cv.put(TblDebts.LASTNAME, DBData.lastname);
            cv.put(TblDebts.VALUE, DBData.value);
            cv.put(TblDebts.STATUS, status);
            cv.put(TblDebts.I_AM_CREDITOR,false);
            MainActivity.db.insert(TblDebts.TABLE_NAME, null, cv);
        }
    }
}
