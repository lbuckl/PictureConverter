package com.vados.pictureconverter.presenters

import android.net.Uri
import androidx.activity.result.ActivityResultLauncher
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

/**
 * Интерфейс для связи презентёра с View
 * AddToEndSingleStrategy - добавит пришедшую команду в конец очереди команд.
 * Если команда такого типа уже есть в очереди, то действующая удалится
 */
@StateStrategyType(AddToEndSingleStrategy::class)
interface PicturesView:MvpView {
    //Функция инициализации фрагмента
    fun init()

    fun showError(message: String)
    //Функция отображения фото
    fun displayImage(uri: Uri?)
}