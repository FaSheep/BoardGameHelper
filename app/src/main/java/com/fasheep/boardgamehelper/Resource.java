package com.fasheep.boardgamehelper;

import java.util.HashMap;
import java.util.Map;

public class Resource {
    private static final Map<String, Integer> map = new HashMap<>();
    public static final String URL_UK = "http://fasheep.uksouth.cloudapp.azure.com";
    public static final String URL_CN = "http://120.78.126.136";

    static {
        map.put("wolf", R.drawable.wolf);
        map.put("hunter", R.drawable.hunter);
        map.put("seer", R.drawable.seer);
        map.put("witch", R.drawable.witch);
        map.put("guard", R.drawable.guard);
        map.put("idiot", R.drawable.idiot);
        map.put("villager", R.drawable.villager);
        map.put("wolf_king", R.drawable.wolf_king);

        map.put("defRoleImage", R.drawable.ic_role_image);

        map.put("avalon", R.drawable.avalon);
        map.put("custom", R.drawable.cunstom);
    }

    public static int getID(String id) {
        if (map.containsKey(id)) {
            return map.get(id);
        } else {
            return R.drawable.ic_role_image;
        }
    }
}
