package com.example.gridrecyclerviewapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.gridrecyclerviewapp.databinding.ItemRecyclerviewFemaleBinding
import com.example.gridrecyclerviewapp.databinding.ItemRecyclerviewMaleBinding
import com.example.gridrecyclerviewapp.model.Human

class MainRecyclerViewAdapter : ListAdapter<Human, RecyclerView.ViewHolder>(DiffUtils()) {

    private var deleteClick: (item: Human) -> Unit = { }
    private var editClick: (item: Human) -> Unit = { }

    inner class MaleViewHolder(private val binding: ItemRecyclerviewMaleBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Human) = with(binding) {
            tvName.text = item.name
            tvJob.text = item.job
            ivMaleOrFemale.setImageResource(item.logoId)
            ivDelete.setOnClickListener { deleteClick.invoke(item) }
            ivEdit.setOnClickListener { editClick.invoke(item) }
        }
    }

    inner class FemaleViewHolder(private val binding: ItemRecyclerviewFemaleBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Human) = with(binding) {
            tvName.text = item.name
            tvJob.text = item.job
            ivMaleOrFemale.setImageResource(item.logoId)
            ivDelete.setOnClickListener { deleteClick.invoke(item) }
            ivEdit.setOnClickListener { editClick.invoke(item) }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position).gender) {
            true -> FEMALE
            else -> MALE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
            MALE -> MaleViewHolder(
                ItemRecyclerviewMaleBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )

            else -> FemaleViewHolder(
                ItemRecyclerviewFemaleBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is MaleViewHolder -> holder.bind(getItem(position))
            is FemaleViewHolder -> holder.bind(getItem(position))
        }
    }

    fun onDeleteClick(onDeleteClick: (item: Human) -> Unit) {
        this.deleteClick = onDeleteClick
    }

    fun onEditClick(onEditClick: (item: Human) -> Unit) {
        this.editClick = onEditClick
    }

    companion object {
        const val MALE = 0
        const val FEMALE = 1
    }

}