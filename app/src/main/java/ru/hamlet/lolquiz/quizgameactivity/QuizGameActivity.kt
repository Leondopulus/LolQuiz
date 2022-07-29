package ru.hamlet.lolquiz.quizgameactivity

import android.content.ClipData
import android.content.ClipDescription
import android.os.Bundle
import android.util.Log
import android.view.DragEvent
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import org.json.JSONObject
import ru.hamlet.lolquiz.LolItem
import ru.hamlet.lolquiz.MyDragShadowBuilder
import ru.hamlet.lolquiz.R
import ru.hamlet.lolquiz.TagID


class QuizGameActivity() : AppCompatActivity() {
    private val LOG_TAG = "QuizActivity"
    private val viewModel: QuizViewModel by viewModel()

    //////////////////////////////////////////////////
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz_game)

        viewModel.onCreate()

        findViewById<Button>(R.id.okButton).setOnClickListener {
            viewModel.onOkButtonClick()
        }

        viewModel.questItemLiveData.observe(this) { lolItem ->
            setQuestItem(lolItem)

        }

        viewModel.levelComponentVariantsLiveData.observe(this) { variants ->
            displayItemVariants(variants)
        }

        viewModel.slotsLiveData.observe(this) { slots ->
            showSlots(slots)
        }
    }

    private fun showSlots(slotItems: List<LolItem?>) {
        val slots = mutableListOf<ImageView>(
            findViewById(R.id.componentImg0),
            findViewById(R.id.componentImg1),
            findViewById(R.id.componentImg2)
        )

//        val slotsComponents = lolItem.lvl1Components

        for (i in 0 until slots.size) {
            val slotImage = slots[i]

            if (i >= slotItems.size) {
                slotImage.visibility = View.GONE
            } else {
                slotImage.visibility = View.VISIBLE
                val slotData = slotItems[i]
                if (slotData == null){
                    slotImage.setImageResource(R.drawable.frame50)
                    slotImage.setDragListenerForSlots(i)
                }else{
                    slotImage.displayLolItemImage(slotData)
                }
            }
        }
    }

    private fun displayItemVariants(variants: List<LolItem?>) {
        for (i in variants.indices) {
            val lolItem = variants[i]
            val id = resources.getIdentifier("img${i}", "id", packageName)
            val variantImg = findViewById<ImageView>(id)
            if (lolItem != null ){

                variantImg.displayLolItemImage(lolItem)
                variantImg.addDragSupportForVariants(i, lolItem)
            }else{
                variantImg.setImageResource(R.drawable.frame50)
            }
        }
    }

    private fun ImageView.addDragSupportForVariants(variantIndex: Int, lolItem: LolItem) {
        this.setLongClickListenerForDragAndDrop(variantIndex, lolItem = lolItem)
        this.setDragListenerForVariant(lolItem)
    }

    private fun ImageView.setDragListenerForVariant(lolItem: LolItem) {
        setOnDragListener { view, dragEvent ->
//  TODO make proper drag event handling for image variant
            view as ImageView
            return@setOnDragListener when (dragEvent.action) {
                DragEvent.ACTION_DRAG_STARTED -> {
                    Log.d(LOG_TAG, "variant action started: ${dragEvent.clipData}")
        //                    view.setImageResource(R.drawable.frame)
                    true

                }
                DragEvent.ACTION_DROP -> {
        //                    view.displayLolItemImage(lolItem)
                    Log.d(LOG_TAG, "variant action droped: ${dragEvent.clipData}")
                    true
                }
                DragEvent.ACTION_DRAG_ENDED -> {
                    true
                }
                else -> {
                    false
                }
            }
        }

    }

    private fun ImageView.setDragListenerForSlots(slotIndex: Int) {
        setOnDragListener { view, dragEvent ->
            view as ImageView
            return@setOnDragListener when (dragEvent.action) {
                DragEvent.ACTION_DRAG_STARTED -> {
                    true

                }
                DragEvent.ACTION_DROP -> {

                    val jsonString = dragEvent.clipData.description.label.toString()
                    val jsonObj = JSONObject(jsonString)
                    val tagID = TagID(jsonObj)
//                    Log.d(LOG_TAG, "slot action droped: ${tagID}")
                    viewModel.onSlotDropped(tagID, slotIndex)
                    true
                }
                DragEvent.ACTION_DRAG_ENDED -> {
                    true
                }
                else -> {
                    false
                }
            }
        }
    }

    private fun ImageView.displayLolItemImage(item: LolItem) {
        Glide.with(this)
            .applyDefaultRequestOptions(
                RequestOptions()
                    .placeholder(R.drawable.item0)
//                    .error(R.drawable.ic_user_default)
            )
            .load(item.imageUrl).into(this)
    }

    private fun ImageView.setLongClickListenerForDragAndDrop(variantIndex: Int, lolItem: LolItem) {
        this.apply {
//            tag = TagID(lolItem.id, variantIndex)

            setOnLongClickListener { v ->
                val data = TagID(lolItem.id, variantIndex).toJSON().toString()
                val item = ClipData.Item(data)

                val dragData = ClipData(
                    data, //add data about variant image we are dragging// class TagID
                    arrayOf(ClipDescription.MIMETYPE_TEXT_PLAIN),
                    item
                )



                // Instantiate the drag shadow builder.
                val myShadow = MyDragShadowBuilder(this)

                // Start the drag.
                v.startDragAndDrop(
                    dragData,  // The data to be dragged
                    myShadow,  // The drag shadow builder
                    null,      // No need to use local data
                    0          // Flags (not currently used, set to 0)
                )

                // Indicate that the long-click was handled.
                true
            }
        }
    }

    private fun setQuestItem(lolItem: LolItem) {
        println("debug setQuestItem start")
        val questImg = findViewById<ImageView>(R.id.questImg)
        questImg.displayLolItemImage(lolItem)
    }
}


//          setOnDragListener { view, dragEvent ->
//            when (dragEvent.action) {
//                DragEvent.ACTION_DRAG_STARTED -> {

//                DragEvent.ACTION_DRAG_ENTERED -> {
//                    println("draggg entered ")
//                    view.invalidate()
//                    true
//                }
//                DragEvent.ACTION_DRAG_LOCATION -> {

//                DragEvent.ACTION_DRAG_EXITED -> {

//                DragEvent.ACTION_DROP -> {

//                DragEvent.ACTION_DRAG_ENDED -> {


