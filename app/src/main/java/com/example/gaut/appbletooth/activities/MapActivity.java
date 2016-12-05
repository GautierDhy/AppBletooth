package com.example.gaut.appbletooth.activities;

import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.gaut.appbletooth.R;
import com.example.gaut.appbletooth.data.GPSData;
import com.example.gaut.appbletooth.interfaces.IGPSData;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Random;

/**
 * Created by Arthur Parmelan on 22/11/2016.
 */
public class MapActivity extends FragmentActivity implements OnMapReadyCallback{
    GoogleMap mMap;
    Handler mHandler;
    public int i=0;
    public int j=0;
    Random rand = new Random();
    LatLng prevPosition = new LatLng (0,0);
    //------DATA
    IGPSData myPositionGPSData;

    Button ViewButton;

    Marker mPosition;
    Marker mPollution;
    MarkerOptions mPositionMarker = new MarkerOptions().title("MyPosition");
    MarkerOptions mPollutionMarker = new MarkerOptions().alpha(0.70F);
    Runnable mUIRunnable = new Runnable() {


        @Override
        public void run() {

            Location location = myPositionGPSData.getPosition();

            LatLng myPosition = new LatLng(location.getLatitude(), location.getLongitude());

            if (mMap != null) {

                if (mPosition != null) {
                    mPosition.remove();
                }

                if (i==0) {
                    CameraUpdate camera_position = CameraUpdateFactory.newLatLngZoom(myPosition, 13);
                    mMap.moveCamera(camera_position);
                    mMap.animateCamera(camera_position);
                    i=1;
                }
                if (i>0) {
                    int n = rand.nextInt(4);
                    mPollutionMarker.position(prevPosition);
                    if (n ==0){
                        mPollutionMarker.icon(BitmapDescriptorFactory.fromResource(R.mipmap.pol_normal));
                        mPollutionMarker.title("Pas ou très peu de pollution (0)");
                    }
                    else if (n == 1){
                        mPollutionMarker.icon(BitmapDescriptorFactory.fromResource(R.mipmap.pol_level1));
                        mPollutionMarker.title("Peu de pollution (1)");
                    }
                    else if (n == 2){
                        mPollutionMarker.icon(BitmapDescriptorFactory.fromResource(R.mipmap.pol_level2));
                        mPollutionMarker.title("Environnement pollué(2)");
                    }
                    else if (n == 3){
                        mPollutionMarker.icon(BitmapDescriptorFactory.fromResource(R.mipmap.pol_level3));
                        mPollutionMarker.title("Environnement très pollué(3)");
                    }

                    mPollution = mMap.addMarker(mPollutionMarker);
                }

                mPositionMarker.position(myPosition);
                mPositionMarker.icon(BitmapDescriptorFactory.fromResource(R.mipmap.tim_head));

                // Add a marker in Sydney and move the camera
                mPosition = mMap.addMarker(mPositionMarker);
               // mMap.moveCamera(CameraUpdateFactory.newLatLng(myPosition));

            }

            prevPosition = new LatLng(location.getLatitude(),location.getLongitude());

            mHandler.postDelayed(this, 1000);
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_maps);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);
        mHandler = new Handler(Looper.getMainLooper());

        ViewButton = (Button) findViewById(R.id.button_view);

        ViewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (j==0){
                    j = 1;
                    mMap.setMapType(mMap.MAP_TYPE_SATELLITE);
                    Toast.makeText(MapActivity.this, "Vue Satellite", Toast.LENGTH_SHORT).show();
                }
                else {
                    mMap.setMapType(mMap.MAP_TYPE_NORMAL);
                    Toast.makeText(MapActivity.this, "Vue Route", Toast.LENGTH_SHORT).show();
                    j=0;
                }
            }
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.mMap = googleMap;

        //Start Walking guy
        myPositionGPSData = new GPSData();

        mHandler.post(mUIRunnable);
    }
}
