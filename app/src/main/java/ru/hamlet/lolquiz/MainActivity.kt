package ru.hamlet.lolquiz

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.core.widget.addTextChangedListener

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d(TAG, "on create")
        //-------------------------------------

        val okButton = findViewById<Button>(R.id.okButton)
        val cancelButton = findViewById<Button>(R.id.cancelButton)
        val text1 = findViewById<TextView>(R.id.text1)
        val text2 = findViewById<TextView>(R.id.text2)
        val mainTextEdit = findViewById<EditText>(R.id.mainTextEdit)
        val copyButton = findViewById<Button>(R.id.copyButton)
        val hideButton = findViewById<Button>(R.id.hideButton)

        var clickCountOK = 0
        var clickCountCANCEL = 0


        okButton.setOnClickListener {
            clickCountOK++
            text1.text = "button OK ($clickCountOK)"
            Log.d(TAG, "button OK has been clicked")

            val intent = Intent(this, SecondActivity::class.java)
            startActivity(intent)
        }

        cancelButton.setOnClickListener {
            clickCountCANCEL++
            text2.text = "button CANCEL ($clickCountCANCEL)"
            Log.d(TAG, "button CANCEL has been clicked")
        }

        mainTextEdit.addTextChangedListener {
            text1.text = it.toString()
            Log.d(TAG, text1.text.toString())
        }

        copyButton.setOnClickListener{
            text2.text = mainTextEdit.text
        }

        hideButton.setOnClickListener{
            if (mainTextEdit.visibility == View.VISIBLE){
                mainTextEdit.visibility = View.GONE
                hideButton.setText(R.string.show)
                val smth = applicationContext.resources.getString(R.string.show)
            }else{
                mainTextEdit.visibility = View.VISIBLE
                hideButton.setText(R.string.hide)
            }
        }
    }



    override fun onStart() {
        super.onStart()
        Log.d(TAG, "on start")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "on pause")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "on resume")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "on stop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "on destroy")
    }

    companion object {
        private const val TAG = "lolQuize"
    }


}

