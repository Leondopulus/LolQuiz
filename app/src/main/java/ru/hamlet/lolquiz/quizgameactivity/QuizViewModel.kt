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
import ru.hamlet.lolquiz.TagID
import java.lang.IllegalStateException


class QuizViewModel(val repository: LolItemsRepository) : ViewModel() {

    private val LOG_TAG = "QuizViewModel"

    private var lolItems = listOf<LolItem>();

    var questItemLiveData = MutableLiveData<LolItem>()
    var slotsLiveData = MutableLiveData<List<LolItem?>>()
    var levelComponentVariantsLiveData = MutableLiveData<List<LolItem?>>()


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

    fun onSlotDropped(tagID: TagID, slotIndex: Int) {
        val slots = slotsLiveData.value?.toMutableList() ?: return
        val variants = levelComponentVariantsLiveData.value?.toMutableList() ?: return

        slots[slotIndex] = getLolItemOrNull(tagID.idLolItem)
        variants[tagID.indexObject] = null

        slotsLiveData.value = slots
        levelComponentVariantsLiveData.value = variants
    }

    fun onOkButtonClick() {
        //check if all slots are not empty// done
        if (slotsLiveData.value != null ) {
            if (checkIsItemValid()) {
                winMessage()

            } else {
                loseMessage()
            }
        }
        createNewQuizQuestion()
    }

    private fun winMessage() {

    }

    private fun loseMessage() {

    }

    private fun checkIsItemValid(): Boolean {
        // check slotsLiveData.value to validate is answer right or not!// done
        val questLolItem = questItemLiveData.value ?: return false
        val questItemsIdsList = mutableListOf<Int>()
        val slotsList = slotsLiveData.value ?: return false
        val slotsIdsList = mutableListOf<Int>()

        for (i in questLolItem.lvl1Components.indices) {
            questItemsIdsList.add(questLolItem.lvl1Components[i])
        }

        for (i in slotsList.indices) {
            val lolItem = slotsList[i] ?: return false
            slotsIdsList.add(lolItem.id)
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
        slotsLiveData.value = MutableList(randomLolItem.lvl1Components.size){
            null
        }
    }


    private fun generateQuestVariants(questItem: LolItem): List<LolItem> {
        val lvl1components = questItem.lvl1Components
//        Log.d(LOG_TAG, "lvl1components: $lvl1components")

        val result = mutableListOf<LolItem>()
        for (componentId in lvl1components) {
            val component = getLolItemOrNull(componentId)
                ?: throw IllegalStateException("For some reason we can't find lol item with id[$componentId]")
            result.add(component)
        }

        val variantsCount = 10
        val remainingCount = variantsCount - result.size

        for (i in 0 until remainingCount) {
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