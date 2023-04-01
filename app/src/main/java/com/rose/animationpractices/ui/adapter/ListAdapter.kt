package com.rose.animationpractices.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.rose.animationpractices.databinding.ItemFoodBinding
import com.rose.animationpractices.domain.entity.Food
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ListAdapter @AssistedInject constructor(
    @ApplicationContext
    private val context: Context,
    @Assisted
    private val adapterListener: AdapterListener
) : Adapter<FoodViewHolder>() {

    private val foods: MutableList<Food> = mutableListOf()

    suspend fun setFoods(newFoods: List<Food>) {
        val diffFoods = withContext(Dispatchers.Default) {
            val diffFoods = DiffUtil.calculateDiff(FoodDiffCallback(foods, newFoods))
            foods.clear()
            foods.addAll(newFoods)
            diffFoods
        }
        diffFoods.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
        return FoodViewHolder(ItemFoodBinding.inflate(LayoutInflater.from(context), parent, false), adapterListener)
    }

    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {
        holder.bind(foods[position])
    }

    override fun getItemCount(): Int {
        return foods.size
    }

    override fun getItemId(position: Int): Long {
        return foods[position].id
    }
}