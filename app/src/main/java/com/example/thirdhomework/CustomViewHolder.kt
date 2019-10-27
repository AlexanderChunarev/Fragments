package com.example.thirdhomework

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.list_item.view.*

class CustomViewHolder(view: View, private val onItemListener: OnItemListener) :
    RecyclerView.ViewHolder(view) {
    val image: ImageView = view.phone_image
    val name: TextView = view.name
    val description: TextView = view.description
    val listListener = view.setOnClickListener {
        onItemListener.onClickItem(adapterPosition)
    }
}