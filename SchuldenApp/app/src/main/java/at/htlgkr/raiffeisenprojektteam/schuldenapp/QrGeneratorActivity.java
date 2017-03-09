package at.htlgkr.raiffeisenprojektteam.schuldenapp;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.net.URLDecoder;

public class QrGeneratorActivity extends AppCompatActivity {

    ImageView imgVw;

    RadioButton radioButtonCreateShareQrCode, onRadioButtonCreateStuzzaQrCode;
    String shareData;
    String stuzzaData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_scanner);

        imgVw = (ImageView) findViewById(R.id.imgView);

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            shareData = extras.getString("shareData");

            stuzzaData = extras.getString("stuzzaData");
        }

        radioButtonCreateShareQrCode = (RadioButton) findViewById(R.id.radioButtonCreateShareQrCode);
        //onRadioButtonCreateStuzzaQrCode = (RadioButton) findViewById(R.id.onRadioButtonCreateStuzzaQrCode);

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
