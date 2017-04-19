package at.htlgkr.raiffeisenprojektteam.schuldenapp;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dusch on 19.04.2017.
 */

public class DebtsArchiveAdapter extends ArrayAdapter<Debt>{
    public DebtsArchiveAdapter(Context context, ArrayList<Debt> debtsArchive){
        super(context, 0, debtsArchive);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Debt debt = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_debt_archive, parent, false);
        }

        String creditorDebitorString = "";
        if (debt.isiAmCreditor()) creditorDebitorString = "Kreditor";
        else creditorDebitorString = "Debitor";

        // Lookup view for data population
        TextView debtDate = (TextView) convertView.findViewById(R.id.debtDate);
        TextView debtCreditorOrDebitor = (TextView) convertView.findViewById(R.id.debtCreditorOrDebitor);
        TextView debtName = (TextView) convertView.findViewById(R.id.debtName);
        TextView debtValue = (TextView) convertView.findViewById(R.id.debtValue);

        // Populate the data into the template view using the data object
        debtDate.setText(debt.getDate());
        debtCreditorOrDebitor.setText(creditorDebitorString);
        debtName.setText(debt.getDeptorFirstName() + " " + debt.getDeptorLastName());
        debtValue.setText(debt.getValue() + "");
        // Return the completed view to render on screen
        return convertView;
    }
}
