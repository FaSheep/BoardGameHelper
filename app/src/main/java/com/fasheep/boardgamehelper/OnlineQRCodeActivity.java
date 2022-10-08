package com.fasheep.boardgamehelper;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.fasheep.boardgamehelper.android.MatrixToBitmapConvertor;
import com.fasheep.boardgamehelper.core.Encoder;
import com.fasheep.boardgamehelper.core.Encoder.EncoderType;
import com.fasheep.boardgamehelper.core.RoomManager;
import com.google.zxing.WriterException;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class OnlineQRCodeActivity extends AppCompatActivity {
    private Thread connectThread;
    private ImageView qrCode;
    private String time = null;
    private final String URL = "http://120.78.126.136:8080/web/display.do?id=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_qrcode);
        String id = getIntent().getStringExtra("id");
        time = String.valueOf(System.currentTimeMillis());

//        StringBuilder stringBuilder = new StringBuilder();

        connectThread = new Thread(() -> {
            int code = 0;
            try {
                URL url = new URL(getIntent().getStringExtra("url"));
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setConnectTimeout(5000);
                connection.setDoInput(true);
                connection.setDoOutput(true);
                connection.setUseCaches(false);
                connection.setRequestProperty("date", time);
                connection.connect();

                DataOutputStream os = new DataOutputStream(connection.getOutputStream());
                PrintWriter writer = new PrintWriter(os);
                writer.print(RoomManager.getRoom(id).toJson());
                writer.flush();
                writer.close();

                if ((code = connection.getResponseCode()) == 200) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String temp;
                    while ((temp = reader.readLine()) != null) {
                        stringBuilder.append(temp);
                    }
                    reader.close();
                }

                connection.disconnect();
//                System.out.
//                System.out.println("\n\n\n\n\n\n\n\n\n\n" + stringBuilder + "\n\n\n\n\n\n\n\n\n");
//                done = "success".equals(stringBuilder.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (code == 0) {
                runOnUiThread(() -> {
                    Toast.makeText(this, "连接失败", Toast.LENGTH_SHORT).show();
                    finish();
                });
            } else if (code >= 500) {
                runOnUiThread(() -> {
                    Toast.makeText(this, "初始化失败", Toast.LENGTH_SHORT).show();
                    finish();
                });
            } else if (code == 200) {
                runOnUiThread(() -> {
                    qrCode.setImageBitmap(getBitmap());
                    qrCode.setOnLongClickListener(v -> {
                        Uri uri = Uri.parse(URL + time);
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent);
                        return true;
                    });
                });
            }
        });
        connectThread.start();


        qrCode = findViewById(R.id.onlineQRCode);
//        qrCode.setOnLongClickListener();
//        qrCode.setImageBitmap();
    }

    @Override
    public void onBackPressed() {
        if (connectThread.isAlive()) {
            Toast.makeText(this, "正在连接，请勿退出", Toast.LENGTH_SHORT).show();
        } else {
            super.onBackPressed();
        }
//
    }

    @Override
    protected void onDestroy() {
//        while (connectThread.isAlive()){
//
//            Toast.makeText(this, "closing", Toast.LENGTH_SHORT).show();
//        }

        super.onDestroy();
    }

    private Bitmap getBitmap() {
        Bitmap bitmap;
        try {
//            URL + time;
            System.out.println(URL + time);
            Encoder encoder = new Encoder(EncoderType.UTF8);
            MatrixToBitmapConvertor convertor = new MatrixToBitmapConvertor();
            bitmap = convertor.convert(encoder.getMatrix(URL + time));
        } catch (WriterException e) {
            throw new RuntimeException(e);
        }
        return bitmap;
    }
}