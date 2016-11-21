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
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;


public class MainActivity extends AppCompatActivity {

    BluetoothSocket mBluetoothSocket = null;
    BluetoothDevice Connectiondevice = null;
    Button mBluetoothButton;
    Button mScanButton;
    ArrayList<String> mArrayAdapter = new ArrayList<>();
    ArrayList<BluetoothDevice> mArrayDevice = new ArrayList<>();
    ListView mDeviceListView;
    BluetoothAdapter mbluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

    private final static int REQUEST_CODE_ENABLE_BLUETOOTH = 0;
    private ConnectThread mConnectThread;

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
                BluetoothDevice Connectiondevice = mArrayDevice.get(position);

                mConnectThread = new ConnectThread(Connectiondevice);
                mConnectThread.start();
            }
        });

        // Liste des appareils déjà connéctés
        // et si ils le sot les ajoutés à la liste des devices

        Set<BluetoothDevice> pairedDevices =  mbluetoothAdapter.getBondedDevices();
        if (pairedDevices.size()>0){
            findViewById(R.id.activity_main).setVisibility(View.VISIBLE);
            for (BluetoothDevice Itemdevices : pairedDevices){
                mArrayAdapter.add(Itemdevices.getName() + "\n" + Itemdevices.getAddress());
                mArrayDevice.add(Itemdevices);
            }
        }
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
                BluetoothDevice Itemdevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (Itemdevice.getBondState() != BluetoothDevice.BOND_BONDED) {
                Toast.makeText(MainActivity.this, "New Device = " + Itemdevice.getName(), Toast.LENGTH_SHORT).show();
                mArrayAdapter.add(Itemdevice.getName() + " " + Itemdevice.getAddress());
                mArrayDevice.add(Itemdevice);
            }
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

   public class ConnectThread extends Thread {

       private final BluetoothDevice mmDevice;
       private final BluetoothSocket mmSocket;

       public ConnectThread(BluetoothDevice Connectiondevice){

           BluetoothSocket tmp = null;
           mmDevice = Connectiondevice;
           try {
               tmp = Connectiondevice.createRfcommSocketToServiceRecord(UUID.randomUUID());
           } catch (IOException e){}
           mmSocket = tmp;
    }
     public void run(){
     mbluetoothAdapter.cancelDiscovery();
         try {
             mmSocket.connect();
         } catch (IOException connectException) {
             try {
                 mmSocket.close();
             } catch (IOException closeException) {
             }
             return;
         }

   }
    public void cancel(){

        try {
            mmSocket.close();
        } catch (IOException e){}

    }
   }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mbluetoothAdapter.cancelDiscovery();
        unregisterReceiver(bluetoothReceiver);
    }
}
