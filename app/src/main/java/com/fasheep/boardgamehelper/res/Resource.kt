package com.fasheep.boardgamehelper.res

import com.fasheep.boardgamehelper.R

object Resource {
    private val map: MutableMap<String, Int> = HashMap()
    const val URL_HK: String = "http://bghhk.fasheep.xyz"

    init {
        map["wolf"] = R.drawable.wolf
        map["hunter"] = R.drawable.hunter
        map["seer"] = R.drawable.seer
        map["witch"] = R.drawable.witch
        map["guard"] = R.drawable.guard
        map["idiot"] = R.drawable.idiot
        map["villager"] = R.drawable.villager
        map["wolf_king"] = R.drawable.wolf_king

        map["defRoleImage"] = R.drawable.ic_role_image

        map["avalon"] = R.drawable.avalon
        map["custom"] = R.drawable.cunstom
    }

    fun getID(id: String?): Int {
        return if (id != null && map.containsKey(id)) {
            map[id]!!
        } else {
            R.drawable.ic_role_image
        }
    }
}
