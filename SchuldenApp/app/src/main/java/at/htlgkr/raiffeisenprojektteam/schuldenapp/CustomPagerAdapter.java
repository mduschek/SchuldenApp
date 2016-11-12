package at.htlgkr.raiffeisenprojektteam.schuldenapp;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by Alexander on 09.11.16.
 */

class CustomPagerAdapter extends FragmentStatePagerAdapter {

    Context mContext;

    public CustomPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int i) {
        Fragment fragment = new DeptsFragment();
        Bundle args = new Bundle();
        // Our object is just an integer :-P
        if (i==0)
        {
            args.putBoolean("showMyDepts",true);
        }else
        {
            args.putBoolean("showMyDepts",false);
        }
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "Page " + (position + 1);
    }
}
