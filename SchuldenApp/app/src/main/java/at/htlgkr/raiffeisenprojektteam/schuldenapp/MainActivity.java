package at.htlgkr.raiffeisenprojektteam.schuldenapp;

import android.support.v4.view.PagerTitleStrip;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private PagerTitleStrip titleStrip;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        titleStrip = (PagerTitleStrip) findViewById(R.id.pagerTitleStrip);
    }
}
