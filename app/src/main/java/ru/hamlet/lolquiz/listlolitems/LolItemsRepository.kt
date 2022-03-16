package ru.hamlet.lolquiz.listlolitems

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.hamlet.lolquiz.LolItem
import ru.hamlet.lolquiz.R
import java.util.*

class LolItemsRepository(val context: Context) {


    fun getLolItems(): List<LolItem> {
        val text =
            context.resources.openRawResource(R.raw.result).bufferedReader().use { it.readText() }

        val typeToken = object : TypeToken<List<LolItem>>() {}.type
        val lolItems = Gson().fromJson<List<LolItem>>(text, typeToken)

        return lolItems
    }

    fun getSearch2(search: String): List<LolItem> {
        val lolItems = getLolItems()
        val searchResult = mutableListOf<LolItem>()
        val locale = Locale("ru", "RU")

        for (i in 0..(lolItems.size - 1)) {
            if (lolItems[i].name.lowercase(locale).contains(search.lowercase(locale))) {
                searchResult.add(lolItems[i])
            }
        }

        return searchResult
    }

    fun getSearch(search: String): List<LolItem> {
        val lolItems = getLolItems()

        return lolItems.filter {
            it.name.lowercase().contains(search.lowercase())
        }
    }
}