package at.htlgkr.raiffeisenprojektteam.schuldenapp;

import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private PagerTabStrip tabStrip;
    private CustomPagerAdapter customPagerAdapter;
    private ActionBar actionBar;
    private static boolean isInLandscape;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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

        Log.d("*=", "isinlandscape "+ isInLandscape);

        // region AddTabs
        actionBar.addTab(actionBar.newTab().setText(getResources().getString(R.string.iOwe)).setTabListener(tabListener));
        actionBar.addTab(actionBar.newTab().setText(getResources().getString(R.string.owesMe)).setTabListener(tabListener));
        actionBar.addTab(actionBar.newTab().setText(getResources().getString(R.string.details)).setTabListener(tabListener));
        // endregion
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
}
