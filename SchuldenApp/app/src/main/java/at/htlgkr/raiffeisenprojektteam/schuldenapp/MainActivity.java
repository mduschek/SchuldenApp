package at.htlgkr.raiffeisenprojektteam.schuldenapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.nfc.NfcAdapter;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    private final DeptsDbHelper dbHelper = new DeptsDbHelper(this);
    public static SQLiteDatabase db;
    private String TAG = "*=";
    private ViewPager viewPager;
    private PagerTabStrip tabStrip;
    private CustomPagerAdapter customPagerAdapter;
    private ActionBar actionBar;
    private static boolean isInLandscape;
    public static boolean nfcIsAvailable = false;
    public static NfcAdapter nfcAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = dbHelper.getWritableDatabase();
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (nfcAdapter!= null)
        {
            nfcIsAvailable = true;

            nfcAdapter.setNdefPushMessageCallback(this, this);
            nfcAdapter.setOnNdefPushCompleteCallback(this, this);
        }
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        UserData.firstname = sharedPreferences.getString("pref_userdata_firstname",null);
        UserData.lastname = sharedPreferences.getString("pref_userdata_lastname",null);
        UserData.iban = sharedPreferences.getString("pref_userdata_iban",null);

        //region incoming intent from deeplinking
        Intent intent = getIntent();
        Log.e(TAG, "onCreate:");
        if(intent.getData()!=null)
        {
            String params = intent.getData().getQueryParameter("content");
            String split [] = params.split(";");
        }

        //endregion

        //region pager
        actionBar = getSupportActionBar();
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        customPagerAdapter = new CustomPagerAdapter(getSupportFragmentManager(),this);
        viewPager.setAdapter(customPagerAdapter);
        viewPager.addOnPageChangeListener(getOnPageChangedListener());
        viewPager.setPageTransformer(true, new ZoomOutPageTransformer());

        ActionBar.TabListener tabListener = getTabListener(actionBar);
        // Specify that tabs should be displayed in the action bar.
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        // Create a tab listener that is called when the user changes tabs.

        switch (getResources().getConfiguration().orientation)
        {
            case Configuration.ORIENTATION_LANDSCAPE: isInLandscape=true; break;
            case Configuration.ORIENTATION_PORTRAIT: isInLandscape=false; break;
        }


        actionBar.addTab(actionBar.newTab().setText(getResources().getString(R.string.iOwe)).setTabListener(tabListener));
        actionBar.addTab(actionBar.newTab().setText(getResources().getString(R.string.owesMe)).setTabListener(tabListener));
        //actionBar.addTab(actionBar.newTab().setText(getResources().getString(R.string.details)).setTabListener(tabListener));
        // endregion
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option_menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Intent intent;
        switch (id){
            case R.id.option_menu_new_entry:
                intent = new Intent(this,CreateLoanActivity.class);
                //intent.putExtra("object", -1);
                startActivity(intent);
                return true;
            //case R.id.option_menu_userdata:
            //    return true;
            case R.id.option_menu_preferences:
                intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private ActionBar.TabListener getTabListener(final ActionBar actionBar)
    {
        return new ActionBar.TabListener() {
            public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
                Log.w("*=", "" + actionBar.getSelectedNavigationIndex());
                changeTab(tab.getPosition());
            }

            public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
                // hide the given tab
            }

            public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
                // probably ignore this event
            }
        };
    }


    public void changeTab(int position)
    {
        viewPager.setCurrentItem(position);
    }

    public ViewPager.OnPageChangeListener getOnPageChangedListener() {
        //region OnPageChangedListener
        return new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
            {
            }

            @Override
            public void onPageSelected(int position) {
                Log.d("*=", "onPageSelected: "+position);
                actionBar.setSelectedNavigationItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        };
        //endregion
    }

    public void getIntentData(){
        Uri data = getIntent().getData();
        String strData = data.toString();
        //if (strScreenName.equals("com.your_package.something://"))  {
            // THIS IS OPTIONAL IN CASE YOU NEED TO VERIFY. THE ACTUAL USAGE IN MY APP IS BELOW THIS BLOCK
        //}
    }
}
