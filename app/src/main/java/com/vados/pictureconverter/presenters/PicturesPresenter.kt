package com.vados.pictureconverter.presenters

import com.github.terrakok.cicerone.Router
import io.reactivex.rxjava3.disposables.Disposable
import moxy.MvpPresenter

/**
 * Презентёр для фрагмента отображения фото и управление конвертацией
 * из jpg в png
 */
class PicturesPresenter(
    private val router: Router
): MvpPresenter<PicturesView>() {

    //переменные Disposable
    private  lateinit var disposableChooseImage: Disposable
    private  lateinit var disposableSaveImage: Disposable


    /**
     * Первое действие при запуске приложения
     */
    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.init()
    }

    /**
     * Функция отображения фото по запрошенному uri
     */
    fun showImage(){
        viewState.displayImage()
    }

    fun convertAndSaveImage(){
        setLoadingState()
        viewState.convertToPngAndSave()
    }

    private fun setLoadingState(){
        viewState.showProgress()
    }

    fun hideLoadingState(){
        viewState.hideProgress()
    }

    //Дейтсиве при нажатии кнопки "Назад"
    fun backPressed(): Boolean {
        router.exit()
        return true
    }
}