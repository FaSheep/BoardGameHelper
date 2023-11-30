package com.fasheep.boardgamehelper;

import android.content.Intent;
import android.content.res.AssetManager;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.fasheep.boardgamehelper.adapter.OnItemClickListener;
import com.fasheep.boardgamehelper.adapter.RoomAdapter;
import com.fasheep.boardgamehelper.core.Room;
import com.fasheep.boardgamehelper.core.RoomManager;
import com.google.gson.JsonSyntaxException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Locale locale = getResources().getConfiguration().locale;
        String language = locale.getLanguage();

        AssetManager manager = getAssets();
        try {
            String[] files = manager.list(language);
            int i = 0;
            for (String s : files) {
                InputStream is = manager.open(language + "/" + s);
                InputStreamReader inputStreamReader = new InputStreamReader(is);
                BufferedReader reader = new BufferedReader(inputStreamReader);
                String temp;
                StringBuilder stringBuilder = new StringBuilder();
                while ((temp = reader.readLine()) != null) {
                    stringBuilder.append(temp);
                }
                reader.close();

                RoomManager.addRoom(String.valueOf(i++), Room.getInstanceFromJson(stringBuilder.toString()));
            }//
//            manager.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }catch (JsonSyntaxException e){
            e.printStackTrace();
        }

        RoomAdapter roomAdapter = new RoomAdapter();
        roomAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick( View view, int i) {
                Intent intent = new Intent(MainActivity.this, RoomActivity.class);
                intent.putExtra("id", String.valueOf(i));
                startActivity(intent);
            }
        });
        RecyclerView roomList = findViewById(R.id.roomList);
        roomList.setLayoutManager(new LinearLayoutManager(this));
        roomList.setAdapter(roomAdapter);
    }
}