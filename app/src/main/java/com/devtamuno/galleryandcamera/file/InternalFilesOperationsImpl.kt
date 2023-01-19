package com.devtamuno.galleryandcamera.file

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.devtamuno.galleryandcamera.data.InternalStoragePhoto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException

class InternalFilesOperationsImpl(private val context: Context) : FileOperations {

    override suspend fun loadPhotosFromStorage(): List<InternalStoragePhoto> {
        return withContext(Dispatchers.IO) {
            val files = context.filesDir.listFiles()
            files?.filter { it.canRead() && it.isFile && it.name.endsWith(".jpg") }?.map {
                val bytes = it.readBytes()
                val bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                InternalStoragePhoto(it.name, bmp)
            } ?: emptyList()
        }
    }

    override suspend fun savePhotoToStorage(filename: String, bmp: Bitmap): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                context.openFileOutput("$filename.jpg", Context.MODE_PRIVATE).use { stream ->
                    if (!bmp.compress(Bitmap.CompressFormat.JPEG, 95, stream)) {
                        throw IOException("Couldn't save bitmap.")
                    }
                }
                true
            } catch (e: IOException) {
                e.printStackTrace()
                false
            }
        }
    }

    override suspend fun deletePhotoFromStorage(filename: String): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                context.deleteFile(filename)
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }
    }
}