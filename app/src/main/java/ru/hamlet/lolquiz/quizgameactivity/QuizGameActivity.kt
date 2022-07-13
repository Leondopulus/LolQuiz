package ru.hamlet.lolquiz.quizgameactivity

import android.content.ClipData
import android.content.ClipDescription
import android.graphics.Bitmap
import android.os.Bundle
import android.view.DragEvent
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat.startDragAndDrop
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.bumptech.glide.Glide
import ru.hamlet.lolquiz.LolItem
import ru.hamlet.lolquiz.MyDragShadowBuilder
import ru.hamlet.lolquiz.R
import kotlin.random.Random

import android.graphics.Canvas
import android.graphics.Point
import android.view.DragEvent.ACTION_DRAG_ENTERED
import android.view.DragEvent.ACTION_DROP
import androidx.core.content.res.ResourcesCompat



class QuizGameActivity() : AppCompatActivity() {

    val viewModel: QuizViewModel by viewModel()
//    val MyDragShadowBuilder: MyDragShadowBuilder()

    val listVariables = mutableListOf<ImageView>()


    //////////////////////////////////////////////////
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz_game)

        viewModel.onCreate()


        findViewById<Button>(R.id.okButton).setOnClickListener {
            println("2")
            viewModel.onOkButtonClick()
        }


        viewModel.questItemLiveData.observe(this) { lolItem ->
            if (lolItem == null) {
                viewModel.getNewQuizItem()
            } else {
                setQuestItem(lolItem)
            }

        }


//        findViewById<View>(R.id.componentImg0).setOnDragListener()

        val imgView = findViewById<ImageView>(R.id.img0)
        setDragAndDropOnImageView(imgView)


        findViewById<View>(R.id.img1).setOnClickListener {
            println("long click")
        }
        findViewById<View>(R.id.img2).setOnClickListener {
            viewModel.onImgButtonClick(findViewById<View>(R.id.img2))
        }
        findViewById<View>(R.id.img3).setOnClickListener {
            viewModel.onImgButtonClick(findViewById<View>(R.id.img3))
        }
        findViewById<View>(R.id.img4).setOnClickListener {
            viewModel.onImgButtonClick(findViewById<View>(R.id.img4))
        }
        findViewById<View>(R.id.img5).setOnClickListener {
            viewModel.onImgButtonClick(findViewById<View>(R.id.img5))
        }
        findViewById<View>(R.id.img6).setOnClickListener {
            viewModel.onImgButtonClick(findViewById<View>(R.id.img6))
        }
        findViewById<View>(R.id.img7).setOnClickListener {
            viewModel.onImgButtonClick(findViewById<View>(R.id.img7))
        }
        findViewById<View>(R.id.img8).setOnClickListener {
            viewModel.onImgButtonClick(findViewById<View>(R.id.img8))
        }
        findViewById<View>(R.id.img9).setOnClickListener {
            viewModel.onImgButtonClick(findViewById<View>(R.id.img9))
        }

        // 1
        val viewDragListener = View.OnDragListener { view, dragEvent ->

            //2
            val draggableItem = dragEvent.localState as View

            //3
            when (dragEvent.action) {
                DragEvent.ACTION_DRAG_STARTED -> {
                    println("draggg started ")
                    true
                }
                DragEvent.ACTION_DRAG_ENTERED -> {
                    println("draggg entered ")
                    view.invalidate()
                    true
                }
                DragEvent.ACTION_DRAG_LOCATION -> {
                    println("draggg location? wtf is tis? ")
                    true
                }
                DragEvent.ACTION_DRAG_EXITED -> {
                    println("draggg exited ")
                    true
                }
                DragEvent.ACTION_DROP -> {
                    println("draggg dropped ")
                    true
                }
                DragEvent.ACTION_DRAG_ENDED -> {
                    println("draggg ended ")
                    true
                }
                else -> {
                    println("draggg falsed ")
                    false
                }
            }
        }

        val slot = mutableListOf<ImageView>(
            findViewById(R.id.componentImg0),
            findViewById(R.id.componentImg1),
            findViewById(R.id.componentImg2)
        )
        //slot[0].setOnDragListener(viewDragListener)

        var view = findViewById<ImageView>(R.id.img0)
        listVariables.add(view)
        view.setOnDragListener(viewDragListener)

        view = findViewById(R.id.img1)
        listVariables.add(view)
        view = findViewById(R.id.img2)
        listVariables.add(view)
        view = findViewById(R.id.img3)
        listVariables.add(view)
        view = findViewById(R.id.img4)
        listVariables.add(view)
        view = findViewById(R.id.img5)
        listVariables.add(view)
        view = findViewById(R.id.img6)
        listVariables.add(view)
        view = findViewById(R.id.img7)
        listVariables.add(view)
        view = findViewById(R.id.img8)
        listVariables.add(view)
        view = findViewById(R.id.img9)
        listVariables.add(view)





    }


    fun setDragAndDropOnImageView (view: ImageView){
        ///////////////////////////////////////////
        // Create a string for the ImageView label.
        val IMAGEVIEW_TAG = "icon bitmap"
        val iconBitmap = Bitmap.createBitmap(100, 100,
        Bitmap.Config.ARGB_8888)

        view.apply {
            // Sets the bitmap for the ImageView from an icon bit map (defined elsewhere).
            setImageBitmap(iconBitmap)
            tag = IMAGEVIEW_TAG
            setOnLongClickListener { v ->
                // Create a new ClipData.
                // This is done in two steps to provide clarity. The convenience method
                // ClipData.newPlainText() can create a plain text ClipData in one step.

                // Create a new ClipData.Item from the ImageView object's tag.
                val item = ClipData.Item(v.tag as? CharSequence)

                // Create a new ClipData using the tag as a label, the plain text MIME type, and
                // the already-created item. This creates a new ClipDescription object within the
                // ClipData and sets its MIME type to "text/plain".
                val dragData = ClipData(
                    v.tag as? CharSequence,
                    arrayOf(ClipDescription.MIMETYPE_TEXT_PLAIN),
                    item)

                // Instantiate the drag shadow builder.
                val myShadow = MyDragShadowBuilder(this)

                // Start the drag.
                v.startDragAndDrop(dragData,  // The data to be dragged
                    myShadow,  // The drag shadow builder
                    null,      // No need to use local data
                    0          // Flags (not currently used, set to 0)
                )

                // Indicate that the long-click was handled.
                true
            }
        }
    }

    ///////////////////////////////////////////////////
