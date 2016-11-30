package at.htlgkr.raiffeisenprojektteam.schuldenapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by Alexander on 21.11.16.
 */

public class CreateLoanActivity extends AppCompatActivity
{
    TextView textViewCreateLoanDescription;
    Button buttonManualInput, buttonBluetooth, buttonNfc, buttonSms, buttonWhatsapp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_create_loan);

        Dept d = (Dept) getIntent().getSerializableExtra("object");

        textViewCreateLoanDescription = (TextView) findViewById(R.id.textViewCreateLoanDescription);

        buttonManualInput = (Button) findViewById(R.id.buttonManualInput);
        buttonBluetooth = (Button) findViewById(R.id.buttonBluetooth);
        buttonNfc = (Button) findViewById(R.id.buttonNfc);
        buttonSms = (Button) findViewById(R.id.buttonSms);
        buttonWhatsapp= (Button) findViewById(R.id.buttonWhatsapp);
    }

    public void onButtonPressed (View source){
        Intent intent;
        switch (source.getId()){
            case R.id.buttonManualInput:
                intent = new Intent(this, DetailActivity.class);
                //intent.putExtra("object", -1);
                startActivity(intent);
                break;
            case R.id.buttonBluetooth:
                //an dieser Stelle Bluetooth aktivieren
                intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
                intent.setType("text/plain");
                startActivity(Intent.createChooser(intent, "Titel"));
                break;
            case R.id.buttonNfc:
                //an dieser Stelle NFC aktivieren
                //intent = new Intent(this,DetailActivity.class);
                //intent.putExtra("object", -1);
                //startActivity(intent);
                break;
            case R.id.buttonSms:
                //intent = new Intent(this,DetailActivity.class);
                //intent.putExtra("object", -1);
                //startActivity(intent);
                break;
            case R.id.buttonWhatsapp:
                //intent = new Intent(this,DetailActivity.class);
                //intent.putExtra("object", -1);
                //startActivity(intent);
                break;
        }
    }
}
