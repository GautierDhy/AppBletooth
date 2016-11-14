package com.example.gaut.appbletooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Button mBluetoothButton;
    Button mScanButton;
    ArrayList<String> mArrayAdapter = new ArrayList<>();
    ArrayList<BluetoothDevice> mArrayDevice = new ArrayList<>();
    ListView mDeviceListView;

    BluetoothAdapter mbluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    private final static int REQUEST_CODE_ENABLE_BLUETOOTH = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mScanButton = (Button) findViewById(R.id.scan_button);
        mBluetoothButton = (Button) findViewById(R.id.bluetooth_button);
        mDeviceListView = (ListView) findViewById(R.id.device_ListView);

        if (mbluetoothAdapter == null)
            Toast.makeText(MainActivity.this, "Pas de Bluetooth",
                    Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(MainActivity.this, "Avec Bluetooth",
                    Toast.LENGTH_SHORT).show();



        mScanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
                filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
                registerReceiver(bluetoothReceiver, filter);
                mbluetoothAdapter.startDiscovery();
            }
        });

        mBluetoothButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (!mbluetoothAdapter.isEnabled()) {
                    Intent enableBlueTooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBlueTooth, REQUEST_CODE_ENABLE_BLUETOOTH);
                }
            }
        });
        mDeviceListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this, "Device = " + mArrayAdapter.get(position), Toast.LENGTH_SHORT).show();
                BluetoothDevice device = mArrayDevice.get(position);


            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode != REQUEST_CODE_ENABLE_BLUETOOTH)
            return;
        if (resultCode == RESULT_OK) {
            // L'utilisation a activé le bluetooth
        } else {
            // L'utilisation n'a pas activé le bluetooth
        }
    }
    
    private final BroadcastReceiver bluetoothReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                Toast.makeText(MainActivity.this, "New Device = " + device.getName(), Toast.LENGTH_SHORT).show();
                mArrayAdapter.add(device.getName() + " " + device.getAddress());
                mArrayDevice.add(device);
            }
            if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<String>(
                        getApplicationContext(),
                        R.layout.simple_list1,
                        mArrayAdapter
                );
                    mDeviceListView.setAdapter(new ArrayAdapter<String>(context,android.R.layout.simple_expandable_list_item_1,mArrayAdapter));
            }
        }

    };


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mbluetoothAdapter.cancelDiscovery();
        unregisterReceiver(bluetoothReceiver);
    }
}