//    @SuppressLint("CutPasteId")
    fun setQuestItem(lolItem: LolItem) {
        println("debug setQuestItem start")
        val questImg = findViewById<ImageView>(R.id.questImg)
        Glide.with(this).load(lolItem.imageUrl).into(questImg)

        ////////////////////
        var rIndex = 0
        val indexSet = mutableSetOf<Int>()
        val finalIdexSet = mutableSetOf<Int>()
        for (i in 0..(listVariables.size - 1)) {
            finalIdexSet.add(i)
        }


        //////////////////
        val slot = mutableListOf<ImageView>(
            findViewById(R.id.componentImg0),
            findViewById(R.id.componentImg1),
            findViewById(R.id.componentImg2)
        )
//        slot[0].setOnDragListener()

        val lvl1components = lolItem.lvl1Components
        println("lvl1components: " + lvl1components)


        //add true items
        var itemId: Int
        var item: LolItem?
        var img: View

        for (j in 0 until slot.size) {
            if (lvl1components.size > j) {

                slot[j].visibility = View.VISIBLE //

                while (true) {
                    rIndex = (0..(listVariables.size - 1)).random()
                    if (indexSet.contains(rIndex)) {
                        continue
                    } else {
                        itemId = lvl1components[j]
                        item = viewModel.getItem(itemId)
                        img = listVariables.get(rIndex)
                        if (item != null) {
                            Glide.with(this).load(item.imageUrl).into(img)
                            println("item " + item.name + " pos " + rIndex)
                        }
                        indexSet.add(rIndex)
                        break
                    }
                }
            } else {
                slot[j].visibility = View.GONE    //
            }
        }

        //add random items
        while (true) {
            rIndex = (0..(listVariables.size - 1)).random()
            if (indexSet == finalIdexSet) {
                break
            } else if (indexSet.contains(rIndex)) {
                continue
            } else {
                img = listVariables.get(rIndex)
                item = null

                while (item == null) {
                    val randomItem = viewModel.getItem()
                    if (randomItem.lvl1Components.size != 0) {
                        item =
                            viewModel.getItem(randomItem.lvl1Components.get((0..(randomItem.lvl1Components.size - 1)).random()))
                    }
                }
                Glide.with(this).load(item.imageUrl).into(img)
                indexSet.add(rIndex)
                continue
            }
        }
    }
}

class MyDragListener: View.OnDragListener{
    override fun onDrag(p0: View?, p1: DragEvent?): Boolean {
        TODO("Not yet implemented")
    }

}




