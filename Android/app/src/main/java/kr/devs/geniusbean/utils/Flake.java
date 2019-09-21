package kr.devs.geniusbean.utils;

import java.util.HashMap;

import android.graphics.Bitmap;
import android.util.Log;

public class Flake {

    float x, y;
    float rotation;
    float speed;
    float rotationSpeed;
    int width, height;
    Bitmap bitmap;

    static HashMap<Integer, Bitmap> bitmapMap = new HashMap<Integer, Bitmap>();

    static Flake createFlake(float xRange, Bitmap originalBitmap) {
        Flake flake = new Flake();
        flake.width = (int)(25 + Math.random()*25);
        float hwRatio = (float)originalBitmap.getHeight() / (float)originalBitmap.getWidth();
        flake.height = (int)(flake.width * hwRatio);

        flake.x = (float)Math.random() * (xRange - flake.width);
        flake.y = 0 - (flake.height + (float)Math.random() * flake.height);
        flake.speed = 35 + (float) Math.random() * 170;

        flake.rotation = (float) Math.random() * 180 - 90;
        flake.rotationSpeed = (float) Math.random() * 90 - 45;

        flake.bitmap = bitmapMap.get(flake.width);
        if (flake.bitmap == null) {
            flake.bitmap = Bitmap.createScaledBitmap(originalBitmap,
                    (int)flake.width, (int)flake.height, true);
            bitmapMap.put(flake.width, flake.bitmap);
        }
        return flake;
    }
}

