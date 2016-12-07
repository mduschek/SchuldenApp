package at.htlgkr.raiffeisenprojektteam.schuldenapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
        buttonWhatsapp= (Button) findViewById(R.id.buttonOther);
    }

    public void onButtonPressed (View source){
        Intent intent;
        switch (source.getId()){
            case R.id.buttonManualInput:
<<<<<<< HEAD
                intent = new Intent(this, DetailActivity.class);
=======
                intent = new Intent(this,DetailActivity.class);
>>>>>>> 7d89e901307e46ae52a032e0879e5b9be7d4785f
                //intent.putExtra("object", -1);
                startActivity(intent);
                break;
            case R.id.buttonBluetooth:
<<<<<<< HEAD
                intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT, "test msg");
                intent.setType("text/plain");

                startActivity(Intent.createChooser(intent, "Titel"));
=======
                /*Uri bluetooth = Uri.parse("URL");  //URL parsen
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, bluetooth);
                sendIntent.setType("text/plain");
                startActivity((sendIntent, "Titel"));*/
>>>>>>> 7d89e901307e46ae52a032e0879e5b9be7d4785f
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
            case R.id.buttonOther:
                Uri adress = Uri.parse("URL");  //URL parsen
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, adress);
                sendIntent.setType("text/plain");
                startActivity(Intent.createChooser(sendIntent, "Titel"));
                break;
        }
    }
}
