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
        UserData.email = sharedPreferences.getString("pref_userdata_email", null);

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
    }
    //endregion

    private void insertIntoDb(final String[] split) {//STRUKTUR: ?content=depttype;Michael;Duschek;Usuage;IBAN;30.65;12.12.16
        if (split[0].equals("true")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Sind Sie sicher, dass Sie folgendes hinzufügen wollen?\nMir schuldet " + split[1] + " " + split[2] + " " + split[5] + "€ für " + split[3] + ", am " + split[6]);
            builder.setIcon(android.R.drawable.ic_dialog_alert);
            builder.setPositiveButton("Ja", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    ContentValues cv = new ContentValues();
                    Toast.makeText(getApplicationContext(), "inputAccepted", Toast.LENGTH_LONG).show();
                    cv.put(TblWhoOwesMe.PERS_WHO_OWES_ME_DATE, split[6]);
                    cv.put(TblWhoOwesMe.PERS_WHO_OWES_ME_FIRSTNAME, split[1]);
                    cv.put(TblWhoOwesMe.PERS_WHO_OWES_ME_IBAN, split[4]);
                    cv.put(TblWhoOwesMe.PERS_WHO_OWES_ME_USUAGE, split[3]);
                    cv.put(TblWhoOwesMe.PERS_WHO_OWES_ME_LASTNAME, split[2]);
                    cv.put(TblWhoOwesMe.PERS_WHO_OWES_ME_VALUE, split[5]);
                    cv.put(TblWhoOwesMe.STATUS, "not_paid");
                    db.insert(TblWhoOwesMe.TABLE_NAME, null, cv);

                }
            });
            builder.setCancelable(false);
            builder.setNegativeButton("Nein", null);
            builder.create().show();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Sind Sie sicher, dass Sie folgendes hinzufügen wollen?\nIch schulde " + split[1] + " " + split[2] + " " + split[5] + "€ für " + split[3] + ", am " + split[6]);
            builder.setIcon(android.R.drawable.ic_dialog_alert);
            builder.setPositiveButton("Ja", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    ContentValues cv = new ContentValues();
                    cv.put(TblMyDebts.PERS_I_OWE_DATE, split[6]);
                    cv.put(TblMyDebts.PERS_I_OWE_FIRSTNAME, split[1]);
                    cv.put(TblMyDebts.PERS_I_OWE_IBAN, split[4]);
                    cv.put(TblMyDebts.PERS_I_OWE_USUAGE, split[3]);
                    cv.put(TblMyDebts.PERS_I_OWE_LASTNAME, split[2]);
                    cv.put(TblMyDebts.PERS_I_OWE_VALUE, split[5]);
                    cv.put(TblMyDebts.STATUS, "not_paid");
                    db.insert(TblMyDebts.TABLE_NAME, null, cv);
                }
            });
            builder.setCancelable(false);
            builder.setNegativeButton("Nein", null);
            builder.create().show();
        }

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
                insertIntoDb(string.split(";"));
                Toast.makeText(this, string, Toast.LENGTH_LONG).show();
                //startActivity(intent);
            }
        }
    }
}