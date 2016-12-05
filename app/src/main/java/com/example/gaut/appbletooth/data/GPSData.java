package com.example.gaut.appbletooth.data;

/**
 * Created by Arthur Parmelan on 22/11/2016.
 */

import android.location.Location;

import com.example.gaut.appbletooth.interfaces.IGPSData;

public class GPSData implements IGPSData {

    String mMyPosition = "myPosition";
    Location mLocation = new Location(mMyPosition);
    double lat [] = new double[] {48.814330,48.814572,48.814404,48.8117065,48.811738,48.815964,48.818765,48.820904,48.822603,48.825120,48.826253,48.826001,48.825813,48.826190,48.827008,48.827826,48.828393,48.82984,48.831224,48.832734,48.834118,48.835754,48.837075,48.838511,48.839368,48.839777};
    double lon [] = new double[] {2.394717,2.391785,2.390687,2.385454,2.380484,2.377206,2.373889,2.370352,2.367389,2.362323,2.357162,2.353243,2.347795,2.343590,2.338715,2.330400,2.325238,2.321606,2.318452,2.316636,2.314247,2.311284,2.310519,2.311497,2.309782,2.309964};

    boolean isThreadStart = false;

    public void startThread(){
        isThreadStart = true;
        mThread.start();
    }
    public void stopThread(){
        isThreadStart = false;
        try {
            mThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    Thread mThread;

    Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            int maxStep = 25;
            int step = 0;

            while (isThreadStart){
                step++;
                if(step > maxStep){
                    step = maxStep;
                }
                mLocation.setLatitude(lat[step]);
                mLocation.setLongitude(lon[step]);

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };



    public GPSData(){
        mLocation.setLatitude(12);
        mLocation.setLongitude(18);

        mThread = new Thread(mRunnable);
        startThread();
    }

    @Override
    public Location getPosition() {
        return mLocation;
    }

}