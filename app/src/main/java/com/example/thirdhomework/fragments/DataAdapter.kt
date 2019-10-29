package com.example.thirdhomework.fragments

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.example.thirdhomework.listener.OnItemListener
import com.example.thirdhomework.R
import com.example.thirdhomework.data.CPU

class DataAdapter(
    private val processors: MutableList<CPU>,
    private val onItemListener: OnItemListener
) : Adapter<CustomViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        CustomViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.list_item,
                parent,
                false
            ), onItemListener
        )

    override fun getItemCount() = processors.size

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        processors[position].apply {
            holder.image.setImageResource(image)
            holder.name.text = name
            holder.listListener
        }
    }
}

