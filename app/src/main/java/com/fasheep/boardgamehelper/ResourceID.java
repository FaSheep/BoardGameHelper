package com.fasheep.boardgamehelper;

import java.util.HashMap;
import java.util.Map;

public class ResourceID {
    private final static Map<String, Integer> map = new HashMap<>();

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
