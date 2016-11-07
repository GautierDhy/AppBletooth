package com.example.gaut.appbletooth;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button mBluetoothButton;
    Button mScanButton;

    BluetoothAdapter mbluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    private final static int REQUEST_CODE_ENABLE_BLUETOOTH = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mScanButton = (Button) findViewById(R.id.scan_button);
        mBluetoothButton = (Button) findViewById(R.id.bluetooth_button);

        if (mbluetoothAdapter == null)
            Toast.makeText(MainActivity.this, "Pas de Bluetooth",
                    Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(MainActivity.this, "Avec Bluetooth",
                    Toast.LENGTH_SHORT).show();



        mScanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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

}
