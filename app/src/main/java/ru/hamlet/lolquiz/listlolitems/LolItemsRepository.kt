package ru.hamlet.lolquiz.listlolitems

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.hamlet.lolquiz.LolItem
import ru.hamlet.lolquiz.R
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import java.util.*

class LolItemsRepository(
    val lolItemsService: LolItemsService,

) {



//    fun getLolItems(): List<LolItem> {
//        val text =
//            context.resources.openRawResource(R.raw.result).bufferedReader().use { it.readText() }
//
//        val typeToken = object : TypeToken<List<LolItem>>() {}.type     //gson parser
//        val lolItems = Gson().fromJson<List<LolItem>>(text, typeToken)  //
//
//        return lolItems
//    }





    suspend fun getSearch2(search: String): List<LolItem> {
        val lolItems = lolItemsService.getItems()?: listOf()
        val searchResult = mutableListOf<LolItem>()
        val locale = Locale("ru", "RU")

        for (i in 0..(lolItems.size - 1)) {
            if (lolItems[i].name.lowercase(locale).contains(search.lowercase(locale))) {
                searchResult.add(lolItems[i])
            }
        }

        return searchResult
    }

    suspend fun getSearch(search: String): List<LolItem> {
        val lolItems = lolItemsService.getItems()?: listOf()

        return lolItems.filter {
            it.name.lowercase().contains(search.lowercase())
        }
    }

    suspend fun getItems(): List<LolItem>? {
        return lolItemsService.getItems()
    }


}