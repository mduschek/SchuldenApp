package com.example.yg.qrgeneratordemo;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class MainActivity extends AppCompatActivity {
    Button generate;
    ImageView imgVw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        imgVw= (ImageView) findViewById(R.id.imgView);

        generate= (Button) findViewById(R.id.btnGnrt);
        generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String demo="Alex, Michi und Flo"; //i moch a qr code aus euch muahahahahaahaha
                MultiFormatWriter multiFormatWriter=new MultiFormatWriter();

                try {
                    BitMatrix bitMatrix = multiFormatWriter.encode(demo, BarcodeFormat.QR_CODE,200,200); //boom schaka laka
                    BarcodeEncoder barcodeEncoder=new BarcodeEncoder(); //simsalabim
                    Bitmap bitmap=barcodeEncoder.createBitmap(bitMatrix);//expiliamos
                    imgVw.setImageBitmap(bitmap);//stupor
                } catch (WriterException e) {
                    e.printStackTrace();
                }
            }
        });

        //muahaha jetzt satz es a qrcode muahahaha

        //es tuad ma so lad mir is schreklich fad
        //hokus pokus
        // i hof ds funtzt nälich i hob ka vm in polen und ds internet is scheiss longsom do oben oiso i hof das ds gehd
        //ps fois es frogen zu dem code hobts frogts mi nd i hod ds nämlich vo dem yt-video: https://www.youtube.com/watch?v=-vWHbCr_OWM
        //viel spaß :)

    }

}
