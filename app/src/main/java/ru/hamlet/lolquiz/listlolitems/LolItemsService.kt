package ru.hamlet.lolquiz.listlolitems

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.hamlet.lolquiz.LolItem
import ru.hamlet.lolquiz.R
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import java.util.prefs.Preferences

interface LolItemsService {
    suspend fun getItems():List<LolItem>?
}

class RetrofitLolItemService(val api: LolItemsAPI): LolItemsService{
    override suspend fun getItems(): List<LolItem>? {
        try {
            return api.getItems()
        }catch (e:Exception){
            e.printStackTrace()
        }
        return null
    }

}

class StreamLolItemService: LolItemsService{
    override suspend fun getItems(): List<LolItem>? {
        try {
            val url = URL("http://5462-5-164-236-45.ngrok.io/items")
            val connection = url.openConnection()
            val connectionInputStream = connection.getInputStream()
            val inputStreamReader =
                InputStreamReader(connectionInputStream)    //считывает побайтово. потом buffer считывает кучкой

            var text = ""

            BufferedReader(inputStreamReader).use { inp ->
                while (true) {
                    val line: String? = inp.readLine()
                    if (line == null) break
                    text += line + " "
                }
            }

            val typeToken = object : TypeToken<List<LolItem>>() {}.type
            return Gson().fromJson(text, typeToken)
        } catch (e: Exception) {
            e.printStackTrace()

        }
        return null
    }

}

class CacheLolItemService(val context: Context): LolItemsService{
    override suspend fun getItems(): List<LolItem>? {
        val text = context.resources.openRawResource(R.raw.result).bufferedReader().use { it.readText() }

        val typeToken = object : TypeToken<List<LolItem>>() {}.type     //gson parser
        val lolItems = Gson().fromJson<List<LolItem>>(text, typeToken)  //

        return lolItems
    }


}

class SmartLolItemService(val context: Context, val api: LolItemsAPI, ):LolItemsService{
    val sharedPreferences = context.getSharedPreferences("lolItems", Context.MODE_PRIVATE)
    val CACHE_LIVE_TIME = 1000*60*10
    override suspend fun getItems(): List<LolItem>? {
        val lastSync = sharedPreferences.getLong("lastSync", -1)
        if (System.currentTimeMillis()-lastSync > CACHE_LIVE_TIME){
            try {

                val items = api.getItems()      //перевести в GSON (не в цикле) - Gson().toJson(items) - положить в pref -

                val cacheStringResult = Gson().toJson(items)

                sharedPreferences.edit()
                    .putLong("lastSync", System.currentTimeMillis())
                    .putString("items", cacheStringResult)
                    .apply()
                return items

            }catch (e:Exception){
                e.printStackTrace()
            }

            return null
        }else{
            //TODO request from cache   // берем строку и парсим в обьект
            val typeToken = object : TypeToken<List<LolItem>>() {}.type     //gson parser
            val lolItems = Gson().fromJson<List<LolItem>>(sharedPreferences.getString("items", ""), typeToken)  //

            return lolItems
             //а тут возвращаем
        }
    }

}

