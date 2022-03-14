package ru.hamlet.lolquiz.listlolitems

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.hamlet.lolquiz.LolItem
import ru.hamlet.lolquiz.MyAdapter
import ru.hamlet.lolquiz.R
import androidx.activity.viewModels

class ListActivity : AppCompatActivity() {

    val viewModel: ListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)
        viewModel.onCreate()
        val rvMain = findViewById<RecyclerView>(R.id.rvMain)

        rvMain.layoutManager = LinearLayoutManager(this)
        val myAdapter = MyAdapter(getLolItems().toMutableList())
        rvMain.adapter = myAdapter

        viewModel.lolItemsLiveData.observe(this){ list ->
            myAdapter.replace(list)
        }
    }

    fun getLolItems(): List<LolItem> {
        val text = resources.openRawResource(R.raw.result).bufferedReader().use { it.readText() }

        val typeToken = object : TypeToken<List<LolItem>>() {}.type
        val lolItems = Gson().fromJson<List<LolItem>>(text, typeToken)

        return lolItems
    }


}