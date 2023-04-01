package com.rose.animationpractices.domain.data_helper

interface ImageHelper {
    suspend fun saveUriToFile(uri: String): String
    fun getImageUri(fileName: String): String
}