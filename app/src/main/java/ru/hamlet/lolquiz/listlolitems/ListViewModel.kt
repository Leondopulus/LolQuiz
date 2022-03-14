package ru.hamlet.lolquiz.listlolitems

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.hamlet.lolquiz.LolItem

class ListViewModel: ViewModel() {

    val lolItemsLiveData = MutableLiveData<List<LolItem>>()


    fun onCreate(){
        lolItemsLiveData.value = listOf(
            LolItem(id = 0, name = "boots", price = 1, imageUrl = ""),
            LolItem(id = 1, name = "sword", price = 1, imageUrl = ""),
            LolItem(id = 2, name = "shooes", price = 1, imageUrl = ""),
        )
    }
}