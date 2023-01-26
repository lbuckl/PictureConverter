package com.vados.pictureconverter.model

import androidx.activity.result.ActivityResultLauncher

//Интерфейс для реализации функционала выбора фото из галереи
interface IPictureRequest {
    val launcher: ActivityResultLauncher<Int>
}