package ru.hamlet.lolquiz

import org.json.JSONObject

data class TagID(
    val idLolItem: Int,
    val indexObject: Int,
) {
    constructor(obj: JSONObject) : this(
        idLolItem = obj.getInt("id"),
        indexObject = obj.getInt("index")
    )

    fun toJSON(): JSONObject {
        val obj = JSONObject()
        obj.put("id", idLolItem)
        obj.put("index", indexObject)
        return obj
    }


}