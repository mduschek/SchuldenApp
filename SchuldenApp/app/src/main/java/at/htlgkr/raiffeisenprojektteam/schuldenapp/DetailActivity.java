package at.htlgkr.raiffeisenprojektteam.schuldenapp;

import android.app.Dialog;
import android.bluetooth.BluetoothDevice;
import android.content.ContentValues;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by michael on 24.11.16.
 */

public class DetailActivity extends AppCompatActivity {

    private static final String IS_DEBTOR_KEY = "isDebtorKey";
    private SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
    private TextView textViewCreateLoanDescription, textViewStatus;
    private RadioButton radioButtonDebtor, radioButtonCreditor;
    private Button buttonManualInput, buttonBluetooth, buttonNfc, buttonGenerateQrCode, buttonOther, buttonPayDebt;
    private EditText edVal, edUsuage, edIBAN, edFirstname, edLastname;
    public static final String LINK = "http://at.htlgkr.schuldenapp.createloan/schuldenapp?content=";
    //STRUKTUR: ?content=depttype;Michael;Duschek;Usuage;IBAN;30.65;12.12.16
    private static final String TAG = "*=DetailActivity";
    private String nfcString = "";
    private boolean nfcIsEnabled = false;
    private BluetoothDevice selectedDevice;
    //private CalendarView calendarView;


    private String firstname = "", lastname = "", usage = "", value = "", iban = "", partnerIsCreditor = "";
    private Date date = new Date();

    public int dialogWich = -1;


