package at.htlgkr.raiffeisenprojektteam.schuldenapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.net.URLDecoder;

public class QrGeneratorActivity extends AppCompatActivity {

    ImageView imgVw;

    RadioGroup radioGroupQrCode;
    RadioButton radioButtonCreateShareQrCode, onRadioButtonCreateStuzzaQrCode;
    //Button buttonQrCodeScannerCancel, buttonQrCodeScannerConfirmation;
    String shareData;
    String stuzzaData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_scanner);

        imgVw = (ImageView) findViewById(R.id.imgView);
        radioGroupQrCode = (RadioGroup)findViewById(R.id.radioGroupQrCode);

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            shareData = extras.getString("shareData");
            stuzzaData = extras.getString("stuzzaData");
        }

        //Falls nur 1 QR Code verfügbar, RadioGroup ausschalten
        if (shareData == null || stuzzaData == null){
            radioGroupQrCode.setVisibility(View.GONE);

            if (shareData == null && stuzzaData != null)  shareData = stuzzaData;
            else if (stuzzaData == null && shareData != null)  stuzzaData = shareData;
        }

        radioButtonCreateShareQrCode = (RadioButton) findViewById(R.id.radioButtonCreateShareQrCode);

        //onRadioButtonCreateStuzzaQrCode = (RadioButton) findViewById(R.id.onRadioButtonCreateStuzzaQrCode);

        //buttonQrCodeScannerCancel = (Button) findViewById(R.id.buttonQrCodeScannerCancel);
        //buttonQrCodeScannerConfirmation = (Button) findViewById(R.id.buttonQrCodeScannerConfirmation);

        //Log.d("shareData", shareData);
        //Log.d("stuzzaData", stuzzaData);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (radioButtonCreateShareQrCode.isChecked())
            createQrCode(shareData);
        else
            createQrCode(stuzzaData);
    }

    public void onRadioButtonClicked(View source) {
        switch (source.getId()) {
            case R.id.radioButtonCreateShareQrCode:
                createQrCode(shareData);
                break;

            case R.id.onRadioButtonCreateStuzzaQrCode:
                createQrCode(stuzzaData);
                break;
        }
    }

    public void onButtonClicked(View source){
        switch (source.getId()) {
            case R.id.buttonQrCodeScannerCancel:
                setResult(RESULT_CANCELED);
                finish();
                break;

            case R.id.buttonQrCodeScannerConfirmation:
                setResult(RESULT_OK);
                finish();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        finish();
    }

    public void createQrCode(String data) {
        Log.d("Data", URLDecoder.decode(data));
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();

        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(URLDecoder.decode(data), BarcodeFormat.QR_CODE, 800, 800); //boom schaka laka
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder(); //simsalabim
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);//expiliamos
            imgVw.setImageBitmap(bitmap);//stupor
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }
}
