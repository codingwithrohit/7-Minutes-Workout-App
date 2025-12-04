package com.example.a7minutesworkoutapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.a7minutesworkoutapp.R
import com.example.a7minutesworkoutapp.data.entity.HistoryEntity
import com.example.a7minutesworkoutapp.databinding.ItemHistoryRowBinding

class HistoryAdapter(
    private val items: ArrayList<HistoryEntity>,
    private val onDeleteClick: (HistoryEntity) -> Unit
) : RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    inner class ViewHolder(binding: ItemHistoryRowBinding) :
        RecyclerView.ViewHolder(binding.root) {

        val llHistoryItem = binding.llHistoryItem
        val tvPosition = binding.tvPosition
        val tvDate = binding.tvDate
        val tvName = binding.tvName
        val ibDelete = binding.ibDelete
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemHistoryRowBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        holder.tvPosition.text = (position + 1).toString()
        holder.tvDate.text = item.date
        holder.tvName.text = item.name

        holder.ibDelete.setOnClickListener {
            onDeleteClick(item)
        }

        if (position % 2 == 0) {
            holder.llHistoryItem.setBackgroundResource(R.drawable.rounded_background_even)
        } else {
            holder.llHistoryItem.setBackgroundResource(R.drawable.rounded_background_odd)
        }
    }

    override fun getItemCount(): Int = items.size
}