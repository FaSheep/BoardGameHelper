package com.fasheep.boardgamehelper.core;

import java.util.*;

public class RoomManager {
    private static final Map<String, Room> map = new LinkedHashMap<>();

    public static synchronized int getNumOfRooms() {
        return map.size();
    }

    public static synchronized Set<String> getKeySet() {
        return map.keySet();
    }

    public static synchronized Room getRoom(String roomID) {
        return map.get(roomID);
    }

    public static synchronized void addRoom(String roomID, Room room) {
        map.put(roomID, room);
    }

    public static synchronized void removeRoom(String roomID) {
        map.remove(roomID);
    }

    public static synchronized void clean() {
        map.clear();
    }

    public static List<Room> getRoomList() {
        return new ArrayList<>(map.values());
    }
}
