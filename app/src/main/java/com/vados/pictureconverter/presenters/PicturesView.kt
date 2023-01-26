package com.vados.pictureconverter.presenters

import android.net.Uri
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
    //Функция отображения ошибки
    fun showError(message: String)
    //Функция отображения иформационного сообщения
    fun showInfo(message: String)
    //Функция отображения фото
    fun displayImage()
    //Функция отображения прогресс бара
    fun showProgress()
    //Функция скрытия прогресс бара
    fun hideProgress()

    fun convertToPngAndSave(imageName: String)
}