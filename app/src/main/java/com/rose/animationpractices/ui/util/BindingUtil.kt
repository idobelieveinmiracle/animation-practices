package com.rose.animationpractices.ui.util

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.Glide

@BindingAdapter("animating")
fun setAnimating(view: LottieAnimationView, animating: Boolean) {
    if (animating) {
        view.playAnimation()
    } else {
        view.pauseAnimation()
    }
}

@BindingAdapter("imageUri", "error")
fun loadImage(view: ImageView, uri: String?, error: Drawable) {
    Glide.with(view.context)
        .load(uri)
        .centerCrop()
        .placeholder(error)
        .into(view)
}