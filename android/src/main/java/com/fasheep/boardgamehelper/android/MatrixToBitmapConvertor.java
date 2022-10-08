package com.fasheep.boardgamehelper.android;

import android.graphics.Bitmap;
import android.graphics.Color;
import com.google.zxing.common.BitMatrix;

public class MatrixToBitmapConvertor {
    public Bitmap convert(BitMatrix matrix) {
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        System.out.println(width + " " + height);
        int[] pixels = new int[width * height];
        for (int y = 0; y < height; y++) {
            int offset = y * width;
            for (int x = 0; x < width; x++) {
                pixels[offset + x] = matrix.get(x, y) ? Color.BLACK : Color.WHITE;
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);// 可能会太占内存
        bitmap.setPixels(pixels, 0, 300, 0, 0, width, height);
        return bitmap;
    }
}
