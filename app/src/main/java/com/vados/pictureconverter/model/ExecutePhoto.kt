package com.vados.pictureconverter.model

import androidx.activity.result.ActivityResultLauncher

interface ExecutePhoto {
    val launcher: ActivityResultLauncher<Int>
}