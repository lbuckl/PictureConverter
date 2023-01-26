package com.vados.pictureconverter.model

import android.content.ContentResolver
import android.graphics.Bitmap
import androidx.activity.result.ActivityResultLauncher

interface IConvertAndSaveImage {
    val converter: PictureConverter
}