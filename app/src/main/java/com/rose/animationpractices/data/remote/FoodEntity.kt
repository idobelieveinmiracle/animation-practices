package com.rose.animationpractices.data.remote

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.rose.animationpractices.domain.entity.Food

@Entity(tableName = "food")
data class FoodEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val title: String,
    val description: String,
    @ColumnInfo(name = "created_time")
    val createdTime: Long,
    @ColumnInfo(name = "image_path")
    val imagePath: String
) {
    companion object {
        fun fromFood(food: Food): FoodEntity = FoodEntity(
            food.id,
            food.title,
            food.description,
            food.createdTime,
            food.imageFileName
        )
    }

    fun toFood(): Food = Food(
        id = id,
        title = title,
        description = description,
        createdTime = createdTime,
        imageFileName = imagePath
    )
}
