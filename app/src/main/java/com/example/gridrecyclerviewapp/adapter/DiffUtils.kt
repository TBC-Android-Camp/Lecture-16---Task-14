package com.example.gridrecyclerviewapp.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.gridrecyclerviewapp.model.Human

class DiffUtils : DiffUtil.ItemCallback<Human> () {
    override fun areItemsTheSame(oldItem: Human, newItem: Human) = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Human, newItem: Human) = oldItem == newItem
}