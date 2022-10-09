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

    public enum EncoderType {
        GBK,
        UTF8
    }

    public Encoder(EncoderType charset) {
        switch (charset) {
            case GBK:
                TYPE = "GBK";
                break;
            case UTF8:
            default:
                TYPE = "UTF-8";
        }
    }

    public BitMatrix getMatrix(String targetText) throws WriterException {
        Map<EncodeHintType, Object> hints = new Hashtable<>();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        hints.put(EncodeHintType.CHARACTER_SET, TYPE);
        hints.put(EncodeHintType.MARGIN, 2);
        return new MultiFormatWriter().encode(targetText, BarcodeFormat.QR_CODE, 300, 300, hints);
    }
}

