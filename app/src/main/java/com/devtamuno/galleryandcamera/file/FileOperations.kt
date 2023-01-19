package com.devtamuno.galleryandcamera.file

import android.graphics.Bitmap
import com.devtamuno.galleryandcamera.data.InternalStoragePhoto

interface FileOperations {

    suspend fun loadPhotosFromStorage(): List<InternalStoragePhoto>

    suspend fun savePhotoToStorage(filename: String, bmp: Bitmap): Boolean

    suspend fun deletePhotoFromStorage(filename: String): Boolean
}