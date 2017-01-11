package at.htlgkr.raiffeisenprojektteam.schuldenapp;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class QrGeneratorActivity extends AppCompatActivity {

    ImageView imgVw;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_scanner);

        imgVw= (ImageView) findViewById(R.id.imgView);

        Bundle extras = getIntent().getExtras();
        String data="";

        if (extras != null) {
            data = extras.getString("qr");
        }

        MultiFormatWriter multiFormatWriter=new MultiFormatWriter();

        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(data, BarcodeFormat.QR_CODE,800,800); //boom schaka laka
            BarcodeEncoder barcodeEncoder=new BarcodeEncoder(); //simsalabim
            Bitmap bitmap=barcodeEncoder.createBitmap(bitMatrix);//expiliamos
            imgVw.setImageBitmap(bitmap);//stupor
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }
}
