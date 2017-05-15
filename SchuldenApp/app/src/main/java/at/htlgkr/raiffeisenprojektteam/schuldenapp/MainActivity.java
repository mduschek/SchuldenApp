package at.htlgkr.raiffeisenprojektteam.schuldenapp;

import android.bluetooth.BluetoothA2dp;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private final DebtsDbHelper dbHelper = new DebtsDbHelper(this);
    public static SQLiteDatabase db;
    private String TAG = "*=";
    private ViewPager viewPager;
    private PagerTabStrip tabStrip;
    private CustomPagerAdapter customPagerAdapter;
    private ActionBar actionBar;
    private static boolean isInLandscape;
    public NfcAdapter nfcAdapter;
    public static BufferedReader br;
    public static BufferedWriter bw;

    public boolean i_am_creditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = dbHelper.getWritableDatabase();
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        UserData.firstname = sharedPreferences.getString("pref_userdata_firstname", null);
        UserData.lastname = sharedPreferences.getString("pref_userdata_lastname", null);
        UserData.iban = sharedPreferences.getString("pref_userdata_iban", null);
        UserData.bic = sharedPreferences.getString("pref_userdata_bic","");

        Log.d(TAG, UserData.getString());

        //region incoming intent from deeplinking
        Intent intent = getIntent();
        Log.e(TAG, "onCreate:");
        if (intent.getData() != null) {
            //STRUKTUR: ?content=depttype;Michael;Duschek;Usuage;IBAN;30.65;24.12.2016
            String params = intent.getData().getQueryParameter("content");
            params = URLDecoder.decode(params);
            Log.e(TAG, params);
            final String split[] = params.split(";");
            insertIntoDb(split);
        }

        //endregion

        //region pager
        actionBar = getSupportActionBar();
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        customPagerAdapter = new CustomPagerAdapter(getSupportFragmentManager(), this);
        viewPager.setAdapter(customPagerAdapter);
        viewPager.addOnPageChangeListener(getOnPageChangedListener());
        viewPager.setPageTransformer(true, new ZoomOutPageTransformer());

        ActionBar.TabListener tabListener = getTabListener(actionBar);
        // Specify that tabs should be displayed in the action bar.
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        // Create a tab listener that is called when the user changes tabs.

        switch (getResources().getConfiguration().orientation) {
            case Configuration.ORIENTATION_LANDSCAPE:
                isInLandscape = true;
                break;
            case Configuration.ORIENTATION_PORTRAIT:
                isInLandscape = false;
                break;
        }


        actionBar.addTab(actionBar.newTab().setText(getResources().getString(R.string.iOwe)).setTabListener(tabListener));
        actionBar.addTab(actionBar.newTab().setText(getResources().getString(R.string.owesMe)).setTabListener(tabListener));
        //actionBar.addTab(actionBar.newTab().setText(getResources().getString(R.string.details)).setTabListener(tabListener));
        // endregion
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
            case R.id.option_menu_new_entry:
                intent = new Intent(this, DetailActivity.class);
                //intent.putExtra("object", -1);
                startActivity(intent);
                return true;
            //case R.id.option_menu_userdata:
            //    return true;
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

            case R.id.option_menu_archive:
                intent = new Intent(this, ArchiveActivity.class);
                startActivity(intent);
                return true;

            case R.id.option_menu_qr_code_bank_details:
                intent = new Intent(this, QrGeneratorActivity.class);
                intent.setAction(Intent.ACTION_SEND);
                intent.putExtra("shareData", URLEncoder.encode(createBankDetailsString()));
                startActivity(intent);
                return true;

            case R.id.option_menu_open_payapp:
                try
                {

                    intent = this.getPackageManager().getLaunchIntentForPackage("at.htlgkr.raiffeisenprojektteam.bezahlapp");
                    startActivity(intent);
                }catch (Exception ex)
                {
                    Toast.makeText(this,"Die BezahlApp ist nicht installiert!",Toast.LENGTH_LONG).show();
                }

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private ActionBar.TabListener getTabListener(final ActionBar actionBar) {
        return new ActionBar.TabListener() {
            public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
                Log.w("*=", "" + actionBar.getSelectedNavigationIndex());
                changeTab(tab.getPosition());
            }

            public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
                // hide the given tab
            }

            public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
                // probably ignore this event
            }
        };
    }

    public void changeTab(int position) {
        viewPager.setCurrentItem(position);
    }

    public ViewPager.OnPageChangeListener getOnPageChangedListener() {
        //region OnPageChangedListener
        return new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                Log.d("*=", "onPageSelected: " + position);
                actionBar.setSelectedNavigationItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        };
        //endregion
    }


    //region NFC implementation


    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        String action = intent.getAction();
        if (action.equals(NfcAdapter.ACTION_NDEF_DISCOVERED)) {
            Parcelable[] parcelables = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            NdefMessage inNdefMessage = (NdefMessage) parcelables[0];
            NdefRecord[] inNdefRecords = inNdefMessage.getRecords();
            NdefRecord NdefRecord_0 = inNdefRecords[0];
            String inMsg = new String(NdefRecord_0.getPayload());
            Log.d(TAG, "onResume " + inMsg);
            Toast.makeText(this, inMsg, Toast.LENGTH_LONG).show();
            final String split[] = inMsg.split(";");
            insertIntoDb(split);
        }
        //Toast.makeText(this,"onResume",Toast.LENGTH_LONG).show();
        refresh();
    }

    //endregion
    private void refresh() {
        customPagerAdapter = new CustomPagerAdapter(getSupportFragmentManager(), this);
        viewPager.setAdapter(customPagerAdapter);
        viewPager.addOnPageChangeListener(getOnPageChangedListener());
        viewPager.setPageTransformer(true, new ZoomOutPageTransformer());
        viewPager.setCurrentItem(0);
        actionBar.setSelectedNavigationItem(0);
    }

    private void insertIntoDb(final String[] split) {
        //STRUKTUR: ?content=depttype;Michael;Duschek;Usuage;IBAN;30.65;12.12.16

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if (split[0].equals("true")) {
            builder.setMessage("Sind Sie sicher, dass Sie folgendes hinzufügen wollen?\r\nMir schuldet " + split[1] + " " + split[2] + " " + split[6] + "€ für " + split[3] + ", am " + split[7]);
            i_am_creditor = true;
        } else {
            builder.setMessage("Sind Sie sicher, dass Sie folgendes hinzufügen wollen?\r\nIch schulde " + split[1] + " " + split[2] + " " + split[6] + "€ für " + split[3] + ", am " + split[7]);
            i_am_creditor = false;
        }
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setPositiveButton("Ja", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ContentValues cv = new ContentValues();
                Toast.makeText(getApplicationContext(), "inputAccepted", Toast.LENGTH_LONG).show();
                cv.put(TblDebts.I_AM_CREDITOR, i_am_creditor);
                cv.put(TblDebts.FIRSTNAME, split[1]);
                cv.put(TblDebts.LASTNAME, split[2]);
                cv.put(TblDebts.USAGE, split[3]);
                cv.put(TblDebts.IBAN, split[4]);
                cv.put(TblDebts.BIC, split[5]);
                cv.put(TblDebts.STATUS, "not_paid");
                cv.put(TblDebts.VALUE, split[6]);
                cv.put(TblDebts.DATE, split[7]);
                db.insert(TblDebts.TABLE_NAME, null, cv);
                refresh();
            }
        });
        builder.setCancelable(false);
        builder.setNegativeButton("Nein", null);
        builder.create().show();
    }

    public static String createStuzzaString(String firstname, String lastname, String iban, String bic, float value, String usage) {
        //partnerIsCreditor + ";" + firstname + ";" + lastname + ";" + usage + ";" + iban + ";" + value + ";" + sdf.format(date));
        //BCD1 \r\n 001 \r\n 1 \r\n SCT \r\n BIC \r\n Creditor \r\n IBAN \r\n Value \r\n Reas \r\n Reference \r\n Text \r\n Message \r\n

        return "BCD" + System.lineSeparator() +
                "001" + System.lineSeparator() +
                "1" + System.lineSeparator() +
                "SCT" + System.lineSeparator() +
                bic + System.lineSeparator() + //Bic ...nicht vorhanden --> neuer als v2 != <März 2016
                firstname + " " + lastname + System.lineSeparator() + //Creditor
                iban + System.lineSeparator() +   //iban
                "EUR" + value + System.lineSeparator() +  //value
                "" + System.lineSeparator() +   //reason
                "" + System.lineSeparator() +   //REFERENCE OR TEXT
                usage + System.lineSeparator();// +     //text
        //"" + System.lineSeparator();   //message
    }

    public static String createBankDetailsString() {
        return ";" +
                UserData.firstname + ";" +
                UserData.lastname + ";" +
                ";" +
                UserData.iban + ";" +
                UserData.bic+
                ";";
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (result == null) {
            super.onActivityResult(requestCode, resultCode, data);
        } else {
            if (result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();


            } else {
                //Toast.makeText(this,result.getContents(),Toast.LENGTH_LONG).show();
                Intent intent = new Intent(this, DetailActivity.class);
                String string = URLDecoder.decode(result.getContents().toString());
                intent.putExtra("qr_code", data);
                String[] split = string.split(";");
                if (!split[0].equals("")) {
                    insertIntoDb(string.split(";"));
                } else {
                    //STRUKTUR: ?content=depttype;Michael;Duschek;Usuage;IBAN;30.65;24.12.2016
                    //public Debt(0 int id,1 boolean iAmCreditor, 2 String deptorFirstName,3 String deptorLastName,4 String usuage,5 String iBan,6 String bic,7 String status,8 double value,9 String date) {

                    Debt d = new Debt(-1, false, split[1], split[2], split[3], split[4],split[5], "", 0, new java.util.Date().toString());
                    Intent i = new Intent(getApplicationContext(), DetailActivity.class);
                    i.putExtra("object", d);
                    startActivity(i);
                }
                Toast.makeText(this, string, Toast.LENGTH_LONG).show();
                //startActivity(intent);
            }
        }
    }
}