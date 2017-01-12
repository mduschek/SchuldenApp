package at.htlgkr.raiffeisenprojektteam.schuldenapp;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

@Deprecated
public class DetailFragment extends Fragment
{
    private View view;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_create_loan, container,false);
        Log.d("*=", "onCreateView: Detail Fragment");
        return view;
    }
}
