package com.example.yg.qrgeneratordemo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.net.URLDecoder;

public class MainActivity extends AppCompatActivity {
    Button generate;
    Button scanner;
    EditText editTextMsg;
    ImageView imgVw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Activity activity=this;

        imgVw= (ImageView) findViewById(R.id.imgView);
        editTextMsg =(EditText) findViewById(R.id.editTextMsg);

        generate= (Button) findViewById(R.id.btnGnrt);
        generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String qrCodeString= editTextMsg.getText().toString(); //i moch a qr code aus euch muahahahahaahaha

                MultiFormatWriter multiFormatWriter=new MultiFormatWriter();
                try {
                    BitMatrix bitMatrix = multiFormatWriter.encode(URLDecoder.decode(qrCodeString), BarcodeFormat.QR_CODE,200,200); //boom schaka laka
                    BarcodeEncoder barcodeEncoder=new BarcodeEncoder(); //simsalabim
                    Bitmap bitmap=barcodeEncoder.createBitmap(bitMatrix);//expiliamos
                    imgVw.setImageBitmap(bitmap);//stupor
                } catch (WriterException e) {
                    e.printStackTrace();
                }
            }
        });

        scanner= (Button) findViewById(R.id.btnScnr);
        scanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator integrator=new IntentIntegrator(activity);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                integrator.setPrompt("Scan");
                integrator.setCameraId(0);
                integrator.setBeepEnabled(false);
                integrator.setBarcodeImageEnabled(false);
                integrator.initiateScan();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result=IntentIntegrator.parseActivityResult(requestCode,resultCode,data);

        if(result==null) {
            super.onActivityResult(requestCode, resultCode, data);
        }
        else{
            if (result.getContents()==null){
                Toast.makeText(this,"Cancelled",Toast.LENGTH_LONG).show();
            }else{
                //Toast.makeText(this,result.getContents(),Toast.LENGTH_LONG).show();
                editTextMsg.setText(result.getContents());
            }
        }
    }
}
    //muahaha jetzt satz es a qrcode muahahaha

        //es tuad ma so lad mir is schreklich fad
        // i hof ds funtzt nämlich i hob ka vm in polen und ds internet is scheiss longsom do oben oiso i hof dass ds gehd
        //ps fois es frogen zu dem code hobts frogts mi nd i hod ds nämlich vo dem yt-video: https://www.youtube.com/watch?v=-vWHbCr_OWM
        //viel spaß :)