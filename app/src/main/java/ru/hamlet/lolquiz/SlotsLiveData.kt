package ru.hamlet.lolquiz

class SlotsLiveData(
    var slot0: Int?,
    var slot1: Int?,
    var slot2: Int?,
    var lolItems: MutableList<LolItem>?
    ) {

    fun clean(){
        slot0 = null
        slot1 = null
        slot2 = null
        lolItems = null
    }

    fun isNotEmpty(): Boolean {
        if (
            slot0 == null
            || slot1 == null
            || slot2 == null
            || lolItems == null
            || lolItems!!.isEmpty()
        ){
            return false
        }
        return true
    }
}