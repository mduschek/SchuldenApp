package at.htlgkr.raiffeisenprojektteam.schuldenapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by michael on 24.11.16.
 */

public class DetailActivity extends AppCompatActivity
{
    EditText iban, firstname, lastname, value,usuage;
    Button btnOk;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_detail_create);
        Dept d = (Dept) getIntent().getSerializableExtra("object");

        iban = (EditText) findViewById(R.id.editTextIban);
        firstname = (EditText) findViewById(R.id.editTextFirstName);
        lastname = (EditText) findViewById(R.id.editTextLastName);
        value = (EditText) findViewById(R.id.editTextSum);
        usuage = (EditText) findViewById(R.id.editTextUsuage);

        btnOk = (Button) findViewById(R.id.btnOk);
    }
}
