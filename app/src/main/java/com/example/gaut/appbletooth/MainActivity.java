package com.example.gaut.appbletooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static java.security.AccessController.getContext;

public class MainActivity extends AppCompatActivity {

    Button mBluetoothButton;
    Button mScanButton;
    ArrayList<String> mArrayAdapter = new ArrayList<>();
    ListView mDeviceListView;
    ImageView mAnimImageView;
    BluetoothAdapter mbluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    private final static int REQUEST_CODE_ENABLE_BLUETOOTH = 0;
    int nextBitmap = 0;
    int stopBitmap = 0;

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

        mAnimImageView = (ImageView) findViewById(R.id.iv_animation);

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


            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (stopBitmap == 1){
                        mAnimImageView.setImageBitmap(null);
                    }
                    else {
                        mAnimImageView.setImageBitmap(GifFrames.getGifs(getApplicationContext(),nextBitmap));
                        nextBitmap++;
                        handler.postDelayed(this,200);
                    }
                }
            },0);

            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                Toast.makeText(MainActivity.this, "New Device = " + device.getName(), Toast.LENGTH_SHORT).show();
                mArrayAdapter.add(device.getName() + " " + device.getAddress());
                stopBitmap = 0;
            }
            if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<String>(
                        getApplicationContext(),
                        R.layout.simple_list1,
                        mArrayAdapter
                );
                mDeviceListView.setAdapter(stringArrayAdapter);
                stopBitmap = 1;
            }
        }
    };

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
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mbluetoothAdapter.cancelDiscovery();
        if(bluetoothReceiver != null) {
           // unregisterReceiver(bluetoothReceiver);
        }
    }
}
