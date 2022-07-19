package ru.hamlet.lolquiz.quizgameactivity

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
import com.bumptech.glide.Glide
import kotlinx.coroutines.launch
import ru.hamlet.lolquiz.R
import ru.hamlet.lolquiz.SlotsLiveData
import java.lang.IllegalStateException


class QuizViewModel(val repository: LolItemsRepository) : ViewModel() {

    private val LOG_TAG = "QuizViewModel"

    private var lolItems = listOf<LolItem>();

    var questItemLiveData = MutableLiveData<LolItem>()
    var slotsLiveData = MutableLiveData<SlotsLiveData>()
    var levelComponentVariantsLiveData = MutableLiveData<List<LolItem>>()


    ////////////////////////////////////
    fun onCreate() {
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                //IO
                val result: List<LolItem>? = repository.getItems() //getItemsRequestOrNull()
                Log.d(LOG_TAG, result.toString())

                return@withContext result
            }
            val repositoryList = result ?: listOf()
            lolItems = repositoryList

            Log.d(LOG_TAG, "Got result")
            createNewQuizQuestion()
        }
    }

    fun onOkButtonClick() {
        //check if all slots are not empty// done
        if(slotsLiveData.value != null && slotsLiveData.value!!.isNotEmpty()){
            if (checkIsItemValid()) {
                winMessage()
                createNewQuizQuestion()
            } else {
                loseMessage()
            }
        }
    }
    private fun winMessage(){

    }

    private fun loseMessage(){

    }


    private fun checkIsItemValid(): Boolean {
        // check slotsLiveData.value to validate is answer right or not!// done
        val questLolItem = questItemLiveData.value ?: return false
        val questItemsIdsList = mutableListOf<Int>()
        val slotsList = slotsLiveData.value
        val slotsIdsList = mutableListOf<Int>()

        for (i in questLolItem.lvl1Components.indices) {
            questItemsIdsList.add(questLolItem.lvl1Components[i])
        }

        if (slotsList == null ) return false
        if (slotsList.lolItems == null) return false
        for (i in slotsList.lolItems!!.indices) {
            slotsIdsList.add(slotsList.lolItems!![i].id)
        }


        for (i in slotsIdsList.indices) {
            for (j in questItemsIdsList.indices) {
                if (slotsIdsList[i] == questItemsIdsList[j]) {
                    questItemsIdsList.removeAt(j)
                }
            }
        }

        if (questItemsIdsList.isNotEmpty()) return false

        return true
    }

    private fun createNewQuizQuestion() {
        val randomLolItem = getRandomItemWithComponents()
        questItemLiveData.value = randomLolItem
        levelComponentVariantsLiveData.value = generateQuestVariants(randomLolItem)
//        cleanSlotsLiveData()
        slotsLiveData.value?.clean()
    }

//    private fun cleanSlotsLiveData(){
//        slotsLiveData.value?.slot0 = null
//        slotsLiveData.value?.slot1 = null
//        slotsLiveData.value?.slot2 = null
//        slotsLiveData.value?.lolItems = null
//    }

    private fun generateQuestVariants(questItem: LolItem): List<LolItem> {
        val lvl1components = questItem.lvl1Components
        Log.d(LOG_TAG, "lvl1components: $lvl1components")

        val result = mutableListOf<LolItem>()
        for (componentId in lvl1components) {
            val component = getLolItemOrNull(componentId)
                ?: throw IllegalStateException("For some reason we can't find lol item with id[$componentId]")
            result.add(component)
        }

        val variantsCount = 10
        val remainingCount = variantsCount - result.size

        for (i in 0 until remainingCount) {
            //  add logic to respect 'isItemHasNoParents' //added 124nd string
            val randomItem = getRandomItem(excludingIds = listOf(questItem.id), true)
            result.add(randomItem)
        }

        return result.shuffled()
    }

    private fun getRandomItemWithComponents(): LolItem {
        while (true) {
            val item = getRandomItem()
            if (item.lvl1Components.isEmpty()) continue
            return item
        }
    }

    private fun getRandomItem(excludingIds: List<Int> = listOf(), isSib: Boolean = false): LolItem {
        while (true) {
            val item = lolItems.random()
            if (excludingIds.contains(item.id)) continue
            if (isSib && item.lvl1Components.isNotEmpty()) continue
            return item
        }
    }

    private fun getLolItemOrNull(itemId: Int): LolItem? {
        for (item in lolItems) {
            if (item.id == itemId) return item;
        }
        return null
    }


//    fun isItemHasNoParents(itemId: Int): Boolean {
//        // add implementation // not used
//        return false
//    }

}