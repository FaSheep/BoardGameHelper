package com.fasheep.boardgamehelper;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fasheep.boardgamehelper.android.MatrixToBitmapConvertor;
import com.fasheep.boardgamehelper.core.RoomManager;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;


import java.util.ArrayList;
import java.util.List;


public class QRCodeActivity extends AppCompatActivity {
    private final List<Bitmap> bitmapList = new ArrayList<>();
    private List<String> secretTextList = new ArrayList<>();
    private int playerNum;
    private String id;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);

        id = getIntent().getStringExtra("id");
//        careerList = (List<Career>) getIntent().getSerializableExtra("careerList");
//        Room room = (Room) getIntent().getSerializableExtra("room");
        playerNum = 0;
//        int n = 1;
//        for (Career c : careerList) {
//            for (int i = 0; i < c.getNum(); i++) {
////                System.out.println(c.getCareer());
//                System.out.printf("player%d, you are %s\n", n++, c.getCareer());
//            }
//        }
//        try {
////            initBitmap();
//        } catch (WriterException e) {
//            e.printStackTrace();
//            Toast.makeText(QRCodeActivity.this, "???", Toast.LENGTH_LONG).show();
////            finish();
//            return;
//        } catch (IndexOutOfBoundsException e) {
//            Toast.makeText(QRCodeActivity.this, "玩家数为0", Toast.LENGTH_LONG).show();
////            finish();
//            return;
//        }
        RoomManager.getRoom(id).rearrange();
        secretTextList = RoomManager.getRoom(id).getTargetText();
        try {
            List<BitMatrix> matrixList = RoomManager.getRoom(id).getMatrixList();
            MatrixToBitmapConvertor convertor = new MatrixToBitmapConvertor();
            bitmapList.clear();
            for (BitMatrix m : matrixList) {
                bitmapList.add(convertor.convert(m));
            }
        } catch (WriterException e) {
            e.printStackTrace();
            Toast.makeText(this, getText(R.string.err_text1), Toast.LENGTH_LONG).show();
        }
        TextView playerNumView = findViewById(R.id.playerNum);
        String output = getString(R.string.tips_text2);
        playerNumView.setText(String.format(output, playerNum + 1));
        ImageView qrCode = findViewById(R.id.imageQR);
        qrCode.setImageBitmap(bitmapList.get(playerNum));
        Button previous = findViewById(R.id.previous);
        Button next = findViewById(R.id.next);

        previous.setOnClickListener(view -> {
//            qrCode.setImageMatrix();
            qrCode.setImageBitmap(bitmapList.get(playerNum > 0 ? --playerNum : 0));
            playerNumView.setText(String.format(output, playerNum + 1));
        });

        next.setOnClickListener(view -> {
            qrCode.setImageBitmap(bitmapList.get(playerNum < bitmapList.size() - 1 ? ++playerNum : playerNum));
            playerNumView.setText(String.format(output, playerNum + 1));
        });
        qrCode.setOnLongClickListener(view -> {
            AlertDialog.Builder secretTextDialog = new AlertDialog.Builder(QRCodeActivity.this);
            secretTextDialog.setTitle(String.format(getString(R.string.dialog_result_text), playerNum + 1));
            secretTextDialog.setMessage(secretTextList.get(playerNum));
            secretTextDialog.show();
            return true;
        });
    }

//    private void initBitmap() throws WriterException {
//        Encoder encoder = new Encoder(EncoderType.GBK);
////        Encoder encoder = new Encoder();
//        String output = "玩家%d，你是%s。";
//        List<String> careers = new ArrayList<>();
//        for (Role role : careerList) {
//            for (int i = 0; i < role.getNum(); i++) {
//                careers.add(role.getCareer());
//            }
//        }
//        if (careers.size() == 0) {
//            System.out.println("玩家数为0");
//            throw new IndexOutOfBoundsException();
//        }
//        Random random = new Random();
//        int n = random.nextInt(20);
//        for (int i = 0; i <= n; i++) {
//            Collections.shuffle(careers);
//        }
//        int num = 1;
//        for (String str : careers) {
//            bitmapList.add(encoder.getBitmap(String.format(output, num++, str)));
//        }
//    }
}