    public Debt debt;
    public boolean iAmCreditor = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_create_detail);

        //recover data on recreation
        if (savedInstanceState != null) {
            iAmCreditor = savedInstanceState.getBoolean(IS_DEBTOR_KEY);
            Log.w(TAG, "savedInstanceState" + iAmCreditor);
        }

        textViewCreateLoanDescription = (TextView) findViewById(R.id.textViewCreateLoanDescription);
        textViewStatus = (TextView) findViewById(R.id.textViewStatus);
        buttonManualInput = (Button) findViewById(R.id.buttonManualInput);
        buttonBluetooth = (Button) findViewById(R.id.buttonBluetooth);
        buttonNfc = (Button) findViewById(R.id.buttonNfc);
        buttonGenerateQrCode = (Button) findViewById(R.id.buttonGenerateQrCode);
        buttonOther = (Button) findViewById(R.id.buttonOther);      //Activity Chooser mit anderen Apps
        buttonPayDebt = (Button) findViewById(R.id.buttonPayDebt);  //Button setzt den Status auf Bezahlt
        NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(this);


        radioButtonDebtor = (RadioButton) findViewById(R.id.radioButtonDebtor);
        radioButtonCreditor = (RadioButton) findViewById(R.id.radioButtonCreditor);

        //buttonBluetooth.setVisibility(View.GONE);
        //buttonNfc.setVisibility(View.GONE);
        //buttonOther.setVisibility(View.GONE);

        edVal = (EditText) findViewById(R.id.editTextValue);
        edUsuage = (EditText) findViewById(R.id.editTextUsuage);
        edIBAN = (EditText) findViewById(R.id.editTextIban);
        edFirstname = (EditText) findViewById(R.id.editTextFirstName);
        edLastname = (EditText) findViewById(R.id.editTextLastName);

        //NFC
        if (nfcAdapter == null) {

            buttonNfc.setVisibility(View.GONE);
        } else {
            if (nfcAdapter.isEnabled()) {
                nfcIsEnabled = true;
            }
        }

        if (getIntent().getExtras() != null) {
            debt = (Debt) getIntent().getExtras().getSerializable("object");
            iAmCreditor = debt.isiAmCreditor();
        }
    }

    @Override
    protected void onResume() {
        super.onPostResume();
        //setButtons();
        try {
            setInputs();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void onButtonPressed(View source) {

        if (!checkInputValues()) {
            Toast t = Toast.makeText(this, "Bitte alle Felder ausfüllen", Toast.LENGTH_LONG);
            t.show();
        }
        switch (source.getId()) {
            case R.id.buttonManualInput:
                insert("open");
                Toast.makeText(this, "Inserted", Toast.LENGTH_LONG).show();
                finish();
                break;
            /*case R.id.buttonBluetooth:
                bluetooth();
                break;*/
            case R.id.buttonNfc:
                nfc();
                break;
            case R.id.buttonGenerateQrCode:
                Intent qrgenint = new Intent(this, QrGeneratorActivity.class);
                qrgenint.setAction(Intent.ACTION_SEND);
                initTexts();
                Log.d(TAG + "GenQR", partnerIsCreditor + ";" + firstname + ";" + lastname + ";" + usage + ";" + iban + ";" + value + ";" + sdf.format(date));
                String data = partnerIsCreditor + ";" + firstname + ";" + lastname + ";" + usage + ";" + iban + ";" + value + ";" + sdf.format(date);
                qrgenint.putExtra("qr", URLEncoder.encode(data));
                startActivity(qrgenint);
                if (debt == null) {
                    insert("not_paid");
                } else {
                    Toast.makeText(this, "UPDATED", Toast.LENGTH_LONG).show();
                    if (debt.isiAmCreditor())
                        MainActivity.db.execSQL("UPDATE " + TblWhoOwesMe.TABLE_NAME + " SET status = 'not_paid' WHERE _id = " + debt.getId() + ";");
                    else
                        MainActivity.db.execSQL("UPDATE " + TblMyDebts.TABLE_NAME + " SET status = 'not_paid' WHERE _id = " + debt.getId() + ";");
                }
                finish();
                break;
            case R.id.buttonOther:

                initTexts();
                //Uri adress = Uri.parse("schuldenapp://createloan");  //URL parsen
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                //STRUKTUR: ?content=depttype;Michael;Duschek;Usuage;IBAN;30.65;12.12.16
                String dataString = partnerIsCreditor + ";" + firstname + ";" + lastname + ";" + usage + ";" + iban + ";" + value + ";" + sdf.format(date);
                dataString = URLEncoder.encode(dataString);
                sendIntent.putExtra(Intent.EXTRA_TEXT, LINK + dataString);
                //sendIntent.putExtra(Intent.EXTRA_ORIGINATING_URI, adress); //geändert
                sendIntent.setType("text/plain");
                startActivity(Intent.createChooser(sendIntent, "App zum Senden auswählen"));
                if (debt == null) {
                    insert("not_paid");
                    Toast.makeText(this, "Inserted", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, "UPDATED", Toast.LENGTH_LONG).show();
                    if (debt.isiAmCreditor())
                        MainActivity.db.execSQL("UPDATE " + TblWhoOwesMe.TABLE_NAME + " SET status = 'not_paid' WHERE _id = " + debt.getId() + ";");
                    else
                        MainActivity.db.execSQL("UPDATE " + TblMyDebts.TABLE_NAME + " SET status = 'not_paid' WHERE _id = " + debt.getId() + ";");
                }
                finish();
                /*Intent sendIntent = new Intent();
                sendIntent.setData (Uri.parse("schuldenapp://createloan"));
                startActivity(Intent.createChooser(sendIntent, "Titel"));*/
                break;
            case R.id.buttonPayDebt:
                //Intent bezahlIntent = new Intent(this, BezahlApp.class);
                //bezahlIntent.setAction(Intent.ACTION_SEND);
                //bezahlIntent.putExtra("BezahlApp", "Alexander;Perndorfer;Essen;AT34442566756567;30.65");
                //startActivity(bezahlIntent);

                Intent baintent = this.getPackageManager().getLaunchIntentForPackage("at.htlgkr.raiffeisenprojektteam.bezahlapp");

                //baintent.putExtra("BezahlApp", "AT34442566756567;Alexander;Perndorfer;Essen;9.2.2017;30.65");
                //Toast.makeText(this,"Data: " + transactionToStringConverter(),Toast.LENGTH_LONG).show();
                baintent.putExtra("BezahlApp", transactionToStringConverter());
                startActivity(baintent);
                break;
            case R.id.btnSlctDate:
                Dialog dateDialog = new Dialog(this);
                dateDialog.setContentView(R.layout.dialog_date_layout);

                final DatePicker dp = (DatePicker) dateDialog.findViewById(R.id.datepicker);
                dp.init(dp.getYear(), dp.getMonth(), dp.getDayOfMonth(), new DatePicker.OnDateChangedListener() {
                    @Override
                    public void onDateChanged(DatePicker datePicker, int i, int i1, int i2) {
                        GregorianCalendar gregorianCalendar = new GregorianCalendar(dp.getYear(), dp.getMonth(), dp.getDayOfMonth());
                        date = gregorianCalendar.getTime();
                        Toast.makeText(getApplicationContext(), sdf.format(date), Toast.LENGTH_LONG).show();
                        Log.d(TAG, "+" + sdf.format(date));
                    }
                });


                dateDialog.show();
                break;
        }
    }

    private void nfc() {
        if (!nfcIsEnabled) {
            Toast.makeText(getApplicationContext(), "Bitte aktivieren Sie NFC und drücken Sie dann zurück, um hierher zurückzukehren!", Toast.LENGTH_LONG).show();
            startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
        } else {

            if (debt != null) {

                if (debt.isiAmCreditor()) {
                    MainActivity.db.execSQL("DELETE FROM " + TblWhoOwesMe.TABLE_NAME + " WHERE " + TblWhoOwesMe.ID + " = " + debt.getId());
                    Toast.makeText(this, "DELETED TblWhoOwesMe", Toast.LENGTH_LONG).show();
                } else {
                    MainActivity.db.execSQL("DELETE FROM " + TblMyDebts.TABLE_NAME + " WHERE " + TblWhoOwesMe.ID + " = " + debt.getId());
                    Toast.makeText(this, "DELETED TblMyDebts", Toast.LENGTH_LONG).show();
                }

            }
            initTexts();
            Intent i = new Intent(this, NFCSender.class);
            Log.w(TAG, "iAmCreditor "+iAmCreditor + "partnerIsCreditor "+partnerIsCreditor );
            i.putExtra("partneriscreditor", partnerIsCreditor);
            i.putExtra("firstname", firstname);
            i.putExtra("lastname", lastname);
            i.putExtra("usage", usage);
            i.putExtra("iban", iban);
            i.putExtra("value", value);
            i.putExtra("date", sdf.format(date));
            Log.w(TAG, firstname + lastname + usage + iban + value + partnerIsCreditor);
            startActivity(i);
        }
    }

    public void onRadioButtonClicked(View source) {
        switch (source.getId()) {
            case R.id.radioButtonDebtor:
                iAmCreditor = false;
                setButtons();
                break;

            case R.id.radioButtonCreditor:
                iAmCreditor = true;
                setButtons();
                break;
        }
    }

    @Deprecated
    private void getDebtOrCredit() {
        iAmCreditor = radioButtonDebtor.isSelected();
        setButtons();
    }

    @Deprecated
    private void setButtons() {


//
//        buttonManualInput, Nfc, GenerateQrCode, Other: wenn betrag, verwendung, iban, vorname, nachname, datum != null
//        buttonBluetooth: wenn betrag, verwendung, datum != null (fremder iban, vorname, nachname soll übertragen werden, funktioniert noch nicht?)
//
//
//        if (iAmCreditor){
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

    private void setInputs() throws ParseException {
        if (debt != null) {
            if (debt.isiAmCreditor()) radioButtonCreditor.setChecked(true);
            else radioButtonDebtor.setChecked(true);
            edVal.setText(debt.getValue() + "");
            edUsuage.setText(debt.getUsuage() + "");
            edIBAN.setText(debt.getiBan() + "");
            edFirstname.setText(debt.getDeptorFirstName() + "");
            edLastname.setText(debt.getDeptorLastName() + "");
            //date = debt.getDate();    DATE SETZEN
            textViewStatus.setText(debt.getStatus() + "");
            date = sdf.parse(debt.getDate());

            if (debt.getStatus() != "open") {
                radioButtonCreditor.setClickable(false);
                radioButtonDebtor.setClickable(false);
                edVal.setEnabled(false);
                edUsuage.setEnabled(false);
                edIBAN.setEnabled(false);
                edFirstname.setEnabled(false);
                edLastname.setEnabled(false);

                //Buttons
                buttonManualInput.setVisibility(View.GONE);
                buttonNfc.setVisibility(View.VISIBLE);
                buttonGenerateQrCode.setVisibility(View.VISIBLE);
                buttonOther.setVisibility(View.VISIBLE);
                buttonPayDebt.setVisibility(View.VISIBLE);
            }
        } else {
            radioButtonCreditor.setClickable(true);
            radioButtonDebtor.setClickable(true);
            edVal.setEnabled(true);
            edUsuage.setEnabled(true);
            edIBAN.setEnabled(true);
            edFirstname.setEnabled(true);
            edLastname.setEnabled(true);

            //Buttons
            buttonManualInput.setVisibility(View.VISIBLE);
            buttonNfc.setVisibility(View.VISIBLE);
            buttonGenerateQrCode.setVisibility(View.VISIBLE);
            buttonOther.setVisibility(View.VISIBLE);
            buttonPayDebt.setVisibility(View.GONE);
        }

    }


    private void initTexts()//speichert die werte der textfelder in die variablen
    {
        DBData.firstname = edFirstname.getText().toString();
        DBData.lastname = edLastname.getText().toString();
        DBData.usuage = edUsuage.getText().toString();
        DBData.iban = edIBAN.getText().toString();
        DBData.value = edVal.getText().toString();
        Log.w(TAG, "iamcreditor "+iAmCreditor);
        partnerIsCreditor = !iAmCreditor + "";

        firstname = UserData.firstname;
        lastname = UserData.lastname;
        iban = UserData.iban;
        value = DBData.value;
        usage = DBData.usuage;
        Log.w(TAG + "senddat", firstname + lastname + iban + value + usage);
    }

    private void insert(String status) {
        initTexts();

        if (iAmCreditor) {//iAmCreditor==true == wir sind Gläubiger== wir leihen geld
            ContentValues cv = new ContentValues();
            cv.put(TblWhoOwesMe.PERS_WHO_OWES_ME_DATE, sdf.format(date));
            cv.put(TblWhoOwesMe.PERS_WHO_OWES_ME_FIRSTNAME, DBData.firstname);
            cv.put(TblWhoOwesMe.PERS_WHO_OWES_ME_IBAN, DBData.iban);
            cv.put(TblWhoOwesMe.PERS_WHO_OWES_ME_USUAGE, DBData.usuage);
            cv.put(TblWhoOwesMe.PERS_WHO_OWES_ME_LASTNAME, DBData.lastname);
            cv.put(TblWhoOwesMe.PERS_WHO_OWES_ME_VALUE, DBData.value);
            cv.put(TblWhoOwesMe.STATUS, status);
            MainActivity.db.insert(TblWhoOwesMe.TABLE_NAME, null, cv);
        } else {
            ContentValues cv = new ContentValues();
            cv.put(TblMyDebts.PERS_I_OWE_DATE, sdf.format(date));
            cv.put(TblMyDebts.PERS_I_OWE_FIRSTNAME, DBData.firstname);
            cv.put(TblMyDebts.PERS_I_OWE_IBAN, DBData.iban);
            cv.put(TblMyDebts.PERS_I_OWE_USUAGE, DBData.usuage);
            cv.put(TblMyDebts.PERS_I_OWE_LASTNAME, DBData.lastname);
            cv.put(TblMyDebts.PERS_I_OWE_VALUE, DBData.value);
            cv.put(TblMyDebts.STATUS, status);
            MainActivity.db.insert(TblMyDebts.TABLE_NAME, null, cv);
        }
    }


    private boolean checkInputValues() {
        if (edVal.getText().toString() != ""
                && edUsuage.getText().toString() != ""
                && edIBAN.getText().toString() != ""
                && edFirstname.getText().toString() != ""
                && edLastname.getText().toString() != ""
            //&& date != null
                ) {
            return true;
        }
        return false;
    }

    private String transactionToStringConverter (){
        initTexts();
        return iban + ";" +
                firstname + ";" +
                lastname + ";" +
                usage + ";" +
                date + ";" +
                value;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(IS_DEBTOR_KEY, iAmCreditor);
        super.onSaveInstanceState(outState);
    }
}
