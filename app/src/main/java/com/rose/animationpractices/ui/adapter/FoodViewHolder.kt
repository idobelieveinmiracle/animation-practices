package com.rose.animationpractices.ui.adapter

import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.rose.animationpractices.databinding.ItemFoodBinding
import com.rose.animationpractices.domain.entity.Food

class FoodViewHolder(
    private val binding: ItemFoodBinding,
    private val adapterListener: AdapterListener
) : ViewHolder(binding.root) {
    fun bind(food: Food) {
        binding.food = food
        if (food.imageUri.isNotEmpty()) {
            binding.image.transitionName = food.imageUri
        } else {
            binding.image.transitionName = "empty_image"
        }
        binding.root.setOnClickListener {
            adapterListener.navigateFoodDetails(food, binding.image)
        }
    }
}