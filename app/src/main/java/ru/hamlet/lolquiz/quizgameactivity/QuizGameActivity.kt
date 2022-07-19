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
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import ru.hamlet.lolquiz.LolItem
import ru.hamlet.lolquiz.MyDragShadowBuilder
import ru.hamlet.lolquiz.R
import ru.hamlet.lolquiz.TagID


class QuizGameActivity() : AppCompatActivity() {

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

        val slotsImageViews = mutableListOf<ImageView>(
            findViewById(R.id.componentImg0),
            findViewById(R.id.componentImg1),
            findViewById(R.id.componentImg2)
        )
        viewModel.slotsLiveData.observe(this) { slots ->
            for (i in 0 until slotsImageViews.size) {
                val slotImage = slotsImageViews[i]
                if (i >= slots.size) {
                    slotImage.visibility = View.GONE
                } else {
                    slotImage.visibility = View.VISIBLE

                    val slotData = slots[i]
                    if (slotData == null) {
                        slotImage.setImageResource(R.drawable.frame)
                    } else {
                        slotImage.displayLolItemImage(slotData)
                    }
                }
            }
        }

    }

    private fun displayItemVariants(variants: List<LolItem>) {
        for (i in variants.indices) {
            val lolItem = variants[i]

            val id = resources.getIdentifier("img${i}", "id", packageName)
            val variantImg = findViewById<ImageView>(id)
            variantImg.displayLolItemImage(lolItem)

            // make one function 'addDragSupport' hz // addDragSupportForVariants
            variantImg.addDragSupportForVariants(i, lolItem)
        }
    }

    private fun ImageView.addDragSupportForVariants(variantIndex: Int, lolItem: LolItem){
        this.setLongClickListenerForDragAndDrop(variantIndex, lolItem = lolItem)
        this.setDragListenerForVariant(lolItem)
    }

    private fun ImageView.setDragListenerForVariant(lolItem: LolItem) {
        setOnDragListener { view, dragEvent ->
//  TODO make proper drag event handling for image variant
            view as ImageView
            when (dragEvent.action) {
                DragEvent.ACTION_DRAG_STARTED -> {
                    println("draggg started ${lolItem.id}")
//                    view.setImageResource(R.drawable.frame)
                    true

                }
                DragEvent.ACTION_DROP -> {
                    println("draggg variant dropped ")
//                    view.displayLolItemImage(lolItem)
                    true
                }
                DragEvent.ACTION_DRAG_ENDED -> {
                    println("draggg variant ended ")
                    true
                }
                else -> {
                    false
               }
            }
        }
        true
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
            tag = TagID(lolItem.id, variantIndex)

            setOnLongClickListener { v ->

                val item = ClipData.Item(v.tag as? CharSequence)

                val dragData = ClipData(
                    v.tag as? CharSequence, //add data about variant image we are dragging// class TagID
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
//            val draggableItem = dragEvent.localState as View
//            when (dragEvent.action) {
//                DragEvent.ACTION_DRAG_STARTED -> {
//                    println("draggg started ")
//                    true
//                }
//                DragEvent.ACTION_DRAG_ENTERED -> {
//                    println("draggg entered ")
//                    view.invalidate()
//                    true
//                }
//                DragEvent.ACTION_DRAG_LOCATION -> {
//                    println("draggg location? wtf is tis? ")
//                    true
//                }
//                DragEvent.ACTION_DRAG_EXITED -> {
//                    println("draggg exited ")
//                    true
//                }
//                DragEvent.ACTION_DROP -> {
//                    println("draggg dropped ")
//                    true
//                }
//                DragEvent.ACTION_DRAG_ENDED -> {
//                    println("draggg ended ")
//                    true
//                }
//                else -> {
//                    println("draggg falsed ")
//                    false
//               }
//            }
//        }

