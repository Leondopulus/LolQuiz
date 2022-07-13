package ru.hamlet.lolquiz.quizgameactivity

import android.content.ClipData
import android.content.ClipDescription
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.hamlet.lolquiz.LolItem
import ru.hamlet.lolquiz.listlolitems.LolItemsRepository
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.hamlet.lolquiz.MyDragShadowBuilder

class QuizViewModel(val repository: LolItemsRepository) : ViewModel() {

    var listLolItems = MutableLiveData<List<LolItem>>()
    var questItemLiveData = MutableLiveData<LolItem?>()

    var item0IDLiveData = MutableLiveData<Int>()
    var item1IDLiveData = MutableLiveData<Int>()
    var item2IDLiveData = MutableLiveData<Int>()

    var img0LiveData = MutableLiveData<Int>()

    var itemComponentsIDs = mutableListOf<MutableLiveData<Int>>()

    ////////////////////////////////////
    fun onCreate() {
        viewModelScope.launch {
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
            }
            itemComponentsIDs.add(item0IDLiveData)
            itemComponentsIDs.add(item1IDLiveData)
            itemComponentsIDs.add(item2IDLiveData)


            println("view model got result")
            listLolItems.value = repositoryList
            getNewQuizItem()


        }




    }

    fun onOkButtonClick() {
        if(checkIsItemValid() == true){
            Log.d("quizresult", "its true")
        }else{
            Log.d("quizresult", "its false")
        }

        getNewQuizItem()
    }

    fun onImgButtonClick(button : View){

    }

    fun checkIsItemValid(): Boolean {
        val item = questItemLiveData.value
        val setOfAnswerIds = mutableSetOf<Int>()

        for (i in 0 until itemComponentsIDs.size){
            if (itemComponentsIDs[i].value != null){
                setOfAnswerIds.add(itemComponentsIDs[i].value!!)
            }
        }

        val quizIdsSet = mutableSetOf<Int>()
        if (item != null) {
            for (i in 0 until item.lvl1Components.size)
                quizIdsSet.add(item.lvl1Components[i])
        }

        if (setOfAnswerIds == quizIdsSet){
            return true
        }

        return false
    }

    fun getNewQuizItem() {
        while (true){
            val item = getItem()
            if (item.lvl1Components.isEmpty()) {
                Log.d("quizVM", "lvl1Comp = empty")
                continue
            } else{
                Log.d("quizVM", "comps: " + item.lvl1Components)
                questItemLiveData.value = item
                break
            }
        }
    }

    fun getItem(): LolItem {

        val listItems = listLolItems.value
        Log.d("quizVM", "1")
        var item: LolItem? = null

        while (item == null){
            item = listItems?.get((0..(listItems.size - 1)).random())
            Log.d("quizVM", "item: "+ item)

        }

        return item
    }

    fun getItem(itemId: Int): LolItem? {
        val listItems = listLolItems.value
        if (listItems != null) {
            for (i in 0..(listItems.size - 1)) {
                val item = listItems.get(i)
                if (item.id == itemId) {
                    return item

                }
            }
        }
        return null
    }


}