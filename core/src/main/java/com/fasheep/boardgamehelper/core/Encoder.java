package com.fasheep.boardgamehelper.core;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.util.Hashtable;
import java.util.Map;

public class Encoder {
    private final String TYPE;

    public Encoder(EncoderType format) {
        switch (format) {
            case GBK:
                TYPE = "GBK";
                break;
            case UTF8:
            default:
                TYPE = "UTF-8";
        }
    }

    public void getBitmap(String targetText) throws WriterException {


//        System.out.println(result.toString());
    }

    public BitMatrix getMatrix(String targetText) throws WriterException {
        Map<EncodeHintType, Object> hints = new Hashtable<>();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        hints.put(EncodeHintType.MARGIN, 1);
//        String content;
        return new MultiFormatWriter().encode(targetText, BarcodeFormat.QR_CODE, 300, 300, hints);



/*        Hashtable<EncodeHintType, Object> hints = new Hashtable<>();
        hints.put(EncodeHintType.CHARACTER_SET, TYPE);
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        return multiFormatWriter.encode(targetText, BarcodeFormat.QR_CODE, 200, 200, hints);*/
//        MatrixToImageWriter.writeToPath(result, "png", new File("D:/123.png").toPath());
    }

    public enum EncoderType {
        GBK,
        UTF8;

    }
}

