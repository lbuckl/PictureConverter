package com.vados.pictureconverter.presenters

import com.github.terrakok.cicerone.Router
import moxy.MvpPresenter

class PicturesPresenter(private val router: Router): MvpPresenter<PicturesView>() {

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.init()
    }

    fun downloadImageFromStorage(){

    }

    fun backPressed(): Boolean {
        router.exit()
        return true
    }
}