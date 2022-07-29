package ru.hamlet.lolquiz

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

import com.bumptech.glide.Glide

class MyAdapter(
    val items: MutableList<LolItem>,

): RecyclerView.Adapter<MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.lol_item_view, parent, false)
        val holder = MyViewHolder(view)
        return holder
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val itemNameTextView = holder.itemView.findViewById<TextView>(R.id.itemName)
        val itemPriceTextView = holder.itemView.findViewById<TextView>(R.id.itemPrice)
        val imageView = holder.itemView.findViewById<ImageView>(R.id.image)

        val lolItem = items[position]

        itemNameTextView.text = lolItem.name
        itemPriceTextView.text = lolItem.price.toString()
        Glide.with(imageView.context).load(lolItem.imageUrl).into(imageView)
    }

    override fun getItemCount() = items.size


    fun replace(list: List<LolItem>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

}

class MyViewHolder(myView: View): RecyclerView.ViewHolder(myView){

}

