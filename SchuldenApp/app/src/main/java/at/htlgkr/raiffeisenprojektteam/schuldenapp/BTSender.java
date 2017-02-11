package at.htlgkr.raiffeisenprojektteam.schuldenapp;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.ParcelUuid;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Created by Perndorfer on 11.02.2017.
 */

public class BTSender extends AppCompatActivity {
    private static final String TAG = "*=BTSender";
    private ListView btDevices;
    private int pos;
    private String date, firstname, lastname, iban, usuage, value;
    private boolean partnerIsCreditor;
    private BluetoothAdapter blueAdapter = BluetoothAdapter.getDefaultAdapter();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.btsender);
        LinkedList<String> btdList = new LinkedList<>();
        Set<BluetoothDevice> bondedDevices = blueAdapter.getBondedDevices();
        if (bondedDevices.size()<1)
        {
            Toast.makeText(this,"Keine gepaarten Geräte vorhanden!",Toast.LENGTH_LONG).show();
            finish();
        }

        for (BluetoothDevice bd : bondedDevices) {
            btdList.add(bd.getName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_single_choice, btdList);
        btDevices = (ListView) findViewById(R.id.btdevices);
        btDevices.setAdapter(adapter);

        Intent i = getIntent();
        date = i.getStringExtra("date");
        firstname = i.getStringExtra("firstname");
        lastname = i.getStringExtra("lastname");
        usuage = i.getStringExtra("usuage");
        iban = i.getStringExtra("iban");
        value = i.getStringExtra("value");
        partnerIsCreditor = i.getBooleanExtra("partneriscreditor", false);

        Log.w(TAG, firstname + lastname + usuage + iban + value + partnerIsCreditor);
        btDevices.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                pos = position;
            }
        });
    }

    public void onSendClick(final View src) {
        if (blueAdapter != null)
        {
            if (blueAdapter.isEnabled())
            {
                try {
                    Object[] devices = (Object[]) blueAdapter.getBondedDevices().toArray();
                    BluetoothDevice device = (BluetoothDevice) devices[pos];
                    Log.w(TAG,device.getName());
                    ParcelUuid[] uuids = device.getUuids();
                    BluetoothSocket socket = device.createRfcommSocketToServiceRecord(uuids[0].getUuid());
                    socket.connect();
                    MainActivity.bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                    MainActivity.bw.write(partnerIsCreditor + ";" + firstname + ";" + lastname + ";" + usuage + ";" + iban + ";" + value + ";" + date);
                    MainActivity.bw.flush();
                } catch (Exception ex) {
                    Log.e(TAG, "onSendClick: ", ex);
                }
            } else {
                Log.e("error", "Bluetooth is disabled.");
            }
        }
    }
}
