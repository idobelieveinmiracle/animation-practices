package com.rose.animationpractices.ui.adapter

import androidx.recyclerview.widget.DiffUtil
import com.rose.animationpractices.domain.entity.Food

class FoodDiffCallback(private val oldFoods: List<Food>, private val newFoods: List<Food>) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldFoods.size
    }

    override fun getNewListSize(): Int {
        return newFoods.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldFoods[oldItemPosition].id == newFoods[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldFood = oldFoods[oldItemPosition]
        val newFood = newFoods[newItemPosition]

        return oldFood.title == newFood.title &&
                oldFood.description == newFood.description &&
                oldFood.imageUri == newFood.imageUri
    }
}