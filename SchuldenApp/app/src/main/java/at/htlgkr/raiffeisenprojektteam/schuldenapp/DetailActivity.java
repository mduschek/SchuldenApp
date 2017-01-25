package at.htlgkr.raiffeisenprojektteam.schuldenapp;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.os.Bundle;
import android.os.ParcelUuid;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

/**
 * Created by michael on 24.11.16.
 */

public class DetailActivity extends AppCompatActivity implements NfcAdapter.CreateNdefMessageCallback {

    private static final String IS_DEBTOR_KEY = "isDebtorKey";
    private SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
    private TextView textViewCreateLoanDescription;
    private RadioButton radioButtonDebtor, radioButtonCreditor;
    private Button buttonManualInput, buttonBluetooth, buttonNfc, buttonGenerateQrCode, buttonOther, buttonPayDebt;
    private EditText edVal, edUsuage, edIBAN, edFirstname, edLastname;
    public static final String LINK = "http://at.htlgkr.schuldenapp.createloan/schuldenapp?content=";
    //STRUKTUR: ?content=Michael;Duschek;Usuage;IBAN;30.65;12.12.16
    private static final String TAG = "DetailActivity";
    private String nfcString = "";

    private String firstname = "", lastname = "", usuage = "", value = "", iban = "";
    private Date date = new Date();

    public int dialogWich = -1;

