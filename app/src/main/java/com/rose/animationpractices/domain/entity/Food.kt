package com.rose.animationpractices.domain.entity

data class Food(
    val id: Long = 0,
    val title: String = "",
    val description: String = "",
    val createdTime: Long = System.currentTimeMillis(),
    val imageUri: String = "",
    val imageFileName: String = ""
)
