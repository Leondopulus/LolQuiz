package ru.hamlet.lolquiz.listlolitems

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.hamlet.lolquiz.LolItem

class ListViewModel(val repository: LolItemsRepository): ViewModel() {

    val lolItemsLiveData = MutableLiveData<List<LolItem>>()
    val isClearButtonVisible = MutableLiveData<Boolean>(false)
    val isProgressBarVisibleLiveData = MutableLiveData<Boolean>(false)

    fun onCreate() {

        viewModelScope.launch {         //тестовый коммент    //val launch = на всякий ссылку на курутину если нужно остановить
            //main
            isProgressBarVisibleLiveData.value = true
            val result = withContext(Dispatchers.IO) {
                //IO
                val result: List<LolItem>? = repository.getItems() //getItemsRequestOrNull()
                Log.d("ViewModel", result.toString())

                return@withContext result
            }
            val repositoryList: List<LolItem> = if (result != null) {
                result
            } else {
                listOf()
                //repositoryList = listOf<LolItem>(LolItem(1, "1", 1, "1",))
            }
            isProgressBarVisibleLiveData.value = false
            lolItemsLiveData.value = repositoryList
        }
    }

    fun onDeleteButtonClick() {

    }

    //запускаем корутину х) что бы не запускать suspend с ui
    fun onSearch(text: String) = viewModelScope.launch{
        isClearButtonVisible.value = !text.isEmpty()
        lolItemsLiveData.value = repository.getSearch(text)

    }

    //кнопку которая все возвращает

    override fun onCleared() {
        super.onCleared()
    }


}