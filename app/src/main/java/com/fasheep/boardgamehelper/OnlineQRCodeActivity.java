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
import java.util.Locale;

public class OnlineQRCodeActivity extends AppCompatActivity {
    private Thread connectThread;
    private ImageView qrCode;
    private String time = null;
    private String URL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_qrcode);
        String id = getIntent().getStringExtra("id");
        time = String.valueOf(System.currentTimeMillis());

        Locale locale = getResources().getConfiguration().locale;
        String language = locale.getLanguage();
        if ("zh".equals(language)) {
            URL = Resource.URL_CN;
        } else if ("en".equals(language)) {
            URL = Resource.URL_UK;
        }
        connectThread = new Thread(() -> {
            int code = 0;
            try {
                URL url = new URL(URL + "/web/add.do");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setConnectTimeout(3000);
                connection.setReadTimeout(1000);
                connection.setDoInput(true);
                connection.setDoOutput(true);
                connection.setUseCaches(false);
                connection.connect();

                DataOutputStream os = new DataOutputStream(connection.getOutputStream());
                PrintWriter writer = new PrintWriter(os);
                writer.print(RoomManager.getRoom(id).toJson());
                writer.flush();
                writer.close();

                if ((code = connection.getResponseCode()) == 200) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    time = reader.readLine();
                    /*StringBuilder stringBuilder = new StringBuilder();
                    String temp;
                    while ((temp = reader.readLine()) != null) {
                        stringBuilder.append(temp);
                    }*/
                    reader.close();
                }
                connection.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (code == 0) {
                runOnUiThread(() -> {
                    Toast.makeText(this, R.string.code_0, Toast.LENGTH_SHORT).show();
                    finish();
                });
            } else if (code >= 500) {
                runOnUiThread(() -> {
                    Toast.makeText(this, R.string.code_500, Toast.LENGTH_SHORT).show();
                    finish();
                });
            } else if (code == 200) {
                runOnUiThread(() -> {
                    qrCode.setImageBitmap(getBitmap());
                    qrCode.setOnLongClickListener(v -> {
                        Uri uri = Uri.parse(URL + "/web/display.do?id=" + time);
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent);
                        return true;
                    });
                });
            }
        });
        connectThread.start();

        qrCode = findViewById(R.id.onlineQRCode);
    }

    @Override
    public void onBackPressed() {
        if (connectThread.isAlive()) {
            Toast.makeText(this, R.string.keep_connect, Toast.LENGTH_SHORT).show();
        } else {
            super.onBackPressed();
        }
    }

    private Bitmap getBitmap() {
        Bitmap bitmap;
        try {
            System.out.println(URL + time);
            Encoder encoder = new Encoder(EncoderType.UTF8);
            MatrixToBitmapConvertor convertor = new MatrixToBitmapConvertor();
            bitmap = convertor.convert(encoder.getMatrix(URL + "/web/display.do?id=" + time));
        } catch (WriterException e) {
            throw new RuntimeException(e);
        }
        return bitmap;
    }
}