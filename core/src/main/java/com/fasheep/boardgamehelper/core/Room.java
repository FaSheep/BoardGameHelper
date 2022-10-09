package com.fasheep.boardgamehelper.core;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Room implements Serializable {
    private final List<Role> roleList = new ArrayList<>();
    private final List<String> roleText = new ArrayList<>();
    //    private final List<String> targetText = new ArrayList<>();
    private final String roomName;
    private final String roomImagePath;
    private final String formatText;
    private int playerIndex = 0;


//    public static Room getInstance(RoomType type, String formatText) {
//        Room room = null;
//        switch (type) {
//            case Werewolf:
//                room = new Room("Werewolf", "", formatText);
//                break;
//            case Avalon:
//                room = new Room("Avalon", "", formatText);
//                break;
//            case Custom:
//                room = new Room("Custom", "", formatText);
//                break;
//            default:
//                throw new IllegalStateException("Unexpected value: " + type);
//        }
//        return room;
//    }

    public static Room getInstanceFromJson(String json) throws JsonSyntaxException {
        Gson gson = new Gson();
        return gson.fromJson(json, Room.class);
    }

    public Room(String roomName, String roomImagePath, String formatText) {
        this.roomName = roomName;
        this.roomImagePath = roomImagePath;
        this.formatText = formatText;
    }

    public String getRoomImagePath() {
        return roomImagePath;
    }

    public String getRoomName() {
        return roomName;
    }

    public int getNumOfPlayers() {
        int num = 0;
        for (Role role : roleList) {
            num += role.getNumber();
        }
        return num;
    }

    public int getNumOfRoles() {
        return roleList.size();
    }

    public Role getRole(int index) {
        return roleList.get(index);
    }

    public String getPlayer(int index) {
        return roleText.get(index);
    }

    public String getPlayer() {
        return roleText.get(playerIndex < roleText.size() - 1 ? playerIndex++ : playerIndex);
    }

    public void reloadIndex() {
        playerIndex = 0;
    }

    public void addRole(String name, int defNumber, String imagePath) {
        roleList.add(new Role(name, defNumber, imagePath));
    }

    public void removeRole(int index) {
        roleList.remove(index);
    }

    public List<String> getTargetText() {
        int i = 1;
        List<String> targetText = new ArrayList<>();
        for (String s : roleText) {
            targetText.add(String.format(formatText, i++, s));
        }
        return Collections.unmodifiableList(targetText);
    }

    public void rearrange() {
        roleText.clear();
        for (Role role : roleList) {
            for (int i = 0; i < role.getNumber(); i++) {
                roleText.add(role.getName());
            }
        }
        Collections.shuffle(roleText, new Random());
    }

    public List<BitMatrix> getMatrixList() throws WriterException {
        Encoder encoder = new Encoder(Encoder.EncoderType.UTF8);
        List<BitMatrix> matrixList = new ArrayList<>();
        for (String temp : getTargetText()) {
            matrixList.add(encoder.getMatrix(temp));
        }
        return Collections.unmodifiableList(matrixList);
    }

    public String toJson() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(this);
    }

    @Override
    public String toString() {
        return toJson();
    }


}
