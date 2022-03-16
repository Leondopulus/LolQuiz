package ru.hamlet.lolquiz.listlolitems

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.hamlet.lolquiz.LolItem

class ListViewModel(val repository: LolItemsRepository): ViewModel() {

    val lolItemsLiveData = MutableLiveData<List<LolItem>>()
    val isClearButtonVisible = MutableLiveData<Boolean>(false)

    fun onCreate() {

        val repositoryList: List<LolItem> = repository.getLolItems()
        lolItemsLiveData.value = repositoryList
    }

    fun onDeleteButtonClick() {
        lolItemsLiveData.value = emptyList()
    }

    fun onSearch(text: String){
        isClearButtonVisible.value = !text.isEmpty()
        lolItemsLiveData.value = repository.getSearch(text)

    }

    //кнопку которая все возвращает

    override fun onCleared() {
        super.onCleared()
    }


}