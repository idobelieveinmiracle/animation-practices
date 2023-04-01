package com.rose.animationpractices.data.data_helper

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.util.Log
import android.webkit.MimeTypeMap
import com.rose.animationpractices.domain.data_helper.ImageHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.UUID

class ImageHelperImpl(private val context: Context) : ImageHelper {

    companion object {
        private const val TAG = "ImageHelperImpl"
    }

    override suspend fun saveUriToFile(uri: String): String = withContext(Dispatchers.IO) {
        val inputStream =
            context.contentResolver.openInputStream(Uri.parse(uri)) ?: return@withContext ""

        try {
            val fileName = "image_${UUID.randomUUID()}.${getFileExtension(uri)}"
            val filePath = "${context.filesDir}${File.pathSeparator}$fileName"

            val file = File(filePath)
            file.setWritable(true, true)
            val outputStream = FileOutputStream(file)
            val buffer = ByteArray(1024)
            while (true) {
                val length = inputStream.read(buffer)
                if (length <= 0) {
                    break
                }
                outputStream.write(buffer, 0, length)
            }

            outputStream.close()

            fileName
        } catch (e: IOException) {
            Log.e(TAG, "saveUriToFile: ", e)
            ""
        } finally {
            inputStream.close()
        }
    }

    override fun getImageUri(fileName: String): String {
        if (fileName.isEmpty()) {
            return ""
        }

        val filePath = "${context.filesDir}${File.pathSeparator}$fileName"

        val file = File(filePath)
        return Uri.fromFile(file).toString()
    }

    private fun getFileExtension(uri: String): String {
        val fileUri = Uri.parse(uri)
        return if (fileUri.scheme == ContentResolver.SCHEME_CONTENT) {
            val mime = MimeTypeMap.getSingleton()
            mime.getExtensionFromMimeType(context.contentResolver.getType(fileUri)) ?: ""
        } else {
            MimeTypeMap.getFileExtensionFromUrl(uri)
        }
    }
}