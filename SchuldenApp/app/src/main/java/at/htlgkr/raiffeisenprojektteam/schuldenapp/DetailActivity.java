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

    private SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
    private TextView textViewCreateLoanDescription;
    private Button buttonManualInput, buttonBluetooth, buttonNfc, buttonGenerateQrCode, buttonOther, buttonPayDebt;
    private EditText edVal, edUsuage, edIBAN, edFirstname, edLastname;
    public static final String LINK = "http://at.htlgkr.schuldenapp.createloan/schuldenapp?content=";
    //STRUKTUR: ?content=Michael;Duschek;Usuage;IBAN;30.65;12.12.16
    private static final String TAG = "DetailActivity";
    private String nfcString = "";

    private String firstname = "", lastname = "", usuage = "", value = "", iban = "";
    private Date date = new Date();

    public int dialogWich = -1;

    public boolean isDebt;
//KOMMENTAr
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_detail_create);

        textViewCreateLoanDescription = (TextView) findViewById(R.id.textViewCreateLoanDescription);

        buttonManualInput = (Button) findViewById(R.id.buttonManualInput);
        buttonBluetooth = (Button) findViewById(R.id.buttonBluetooth);
        buttonNfc = (Button) findViewById(R.id.buttonNfc);
        buttonGenerateQrCode = (Button) findViewById(R.id.buttonGenerateQrCode);
        buttonOther = (Button) findViewById(R.id.buttonOther);      //Activity Chooser mit anderen Apps
        buttonPayDebt = (Button) findViewById(R.id.buttonPayDebt);  //Button setzt den Status auf Bezahlt

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

    public void onButtonPressed(View source) {
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
                Intent qrgenint=new Intent(this,QrScannerActivity.class);
                qrgenint.setAction(Intent.ACTION_SEND);
                String data = firstname + ";" + lastname + ";" + usuage + ";" + iban + ";" + value + ";" + sdf.format(date);
                qrgenint.putExtra("qr", data);
                break;
            case R.id.buttonOther:

                insert("not_paid");
                //Uri adress = Uri.parse("schuldenapp://createloan");  //URL parsen
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                //STRUKTUR: ?content=Michael;Duschek;Usuage;IBAN;30.65;12.12.16
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
                isDebt = true;
                setButtons();
                break;

            case R.id.radioButtonPayer:
                isDebt = false;
                setButtons();
                break;
        }
    }

    public void setButtons(){

    }

    @Override
    public NdefMessage createNdefMessage(NfcEvent nfcEvent) {
        //STRUKTUR: ?content=Michael;Duschek;Usuage;IBAN;30.65;12.12.16
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
        ContentValues cv = new ContentValues();
        cv.put(TblMyDebts.PERS_I_OWE_DATE, sdf.format(date));
        cv.put(TblMyDebts.PERS_I_OWE_FIRSTNAME, firstname);
        cv.put(TblMyDebts.PERS_I_OWE_IBAN, iban);
        cv.put(TblMyDebts.PERS_I_OWE_USUAGE, usuage);
        cv.put(TblMyDebts.PERS_I_OWE_LASTNAME, lastname);
        cv.put(TblMyDebts.PERS_I_OWE_VALUE, value);
        cv.put(TblMyDebts.STATUS, status);
        MainActivity.db.insert(TblWhoOwesMe.TABLE_NAME, null, cv);
    }

    //region Bluetooth
    private void Bluetooth() {
        initTexts();
        BluetoothAdapter blueAdapter = BluetoothAdapter.getDefaultAdapter();

        try {
            if (blueAdapter.isEnabled()) {
                Set<BluetoothDevice> bondedDevices = blueAdapter.getBondedDevices();
                //Michael;Duschek;Usuage;IBAN;30.65;12.12.16
                if (bondedDevices.size() > 0) {
                    BluetoothDevice[] devices = (BluetoothDevice[]) bondedDevices.toArray();

                    //ListDialog
                    bluetootDevicesDialog(devices);

                    BluetoothDevice device = (BluetoothDevice) devices[dialogWich];
                    ParcelUuid[] uuids = device.getUuids();
                    BluetoothSocket socket = device.createRfcommSocketToServiceRecord(uuids[dialogWich].getUuid());
                    socket.connect();
                    MainActivity.bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                    MainActivity.bw.write(firstname+";"+lastname+";"+usuage+";"+iban+";"+value+";"+date);
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

    private AlertDialog bluetootDevicesDialog (BluetoothDevice[] devices) {
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

}
    //endregion

