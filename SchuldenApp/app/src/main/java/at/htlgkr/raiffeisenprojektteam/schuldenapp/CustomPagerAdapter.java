package at.htlgkr.raiffeisenprojektteam.schuldenapp;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by Alexander on 09.11.16.
 */

class CustomPagerAdapter extends FragmentPagerAdapter {

    Context mContext;

    public CustomPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {

        // Create fragment object
        Fragment fragment = new DemoFragment();

        // Attach some data to the fragment
        // that we'll use to populate our fragment layouts
        Bundle args = new Bundle();
        args.putInt("page_position", position + 1);

        // Set the arguments on the fragment
        // that will be fetched in the
        // DemoFragment@onCreateView
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "Page " + (position + 1);
    }
}
