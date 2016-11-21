package com.example.gaut.appbletooth;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Arthur Parmelan on 14/11/2016.
 */

public class GifFrames {

    List<Integer> gifs = new ArrayList<>();

    public GifFrames(){
        gifs.add(R.drawable.frame_0_delay08s);
        gifs.add(R.drawable.frame_1_delay08s);
        gifs.add(R.drawable.frame_2_delay08s);
        gifs.add(R.drawable.frame_3_delay08s);
        gifs.add(R.drawable.frame_4_delay08s);
        gifs.add(R.drawable.frame_5_delay08s);
        gifs.add(R.drawable.frame_6_delay08s);
        gifs.add(R.drawable.frame_7_delay08s);
        gifs.add(R.drawable.frame_8_delay08s);
        gifs.add(R.drawable.frame_9_delay08s);
        gifs.add(R.drawable.frame_10_delay08s);
        gifs.add(R.drawable.frame_11_delay08s);
        gifs.add(R.drawable.frame_12_delay08s);
        gifs.add(R.drawable.frame_13_delay08s);
        gifs.add(R.drawable.frame_14_delay08s);
        gifs.add(R.drawable.frame_15_delay08s);
        gifs.add(R.drawable.frame_16_delay08s);
        gifs.add(R.drawable.frame_17_delay08s);
        gifs.add(R.drawable.frame_18_delay08s);
        gifs.add(R.drawable.frame_19_delay08s);
        gifs.add(R.drawable.frame_20_delay08s);
        gifs.add(R.drawable.frame_21_delay08s);
        gifs.add(R.drawable.frame_22_delay08s);
        gifs.add(R.drawable.frame_23_delay08s);
        gifs.add(R.drawable.frame_24_delay08s);
        gifs.add(R.drawable.frame_25_delay08s);
        gifs.add(R.drawable.frame_26_delay08s);
        gifs.add(R.drawable.frame_27_delay08s);
        gifs.add(R.drawable.frame_28_delay08s);
        gifs.add(R.drawable.frame_29_delay08s);
        gifs.add(R.drawable.frame_30_delay08s);
        gifs.add(R.drawable.frame_31_delay08s);
        gifs.add(R.drawable.frame_32_delay08s);
        gifs.add(R.drawable.frame_33_delay08s);
        gifs.add(R.drawable.frame_34_delay08s);
        gifs.add(R.drawable.frame_35_delay08s);
        gifs.add(R.drawable.frame_36_delay08s);
        gifs.add(R.drawable.frame_37_delay08s);
        gifs.add(R.drawable.frame_38_delay08s);
        gifs.add(R.drawable.frame_39_delay08s);
        gifs.add(R.drawable.frame_40_delay08s);
        gifs.add(R.drawable.frame_41_delay08s);
        gifs.add(R.drawable.frame_42_delay08s);
        gifs.add(R.drawable.frame_43_delay08s);
        gifs.add(R.drawable.frame_44_delay08s);
        gifs.add(R.drawable.frame_45_delay08s);
        gifs.add(R.drawable.frame_46_delay08s);
        gifs.add(R.drawable.frame_47_delay08s);


    }

    public Bitmap getGifs(Context ctx, int value) {

        int realValue = value%48;
        int arrayNumber = realValue/10;
        int indice = realValue%10;

        if (value == 48){
            if (indice > 8) {
                indice = 8;
            }
        }
        Bitmap b = BitmapFactory.decodeResource(ctx.getResources(),gifs.get(realValue));

        return b;
    }


}
