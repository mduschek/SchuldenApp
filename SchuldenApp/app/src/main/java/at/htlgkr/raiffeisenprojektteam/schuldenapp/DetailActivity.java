package at.htlgkr.raiffeisenprojektteam.schuldenapp;

import android.app.Dialog;
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
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
    public static SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
    private TextView textViewDate, textViewCreateLoanDescription, textViewStatus, textViewIban, textViewBic;
    private RadioButton radioButtonDebtor, radioButtonCreditor;
    private Button buttonSelectDate, buttonManualInput, buttonBluetooth, buttonNfc, buttonGenerateQrCode, buttonOther, buttonPayDebt, buttonConfirmPayment;
    private EditText edVal, edUsuage, edIBAN, edBIC, edFirstname, edLastname;
    private LinearLayout linearLayoutPartnerData;
    private LinearLayout linearLayoutName;
    private RadioGroup radioGroupCreditorDebtor;
    public static final String LINK = "http://at.htlgkr.schuldenapp.createloan/schuldenapp?content=";
    //STRUKTUR: ?content=depttype;Michael;Duschek;Usuage;IBAN;30.65;12.12.16
    private static final String TAG = "*=DetailActivity";
    //private String nfcString = "";
    private NfcAdapter nfcAdapter;
    //private CalendarView calendarView;

    private String firstname = "", lastname = "", usage = "", value = "", iban = "", bic = "", partnerIsCreditor = "";
    private Date date = new Date();

    //public int dialogWich = -1;

    public Debt debt;
    public boolean iAmCreditor = true;
    public boolean isArchiveEntry = false;

    public final int QR_GEN_REQ_CODE = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_create_detail);

        //recover data on recreation
        if (savedInstanceState != null) {
            iAmCreditor = savedInstanceState.getBoolean(IS_DEBTOR_KEY);
            Log.w(TAG, "savedInstanceState" + iAmCreditor);
        }

        textViewDate = (TextView) findViewById(R.id.textViewDate);
        textViewIban = (TextView) findViewById(R.id.tvIban);
        textViewBic = (TextView) findViewById(R.id.tvBic);
        textViewCreateLoanDescription = (TextView) findViewById(R.id.textViewCreateLoanDescription);
        textViewStatus = (TextView) findViewById(R.id.textViewStatus);
        buttonSelectDate = (Button) findViewById(R.id.buttonSelectDate);
        buttonManualInput = (Button) findViewById(R.id.buttonManualInput);
        buttonNfc = (Button) findViewById(R.id.buttonNfc);
        buttonGenerateQrCode = (Button) findViewById(R.id.buttonGenerateQrCode);
        buttonOther = (Button) findViewById(R.id.buttonOther);      //Activity Chooser mit anderen Apps
        buttonPayDebt = (Button) findViewById(R.id.buttonPayDebt);  //Button setzt den Status auf Bezahlt
        buttonConfirmPayment = (Button) findViewById(R.id.buttonConfirmPayment);

        radioButtonDebtor = (RadioButton) findViewById(R.id.radioButtonDebtor);
        radioButtonCreditor = (RadioButton) findViewById(R.id.radioButtonCreditor);
        radioGroupCreditorDebtor = (RadioGroup) findViewById(R.id.radioGroupCreditorDebtor);

        edVal = (EditText) findViewById(R.id.editTextValue);
        edUsuage = (EditText) findViewById(R.id.editTextUsuage);
        edIBAN = (EditText) findViewById(R.id.editTextIban);
        edBIC = (EditText) findViewById(R.id.editTextBic);
        edFirstname = (EditText) findViewById(R.id.editTextFirstName);
        edLastname = (EditText) findViewById(R.id.editTextLastName);

        linearLayoutPartnerData = (LinearLayout) findViewById(R.id.linearLayoutPartnerData);
        linearLayoutName = (LinearLayout) findViewById(R.id.linearLayoutName);
        //buttonBluetooth.setVisibility(View.GONE);
        //buttonNfc.setVisibility(View.GONE);
        //buttonOther.setVisibility(View.GONE);


        //NFC
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);

        if (nfcAdapter == null) {
            buttonNfc.setVisibility(View.GONE);
        }

        if (getIntent().getExtras() != null) {
            debt = (Debt) getIntent().getExtras().getSerializable("object");
            if (debt.isiAmCreditor()) {
                iAmCreditor = true;
            } else {
                iAmCreditor = false;
            }

            isArchiveEntry = getIntent().getBooleanExtra("isArchiveEntry", false);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        setInputs();
    }

    public void onButtonPressed(View source) {

        if (!checkInputValues()) {
            Toast t = Toast.makeText(this, "Bitte alle Felder ausfüllen", Toast.LENGTH_LONG);
            t.show();
        }
        switch (source.getId()) {
            case R.id.buttonManualInput:
                insert("open");
                //Toast.makeText(this, "Inserted", Toast.LENGTH_LONG).show();
                finish();
                break;
            case R.id.buttonNfc:
                nfc();
                break;
            case R.id.buttonGenerateQrCode:
                Intent qrgenint = new Intent(this, QrGeneratorActivity.class);
                qrgenint.setAction(Intent.ACTION_SEND);
                initTexts();
                Log.d(TAG + "GenQR", partnerIsCreditor + ";" + firstname + ";" + lastname + ";" + usage + ";" + iban + ";" + bic + ";" + value + ";" + sdf.format(date));

                String shareData = partnerIsCreditor + ";" + firstname + ";" + lastname + ";" + usage + ";" + iban + ";" + bic + ";" + value + ";" + sdf.format(date);
                //createStuzziString(); //

                String stuzzaData = MainActivity.createStuzzaString(firstname, lastname, iban, bic, Float.parseFloat(value), usage);

                qrgenint.putExtra("shareData", URLEncoder.encode(shareData));
                qrgenint.putExtra("stuzzaData", URLEncoder.encode(stuzzaData));
                startActivityForResult(qrgenint, QR_GEN_REQ_CODE);
                break;
            case R.id.buttonOther:
                initTexts();
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                //STRUKTUR: ?content=depttype;Michael;Duschek;Usuage;IBAN;30.65;12.12.16
                Log.w(TAG, "bic: "+bic);
                String dataString = partnerIsCreditor + ";" + firstname + ";" + lastname + ";" + usage + ";" + iban + ";" + bic + ";" + value + ";" + sdf.format(date);
                dataString = URLEncoder.encode(dataString);
                sendIntent.putExtra(Intent.EXTRA_TEXT, LINK + dataString);
                sendIntent.setType("text/plain");
                startActivity(Intent.createChooser(sendIntent, "App zum Senden auswählen"));
                if (debt == null) {
                    insert("not_paid");
                    //Toast.makeText(this, "Inserted", Toast.LENGTH_LONG).show();
                } else {
                    //Toast.makeText(this, "UPDATED", Toast.LENGTH_LONG).show();
                    if (debt.isiAmCreditor())
                        MainActivity.db.execSQL("UPDATE " + TblDebts.TABLE_NAME + " SET status = 'not_paid' WHERE _id = " + debt.getId() + ";");
                    else
                        MainActivity.db.execSQL("UPDATE " + TblDebts.TABLE_NAME + " SET status = 'not_paid' WHERE _id = " + debt.getId() + ";");
                }
                finish();
                /*Intent sendIntent = new Intent();
                sendIntent.setData (Uri.parse("schuldenapp://createloan"));
                startActivity(Intent.createChooser(sendIntent, "Titel"));*/
                break;
            case R.id.buttonPayDebt:
                Intent baintent = this.getPackageManager().getLaunchIntentForPackage("at.htlgkr.raiffeisenprojektteam.bezahlapp");
                //baintent.putExtra("BezahlApp", transactionToStringConverter());
                initTexts();
                baintent.putExtra("BezahlApp", MainActivity.createStuzzaString(firstname, lastname, iban, bic, Float.parseFloat(value), usage));
                startActivity(baintent);
                break;
            case R.id.buttonSelectDate:
                Dialog dateDialog = new Dialog(this);
                dateDialog.setContentView(R.layout.dialog_date_layout);

                final DatePicker dp = (DatePicker) dateDialog.findViewById(R.id.datepicker);
                dp.init(dp.getYear(), dp.getMonth(), dp.getDayOfMonth(), new DatePicker.OnDateChangedListener() {
                    @Override
                    public void onDateChanged(DatePicker datePicker, int i, int i1, int i2) {
                        GregorianCalendar gregorianCalendar = new GregorianCalendar(dp.getYear(), dp.getMonth(), dp.getDayOfMonth());
                        date = gregorianCalendar.getTime();
                        //Toast.makeText(getApplicationContext(), sdf.format(date), Toast.LENGTH_LONG).show();
                        Log.d(TAG, "+" + sdf.format(date));
                        textViewDate.setText(sdf.format(date).toString());
                    }
                });

                dateDialog.show();
                break;

            case R.id.buttonConfirmPayment:
                MainActivity.db.execSQL("UPDATE " + TblDebts.TABLE_NAME + " SET status = 'paid' WHERE _id = " + debt.getId() + ";");
                finish();
                break;
        }
    }

    private void nfc() {
        if (!NfcAdapter.getDefaultAdapter(this).isEnabled()) {
            Toast.makeText(getApplicationContext(), "Bitte aktivieren Sie NFC und drücken Sie dann zurück, um hierher zurückzukehren!", Toast.LENGTH_LONG).show();
            startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
        } else {
            initTexts();
            Intent i = new Intent(this, NFCSender.class);
            Log.w(TAG, "iAmCreditor " + iAmCreditor + "partnerIsCreditor " + partnerIsCreditor);
            i.putExtra("partneriscreditor", partnerIsCreditor + "");
            i.putExtra("firstname", firstname);
            i.putExtra("lastname", lastname);
            i.putExtra("usage", usage);
            i.putExtra("iban", iban);
            i.putExtra("bic", bic);
            i.putExtra("value", value);
            i.putExtra("date", sdf.format(date));
            if (debt == null || debt.getId() == -1) {
                i.putExtra("isNewEntry", true);
                //Toast.makeText(this, "NewEntry", Toast.LENGTH_LONG).show();
            } else {
                i.putExtra("isNewEntry", false);
                i.putExtra("updateId", debt.getId());
            }
            Log.w(TAG, firstname + lastname + usage + iban +bic+ value + partnerIsCreditor);
            startActivity(i);
        }
    }

    public void onRadioButtonClicked(View source) {
        switch (source.getId()) {
            case R.id.radioButtonDebtor:
                iAmCreditor = false;
                setInputs();
                break;

            case R.id.radioButtonCreditor:
                iAmCreditor = true;
                setInputs();
                break;
        }
    }

    private void setInputs() {
        if (debt != null) {             //Wenn existierende Schuld aufgerufen wird
            if (debt.getId() == -1) {
                edIBAN.setText(debt.getiBan() + "");
                edBIC.setText(debt.getBic() + "");
                edFirstname.setText(debt.getDeptorFirstName() + "");
                edLastname.setText(debt.getDeptorLastName() + "");
                radioButtonDebtor.setChecked(true);
                radioGroupCreditorDebtor.setEnabled(false);
                iAmCreditor = false;
                buttonConfirmPayment.setVisibility(View.GONE);
                buttonPayDebt.setVisibility(View.GONE);
                Log.w(TAG, "iamCreditor: " + debt.isiAmCreditor());
            } else {
                if (debt.isiAmCreditor()) {
                    radioButtonCreditor.setChecked(true);

                    buttonPayDebt.setVisibility(View.GONE);
                    buttonConfirmPayment.setVisibility(View.VISIBLE);

                } else {
                    radioButtonDebtor.setChecked(true);

                    buttonPayDebt.setVisibility(View.VISIBLE);
                    buttonConfirmPayment.setVisibility(View.GONE);
                }

                edVal.setText(debt.getValue() + "");
                edUsuage.setText(debt.getUsuage() + "");
                edIBAN.setText(debt.getiBan() + "");
                edBIC.setText(debt.getBic() + "");
                edFirstname.setText(debt.getDeptorFirstName() + "");
                edLastname.setText(debt.getDeptorLastName() + "");
                switch (debt.getStatus())
                {
                    case "open": textViewStatus.setText("Status: Offen"); break;
                    case "paid": textViewStatus.setText("Status: Bezahlt"); break;
                    case "not_paid": textViewStatus.setText("Status: Nicht bezahlt");break;
                }

                try {
                    textViewDate.setText("Datum: "+debt.getDate());
                    date = sdf.parse(debt.getDate());
                } catch (ParseException e) {
                    e.printStackTrace();
                }


                if (debt.getStatus() != "open") {           //Wenn status "open" ist
                    radioButtonCreditor.setEnabled(false);
                    radioButtonDebtor.setEnabled(false);
                    edVal.setEnabled(false);
                    edUsuage.setEnabled(false);
                    edIBAN.setEnabled(false);
                    edBIC.setEnabled(false);
                    edFirstname.setEnabled(false);
                    edLastname.setEnabled(false);

                    textViewDate.setVisibility(View.VISIBLE);

                    //Buttons
                    buttonSelectDate.setVisibility(View.GONE);
                    buttonManualInput.setVisibility(View.GONE);
                    buttonNfc.setVisibility(View.VISIBLE);
                    buttonGenerateQrCode.setVisibility(View.VISIBLE);
                    buttonOther.setVisibility(View.VISIBLE);
                    //buttonPayDebt.setVisibility(View.VISIBLE);
                }
            }

        } else {            //Wenn neue Schuld erstellt wird
            radioButtonCreditor.setClickable(true);
            radioButtonDebtor.setClickable(true);
            edVal.setEnabled(true);
            edUsuage.setEnabled(true);
            edIBAN.setEnabled(true);
            edBIC.setEnabled(true);
            edFirstname.setEnabled(true);
            edLastname.setEnabled(true);

            //Buttons
            buttonSelectDate.setVisibility(View.VISIBLE);
            buttonManualInput.setVisibility(View.VISIBLE);
            buttonNfc.setVisibility(View.VISIBLE);
            buttonGenerateQrCode.setVisibility(View.VISIBLE);
            buttonOther.setVisibility(View.VISIBLE);
            buttonPayDebt.setVisibility(View.GONE);
            buttonConfirmPayment.setVisibility(View.GONE);  //neu

        }
        if (nfcAdapter == null) {
            buttonNfc.setVisibility(View.GONE);
        }

        if (radioButtonCreditor.isChecked()) {   //Wenn Kreditor Markiert ist IBAN und BIC auf GONE setzen
            //linearLayoutPartnerData.setVisibility(View.GONE);
            //linearLayoutName.setVisibility(View.GONE);
            edIBAN.setVisibility(View.GONE);
            edBIC.setVisibility(View.GONE);
            textViewIban.setVisibility(View.GONE);
            textViewBic.setVisibility(View.GONE);
        } else {                                //Wenn Debitor Markiert ist IBAN und BIC auf VISIBLE setzen
            //linearLayoutPartnerData.setVisibility(View.VISIBLE);
            //linearLayoutName.setVisibility(View.VISIBLE);
            edIBAN.setVisibility(View.VISIBLE);
            edBIC.setVisibility(View.VISIBLE);
            textViewIban.setVisibility(View.VISIBLE);
            textViewBic.setVisibility(View.VISIBLE);

        }

        if (isArchiveEntry) {   //Wenn aus Archive aufgerufen alle Buttons ausblenden
            //buttonBluetooth.setVisibility(View.GONE);
            buttonNfc.setVisibility(View.GONE);
            buttonPayDebt.setVisibility(View.GONE);
            buttonConfirmPayment.setVisibility(View.GONE);
            buttonGenerateQrCode.setVisibility(View.GONE);
            buttonOther.setVisibility(View.GONE);
            textViewCreateLoanDescription.setVisibility(View.GONE);
        }
    }

    private void initTexts() {   //speichert die werte der textfelder in die variable
        DBData.firstname = edFirstname.getText().toString();
        DBData.lastname = edLastname.getText().toString();
        DBData.usuage = edUsuage.getText().toString();
        DBData.iban = edIBAN.getText().toString();
        DBData.bic = edBIC.getText().toString();
        DBData.value = edVal.getText().toString();

        partnerIsCreditor = !iAmCreditor + "";

        firstname = UserData.firstname;
        lastname = UserData.lastname;
        iban = UserData.iban;
        bic = UserData.bic;
        value = DBData.value;
        usage = DBData.usuage;

        Log.w(TAG + "senddat", firstname + lastname + iban + bic + value + usage);
    }

    private void insert(String status) {
        initTexts();

        ContentValues cv = new ContentValues();
        //Toast.makeText(getApplicationContext(), "insert", Toast.LENGTH_LONG).show();
        cv.put(TblDebts.I_AM_CREDITOR, iAmCreditor);
        cv.put(TblDebts.FIRSTNAME, DBData.firstname);
        cv.put(TblDebts.LASTNAME, DBData.lastname);
        cv.put(TblDebts.USAGE, DBData.usuage);
        cv.put(TblDebts.IBAN, DBData.iban);
        cv.put(TblDebts.BIC, DBData.bic);
        cv.put(TblDebts.STATUS, status);
        cv.put(TblDebts.VALUE, DBData.value);
        cv.put(TblDebts.DATE, sdf.format(date));
        MainActivity.db.insert(TblDebts.TABLE_NAME, null, cv);
    }

    private boolean checkInputValues() {
        if (edVal.getText().toString() != ""
                && edUsuage.getText().toString() != ""
                && edIBAN.getText().toString() != ""
                && edFirstname.getText().toString() != ""
                && edLastname.getText().toString() != ""
                && edBIC.getText().toString() != ""
            //&& date != null
                ) {
            return true;
        }
        return false;
    }

   /*private String transactionToStringConverter() {
        initTexts();
        return iban + ";" +
                firstname + ";" +
                lastname + ";" +
                usage + ";" +
                sdf.format(date) + ";" +
                value;
    }*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d("ActivityResult", "requestCode: " + requestCode);

        if (requestCode == QR_GEN_REQ_CODE) {
            if (resultCode == RESULT_OK) {
                if (debt == null || debt.getId() == -1) {
                    insert("not_paid");
                } else {
                    //Toast.makeText(this, "UPDATED", Toast.LENGTH_LONG).show();
                    MainActivity.db.execSQL("UPDATE " + TblDebts.TABLE_NAME + " SET status = 'not_paid' WHERE _id = " + debt.getId() + ";");
                }
                Log.d("ActivityResult", "RESULT OK");

                finish();
            }
            if (resultCode == RESULT_CANCELED) {
                Log.d("ActivityResult", "RESULT CANCELED");
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(IS_DEBTOR_KEY, iAmCreditor);
        super.onSaveInstanceState(outState);
    }
}
