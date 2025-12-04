package com.example.a7minutesworkoutapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.graphics.toColorInt
import androidx.recyclerview.widget.RecyclerView
import com.example.a7minutesworkoutapp.R
import com.example.a7minutesworkoutapp.databinding.ItemExerciseStatusBinding
import com.example.a7minutesworkoutapp.domain.model.ExerciseModal

class ExerciseStatusAdapter(val items: ArrayList<ExerciseModal>):
RecyclerView.Adapter<ExerciseStatusAdapter.ViewHolder>(){

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        //return ViewHolder we created
        return ViewHolder(
            ItemExerciseStatusBinding.inflate(
            LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        val model: ExerciseModal = items[position]
        holder.tvItem.text = model.id.toString()

        when{
            model.isSelected -> {
                holder.tvItem.background = ContextCompat.getDrawable(
                    holder.itemView.context,
                    R.drawable.item_exercise_status_circular_selected
                )
                holder.tvItem.setTextColor("#212121".toColorInt())
            }
            model.isCompleted ->{
                holder.tvItem.background = ContextCompat.getDrawable(holder.itemView.context,
                    R.drawable.item_exercise_status_circular_completed)
                holder.tvItem.setTextColor("#FFFFFF".toColorInt())
            }
            else ->{
                holder.tvItem.background = ContextCompat.getDrawable(holder.itemView.context,
                    R.drawable.item_exercise_status_circular_gray_color)
                holder.tvItem.setTextColor("#FFFFFF".toColorInt())
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class ViewHolder(binding: ItemExerciseStatusBinding): RecyclerView.ViewHolder(binding.root){

        val tvItem = binding.tvItem
    }
}