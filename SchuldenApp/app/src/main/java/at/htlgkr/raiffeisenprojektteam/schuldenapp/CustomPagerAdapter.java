package at.htlgkr.raiffeisenprojektteam.schuldenapp;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;
import android.widget.Toast;

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
        Fragment fragment;
        if (i<=1) {
            fragment = new DeptListFragment();
            Log.e("*=", "getItem: "+i );
        }else
        {
            Log.w("*=", "getItem: "+i );
            fragment = new DetailFragment();
        }
        Bundle args = new Bundle();
        // Our object is just a boolean :-P
        switch (i) {
            case 0:
                args.putBoolean("showMyDepts", true);
                break;
            case 1:
                args.putBoolean("showMyDepts", false);
                break;

        }
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
