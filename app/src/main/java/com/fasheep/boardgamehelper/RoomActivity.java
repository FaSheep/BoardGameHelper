package com.fasheep.boardgamehelper;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.fasheep.boardgamehelper.adapter.RoleAdapter;
import com.fasheep.boardgamehelper.core.RoomManager;

public class RoomActivity extends AppCompatActivity {
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);

        id = getIntent().getStringExtra("id");
        RoleAdapter roleAdapter = new RoleAdapter(id);
        RecyclerView roleList = findViewById(R.id.roleList);
        Configuration configuration = getResources().getConfiguration();
        GridLayoutManager layoutManager;
        if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            layoutManager = new GridLayoutManager(this, 4);//Test
        } else {
            layoutManager = new GridLayoutManager(this, 2);
        }
        roleList.setLayoutManager(layoutManager);
        roleList.setAdapter(roleAdapter);
        LinearLayout tipsView = findViewById(R.id.tips);

        SwitchCompat online = findViewById(R.id.onlineMode);

        Button submit = findViewById(R.id.submit);
        submit.setOnClickListener(view -> {
            if (RoomManager.getRoom(id).getNumOfPlayers() > 0) {
                if (online.isChecked()){
                    Intent intent = new Intent(RoomActivity.this, OnlineQRCodeActivity.class);
                    intent.putExtra("id", id);
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(RoomActivity.this, QRCodeActivity.class);
                    intent.putExtra("id", id);
                    startActivity(intent);
                }
            } else {
                Toast.makeText(RoomActivity.this, getText(R.string.toast_text), Toast.LENGTH_LONG).show();
            }
        });

        ImageButton close = findViewById(R.id.tips_button);
        close.setOnClickListener(view -> {
            tipsView.setVisibility(View.GONE);
        });
    }
}