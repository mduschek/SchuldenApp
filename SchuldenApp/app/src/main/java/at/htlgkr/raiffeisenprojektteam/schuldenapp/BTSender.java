package at.htlgkr.raiffeisenprojektteam.schuldenapp;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.os.ParcelUuid;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.Set;

/**
 * Created by Perndorfer on 11.02.2017.
 */

public class BTSender extends AppCompatActivity
{
    private static final String TAG = "*=BTSender";
    private ListView btDevices;
    private int pos;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.btsender);
        btDevices = (ListView)findViewById(R.id.btdevices);
        btDevices.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                pos = position;
            }
        });
    }

    public void onSendClick(final View src)
    {
        BluetoothAdapter blueAdapter = BluetoothAdapter.getDefaultAdapter();

        try {
            if (blueAdapter.isEnabled()) {
                Set<BluetoothDevice> bondedDevices = blueAdapter.getBondedDevices();
                //depttype;Michael;Duschek;Usuage;IBAN;30.65;12.12.16
                if (bondedDevices.size() > 0) {
                    Object[] devices = (Object[])  bondedDevices.toArray();

                    //ListDialog
                    ParcelUuid[] uuids = selectedDevice.getUuids();
                    BluetoothSocket socket = selectedDevice.createRfcommSocketToServiceRecord(uuids[0].getUuid());
                    socket.connect();
                    MainActivity.bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                    MainActivity.bw.write(partnerIsCreditor+";"+firstname + ";" + lastname + ";" + usuage + ";" + iban + ";" + value + ";" + sdf.format(date));
                    MainActivity.bw.flush();

                    //SENDER VERBINDET SICH IMMER MIR DEM EMPFÄNGER
                }

                Log.e("error", "No appropriate paired devices.");
            } else {
                Log.e("error", "Bluetooth is disabled.");
            }
        } catch (Exception ex) {
            Log.e(TAG, "Bluetooth: ", ex);
        }
    }
}
