package at.htlgkr.raiffeisenprojektteam.schuldenapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Alexander on 21.11.16.
 */

public class CreateOrDetailActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_create_or_detail);
        Dept d = (Dept) getIntent().getSerializableExtra("object");

    }
}
