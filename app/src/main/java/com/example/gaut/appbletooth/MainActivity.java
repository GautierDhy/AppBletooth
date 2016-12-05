package com.example.gaut.appbletooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.gaut.appbletooth.activities.MapActivity;
import com.example.gaut.appbletooth.data.GPSData;
import com.example.gaut.appbletooth.interfaces.IGPSData;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static java.security.AccessController.getContext;
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
    ImageView mAnimImageView;
    private final static int REQUEST_CODE_ENABLE_BLUETOOTH = 0;
    int nextBitmap = 0;
    int stopBitmap = 0;
    BluetoothAdapter mbluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

    private ConnectThread mConnectThread;


    //---------------------------------------------------------------
    Handler mHandler;
    Button mMapActivityButton;
    TextView mPositionGPSTextView;

    IGPSData myPositionGPSData;

    Runnable mUIRunnable = new Runnable() {
        @Override
        public void run() {

            Location location = myPositionGPSData.getPosition();

            mPositionGPSTextView.setText(" ");

            mHandler.postDelayed(this,1000);
        }
    };

//---------------------------------------------------------------



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
                Toast.makeText(MainActivity.this, "Scan en cours", Toast.LENGTH_LONG).show();
            }
        });

        mBluetoothButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (!mbluetoothAdapter.isEnabled()) {
                    Intent enableBlueTooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBlueTooth, REQUEST_CODE_ENABLE_BLUETOOTH);
                }
                else{
                    Toast.makeText(MainActivity.this, "Bluetooth déjà activé", Toast.LENGTH_SHORT).show();;
                }
            }
        });

        mAnimImageView = (ImageView) findViewById(R.id.iv_animation);

        mDeviceListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this, "Device = " + mArrayAdapter.get(position), Toast.LENGTH_SHORT).show();
                BluetoothDevice Connectiondevice = mArrayDevice.get(position);

                mConnectThread = new ConnectThread(Connectiondevice);
                mConnectThread.start();
            }
        });

<<<<<<< HEAD
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
=======
//---------------------------------------------------------------
        mPositionGPSTextView = (TextView) findViewById(R.id.gps_position_textview);
        mMapActivityButton = (Button) findViewById(R.id.map_activity_button);

        mMapActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent mapActivityIntent = new Intent(getApplicationContext(), MapActivity.class);
                startActivity(mapActivityIntent);
            }
        });


        myPositionGPSData = new GPSData();

        mHandler = new Handler(Looper.getMainLooper());

//---------------------------------------------------------------
>>>>>>> parent of e130f59... Revert "Projet final"
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

          /*  final GifFrames gifFrames = new GifFrames();
            final Handler handler = new Handler();
            stopBitmap = 0;
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {


                    if (stopBitmap == 1){
                        mAnimImageView.setImageBitmap(null);
                    }
                    else {
                        mAnimImageView.setImageBitmap(gifFrames.getGifs(getApplicationContext(),nextBitmap));
                        nextBitmap++;
                        handler.postDelayed(this,100);
                    }


                }
            },100);*/

            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                stopBitmap = 0;
                BluetoothDevice Itemdevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (Itemdevice.getBondState() != BluetoothDevice.BOND_BONDED) {
                Toast.makeText(MainActivity.this, "New Device = " + Itemdevice.getName(), Toast.LENGTH_SHORT).show();
                mArrayAdapter.add(Itemdevice.getName() + " " + Itemdevice.getAddress());
                mArrayDevice.add(Itemdevice);

            }
            if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<String>(
                        getApplicationContext(),
                        R.layout.simple_list1,
                        mArrayAdapter
                );
                stopBitmap = 1;
<<<<<<< HEAD
                    mDeviceListView.setAdapter(new ArrayAdapter<String>(context,android.R.layout.simple_expandable_list_item_1,mArrayAdapter));

=======
                    mDeviceListView.setAdapter(new ArrayAdapter<>(context, android.R.layout.simple_expandable_list_item_1, mArrayAdapter));
                Toast.makeText(MainActivity.this, "Scan fini", Toast.LENGTH_LONG).show();
>>>>>>> parent of e130f59... Revert "Projet final"
            }

        }

    }

   class ConnectThread extends Thread {

<<<<<<< HEAD
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
=======
    @Override
    protected void onResume() {
        super.onResume();

        mHandler.post(mUIRunnable);
    }
>>>>>>> parent of e130f59... Revert "Projet final"

    @Override
    protected void onStart() {
        super.onStart();

    }

    private void checkIfAnimationDone(final AnimationDrawable anim){
        final AnimationDrawable a = anim;
        int timeBetweenChecks = 100;
        Handler h = new Handler();
        h.postDelayed(new Runnable(){
            public void run(){
                if (a.getCurrent() != a.getFrame(a.getNumberOfFrames() - 1)){
                    checkIfAnimationDone(a);
                } else{

                    mAnimImageView.setBackgroundDrawable(null);
                    mAnimImageView.post(new Runnable() {
                        @Override
                        public void run() {
                            AnimationDrawable frameAnimation = new AnimationDrawable();
                            Random g = new Random();


                            mAnimImageView.setBackgroundDrawable(frameAnimation);

                            frameAnimation.start();
                            checkIfAnimationDone(frameAnimation);
                        }
                    });
                }
            }
        }, timeBetweenChecks);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mbluetoothAdapter.cancelDiscovery();
        if(bluetoothReceiver != null) {
           // unregisterReceiver(bluetoothReceiver);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

}
