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
import java.lang.IllegalStateException


class QuizViewModel(val repository: LolItemsRepository) : ViewModel() {

    private val LOG_TAG = "QuizViewModel"

    private var lolItems = listOf<LolItem>();

    var questItemLiveData = MutableLiveData<LolItem>()
    var slotsLiveData = MutableLiveData<List<LolItem?>>()
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
        if(checkIsItemValid()){
            Log.d(LOG_TAG, "its true")
        }else{
            Log.d(LOG_TAG, "its false")
        }

        createNewQuizQuestion()
    }

    fun checkIsItemValid(): Boolean {
        // TODO check slotsLiveData.value to validate is answer right or not!
        return true
//        val item = questItemLiveData.value
//        val setOfAnswerIds = mutableSetOf<Int>()
//
//        for (i in 0 until slotsLiveData.va.size){
//            if (itemComponentsIDs[i].value != null){
//                setOfAnswerIds.add(itemComponentsIDs[i].value!!)
//            }
//        }
//
//        val quizIdsSet = mutableSetOf<Int>()
//        if (item != null) {
//            for (i in 0 until item.lvl1Components.size)
//                quizIdsSet.add(item.lvl1Components[i])
//        }
//
//        if (setOfAnswerIds == quizIdsSet){
//            return true
//        }
//
//        return false
    }

    private fun createNewQuizQuestion() {
        val randomLolItem = getRandomItemWithComponents()
        questItemLiveData.value = randomLolItem
        levelComponentVariantsLiveData.value = generateQuestVariants(randomLolItem)
        slotsLiveData.value = List(randomLolItem.lvl1Components.size) { null }
    }

    private fun generateQuestVariants(questItem: LolItem): List<LolItem> {
        val lvl1components = questItem.lvl1Components
        Log.d(LOG_TAG, "lvl1components: $lvl1components")

        val result = mutableListOf<LolItem>()
        for (componentId in lvl1components) {
            val component = getLolItemOrNull(componentId) ?: throw IllegalStateException("For some reason we can't find lol item with id[$componentId]")
            result.add(component)
        }

        val variantsCount = 10
        val remainingCount = variantsCount - result.size

        for (i in 0 until remainingCount) {
            // TODO add logic to respect 'isItemHasNoParents'
            val randomItem = getRandomItem(excludingIds = listOf(questItem.id))
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

    private fun getRandomItem(excludingIds: List<Int> = listOf()): LolItem {
        while (true) {
            val item = lolItems.random()
            if (excludingIds.contains(item.id)) continue
            return item
        }
    }

    private fun getLolItemOrNull(itemId: Int): LolItem? {
        for (item in lolItems) {
            if (item.id == itemId) return item;
        }
        return null
    }


    fun isItemHasNoParents(itemId: Int): Boolean {
        // TODO add implementation
        return false
    }

}