package com.example.thirdhomework.fragments

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.thirdhomework.listener.OnItemListener
import kotlinx.android.synthetic.main.list_item.view.*

class CustomViewHolder(view: View, private val onItemListener: OnItemListener) :
    RecyclerView.ViewHolder(view) {
    val image: ImageView = view.cpu_image
    val name: TextView = view.name
    val listListener = view.setOnClickListener {
        onItemListener.onClickItem(adapterPosition)
    }
}