    public boolean iAmDebtor = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_detail_create);

        //recover data on recreation
        if (savedInstanceState != null) {
            iAmDebtor = savedInstanceState.getBoolean(IS_DEBTOR_KEY);
        }

        textViewCreateLoanDescription = (TextView) findViewById(R.id.textViewCreateLoanDescription);

        buttonManualInput = (Button) findViewById(R.id.buttonManualInput);
        buttonBluetooth = (Button) findViewById(R.id.buttonBluetooth);
        buttonNfc = (Button) findViewById(R.id.buttonNfc);
        buttonGenerateQrCode = (Button) findViewById(R.id.buttonGenerateQrCode);
        buttonOther = (Button) findViewById(R.id.buttonOther);      //Activity Chooser mit anderen Apps
        buttonPayDebt = (Button) findViewById(R.id.buttonPayDebt);  //Button setzt den Status auf Bezahlt

        radioButtonDebtor = (RadioButton)findViewById(R.id.radioButtonDebtor);
        radioButtonCreditor = (RadioButton)findViewById(R.id.radioButtonCreditor);

        //buttonBluetooth.setVisibility(View.GONE);
        //buttonNfc.setVisibility(View.GONE);
        //buttonOther.setVisibility(View.GONE);

        edVal = (EditText) findViewById(R.id.editTextValue);
        edUsuage = (EditText) findViewById(R.id.editTextUsuage);
        edIBAN = (EditText) findViewById(R.id.editTextIban);
        edFirstname = (EditText) findViewById(R.id.editTextFirstName);
        edLastname = (EditText) findViewById(R.id.editTextLastName);

        //NFC
        if (!MainActivity.nfcIsAvailable) buttonNfc.setVisibility(View.GONE);
        MainActivity.nfcAdapter.setNdefPushMessageCallback(this, this);
    }

    @Override
    protected void onResume() {
        super.onPostResume();
        setButtons();
    }

    public void onButtonPressed(View source) {

        if (!checkInputValues()){
            Toast t = Toast.makeText(this, "Bitte alle Felder ausfüllen", Toast.LENGTH_LONG);
            t.show();
        }
        switch (source.getId()) {
            case R.id.buttonManualInput:
                insert("open");
                finish();
                break;
            case R.id.buttonBluetooth:

                break;
            case R.id.buttonNfc:

                if (MainActivity.nfcAdapter.isEnabled() == false) {
                    Toast.makeText(getApplicationContext(), "Bitte aktivieren Sie NFC und drücken Sie dann zurück, um hierher zurückzukehren!", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
                } else {
                    Toast.makeText(getApplicationContext(), "NFC ist bereits aktiviert. Halten Sie jetzt Ihre Handys zusammen.", Toast.LENGTH_LONG).show();
                }
                insert("not_paid");

                break;
            case R.id.buttonGenerateQrCode:
                Intent qrgenint=new Intent(this,QrGeneratorActivity.class);
                qrgenint.setAction(Intent.ACTION_SEND);
                String data = iAmDebtor +";"+ firstname + ";" + lastname + ";" + usuage + ";" + iban + ";" + value + ";" + sdf.format(date);
                data = URLEncoder.encode(data);
                qrgenint.putExtra("qr", data);
                startActivity(qrgenint);
                break;
            case R.id.buttonOther:

                insert("not_paid");
                //Uri adress = Uri.parse("schuldenapp://createloan");  //URL parsen
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                //STRUKTUR: ?content=depttype;Michael;Duschek;Usuage;IBAN;30.65;12.12.16
                String dataString = firstname + ";" + lastname + ";" + usuage + ";" + iban + ";" + value + ";" + sdf.format(date);
                dataString = URLEncoder.encode(dataString);
                sendIntent.putExtra(Intent.EXTRA_TEXT, LINK + dataString);
                //sendIntent.putExtra(Intent.EXTRA_ORIGINATING_URI, adress); //geändert
                sendIntent.setType("text/plain");
                startActivity(Intent.createChooser(sendIntent, "App zum Senden auswählen"));

                /*Intent sendIntent = new Intent();
                sendIntent.setData (Uri.parse("schuldenapp://createloan"));
                startActivity(Intent.createChooser(sendIntent, "Titel"));*/
                break;
            case R.id.buttonPayDebt:
                Intent bezahlIntent=new Intent();
                bezahlIntent.setAction(Intent.ACTION_SEND);
                bezahlIntent.putExtra("BezahlApp","Alexander;Perndorfer;Essen;AT34442566756567;30.65");
                startActivity(bezahlIntent);
                break;
        }
    }

    public void onRadioButtonClicked(View source) {
        switch (source.getId()) {
            case R.id.radioButtonDateNow:
                date = new Date();
                break;

            case R.id.radioButtonManualDate:
                //DATEPICKERDIALOG
                Toast.makeText(getApplicationContext(), date.toString(), Toast.LENGTH_LONG).show();
                break;

            case R.id.radioButtonDebtor:
                iAmDebtor = true;
                setButtons();
                break;

            case R.id.radioButtonCreditor:
                iAmDebtor = false;
                setButtons();
                break;
        }
    }

    @Deprecated
    public void getDebtOrCredit(){
        iAmDebtor = radioButtonDebtor.isSelected();
        setButtons();
    }

    public void setButtons(){

        if (iAmDebtor){
            buttonManualInput.setVisibility(View.VISIBLE);
            buttonBluetooth.setVisibility(View.VISIBLE);
            buttonNfc.setVisibility(View.VISIBLE);
            buttonGenerateQrCode.setVisibility(View.VISIBLE);
            buttonOther.setVisibility(View.VISIBLE);
            buttonPayDebt.setVisibility(View.VISIBLE);
        }
        else {
            buttonManualInput.setVisibility(View.GONE);
            buttonBluetooth.setVisibility(View.GONE);
            buttonNfc.setVisibility(View.GONE);
            buttonGenerateQrCode.setVisibility(View.GONE);
            buttonOther.setVisibility(View.GONE);
            buttonPayDebt.setVisibility(View.GONE);
        }
        /*
            buttonManualInput, Nfc, GenerateQrCode, Other: wenn betrag, verwendung, iban, vorname, nachname, datum != null
            buttonBluetooth: wenn betrag, verwendung, datum != null (fremder iban, vorname, nachname soll übertragen werden, funktioniert noch nicht?)

        */
//        if (iAmDebtor){
//            if (edVal != null && edUsuage != null && date != null){
//                buttonBluetooth.setVisibility(View.VISIBLE);
//            }
//            if (edVal != null && edUsuage != null && edIBAN != null && edFirstname != null && edLastname != null && date != null){
//                buttonManualInput.setVisibility(View.VISIBLE);
//                buttonNfc.setVisibility(View.VISIBLE);
//                buttonGenerateQrCode.setVisibility(View.VISIBLE);
//                buttonOther.setVisibility(View.VISIBLE);
//                buttonPayDebt.setVisibility(View.VISIBLE);
//            }
//        }
//        else {
//            if (edVal == null && edUsuage == null && date == null){
//                buttonBluetooth.setVisibility(View.GONE);
//            }
//            if (edVal == null && edUsuage == null && edIBAN == null && edFirstname == null && edLastname == null && date == null){
//                buttonManualInput.setVisibility(View.GONE);
//                buttonNfc.setVisibility(View.GONE);
//                buttonGenerateQrCode.setVisibility(View.GONE);
//                buttonOther.setVisibility(View.GONE);
//                buttonPayDebt.setVisibility(View.GONE);
//            }
//        }
    }

    @Override
    public NdefMessage createNdefMessage(NfcEvent nfcEvent) {
        //STRUKTUR: ?content=depttype;Michael;Duschek;Usuage;IBAN;30.65;12.12.16
        initTexts();
        final String stringOut = firstname + ";" + lastname + ";" + usuage + ";" + iban + ";" + value + ";" + sdf.format(date);
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


    private void initTexts()//speichert die werte der textfelder in die variablen
    {
        firstname = edFirstname.getText().toString();
        lastname = edLastname.getText().toString();
        usuage = edUsuage.getText().toString();
        iban = edIBAN.getText().toString();
        value = edVal.getText().toString();
    }

    private void insert(String status) {
        initTexts();

        if(!iAmDebtor){//iAmDebtor==true == wir sind Schuldner== wir schulden geld
            ContentValues cv = new ContentValues();
            cv.put(TblWhoOwesMe.PERS_WHO_OWES_ME_DATE, sdf.format(date));
            cv.put(TblWhoOwesMe.PERS_WHO_OWES_ME_FIRSTNAME, firstname);
            cv.put(TblWhoOwesMe.PERS_WHO_OWES_ME_IBAN, iban);
            cv.put(TblWhoOwesMe.PERS_WHO_OWES_ME_USUAGE, usuage);
            cv.put(TblWhoOwesMe.PERS_WHO_OWES_ME_LASTNAME, lastname);
            cv.put(TblWhoOwesMe.PERS_WHO_OWES_ME_VALUE, value);
            cv.put(TblWhoOwesMe.STATUS, status);
            MainActivity.db.insert(TblWhoOwesMe.TABLE_NAME, null, cv);
        }
        else{
            ContentValues cv = new ContentValues();
            cv.put(TblMyDebts.PERS_I_OWE_DATE, sdf.format(date));
            cv.put(TblMyDebts.PERS_I_OWE_FIRSTNAME, firstname);
            cv.put(TblMyDebts.PERS_I_OWE_IBAN, iban);
            cv.put(TblMyDebts.PERS_I_OWE_USUAGE, usuage);
            cv.put(TblMyDebts.PERS_I_OWE_LASTNAME, lastname);
            cv.put(TblMyDebts.PERS_I_OWE_VALUE, value);
            cv.put(TblMyDebts.STATUS, status);
            MainActivity.db.insert(TblMyDebts.TABLE_NAME, null, cv);
        }
    }

    //region Bluetooth
    private void Bluetooth() {
        initTexts();
        BluetoothAdapter blueAdapter = BluetoothAdapter.getDefaultAdapter();

        try {
            if (blueAdapter.isEnabled()) {
                Set<BluetoothDevice> bondedDevices = blueAdapter.getBondedDevices();
                //depttype;Michael;Duschek;Usuage;IBAN;30.65;12.12.16
                if (bondedDevices.size() > 0) {
                    BluetoothDevice[] devices = (BluetoothDevice[]) bondedDevices.toArray();

                    //ListDialog
                    bluetoothDevicesDialog(devices);

                    BluetoothDevice device = (BluetoothDevice) devices[dialogWich];
                    ParcelUuid[] uuids = device.getUuids();
                    BluetoothSocket socket = device.createRfcommSocketToServiceRecord(uuids[dialogWich].getUuid());
                    socket.connect();
                    MainActivity.bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                    MainActivity.bw.write(firstname+";"+lastname+";"+usuage+";"+iban+";"+value+";"+date);
                    MainActivity.bw.flush();

                    //SENDER VERBINDET SICH IMMER MIR DEM EMPFÄNGER
                }

                Log.e("error", "No appropriate paired devices.");
            } else
            {
                Log.e("error", "Bluetooth is disabled.");
            }
        }
        catch (Exception ex)
        {
            Log.e(TAG, "Bluetooth: ", ex);
        }
    }

    private AlertDialog bluetoothDevicesDialog(BluetoothDevice[] devices) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Bluetooth Gerät auswählen");

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_singlechoice);
        for (BluetoothDevice device : devices){
            arrayAdapter.add(device.getName());
        }

        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                dialogWich = which;
                // The 'which' argument contains the index position
                // of the selected item
            }
        });
        return builder.create();
    }

    private boolean checkInputValues(){
        if (edVal.getText().toString() != ""
                && edUsuage.getText().toString() != ""
                && edIBAN.getText().toString() != ""
                && edFirstname.getText().toString() != ""
                && edLastname.getText().toString() != ""
            //&& date != null
            ){
            return true;
        }
        return false;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(IS_DEBTOR_KEY, iAmDebtor);
        super.onSaveInstanceState(outState);
    }
}
    //endregion