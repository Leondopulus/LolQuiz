package ru.hamlet.lolquiz.listlolitems

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.hamlet.lolquiz.MyAdapter
import ru.hamlet.lolquiz.R


class ListActivity : AppCompatActivity() {


    val viewModel: ListViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        viewModel.onCreate()

        val rvMain = findViewById<RecyclerView>(R.id.rvMain)
        rvMain.layoutManager = LinearLayoutManager(this)

        val myAdapter = MyAdapter(mutableListOf()) //оборачиваем каждый обьект во view
        rvMain.adapter = myAdapter



        viewModel.lolItemsLiveData.observe(this) { list ->
            myAdapter.replace(list)
        }

        val clearButton = findViewById<ImageView>(R.id.clearSearch)


        viewModel.isClearButtonVisible.observe(this) { isVisible ->
            if (isVisible){
                clearButton.visibility = View.VISIBLE
            }else{
                clearButton.visibility = View.GONE
            }
        }

        val progressBar = findViewById<ProgressBar>(R.id.progressBar)

        viewModel.isProgressBarVisibleLiveData.observe(this){isVisible ->
            if (isVisible){
                progressBar.visibility = View.VISIBLE
            }else{
                progressBar.visibility = View.GONE
            }
        }


        val searchView = findViewById<EditText>(R.id.search)
        searchView.addTextChangedListener {
            viewModel.onSearch(it.toString())
        }



        findViewById<Button>(R.id.delete).setOnClickListener {
            viewModel.onDeleteButtonClick()
        }


        clearButton.setOnClickListener {
            searchView.text.clear()
        }


    }

}