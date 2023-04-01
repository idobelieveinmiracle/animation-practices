package com.rose.animationpractices.ui.adapter

import android.view.View
import com.rose.animationpractices.domain.entity.Food

interface AdapterListener {
    fun navigateFoodDetails(food: Food, imageView: View)
}