package com.fasheep.boardgamehelper.core;

import java.util.HashMap;
import java.util.Map;

public class RoomManager {
    private static final Map<String, Room> map = new HashMap<>();

    synchronized public static int getNumOfRooms() {
        return map.size();
    }

    synchronized public static Room getRoom(String roomID) {
        return map.get(roomID);
    }

    synchronized public static void addRoom(String roomID, Room room) {
        map.put(roomID, room);
    }

    synchronized public static void removeRoom(String roomID) {
        map.remove(roomID);
    }

    synchronized public static void clean() {
        map.clear();
    }
}
