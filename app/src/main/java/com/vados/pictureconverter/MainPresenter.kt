package com.vados.pictureconverter

import com.github.terrakok.cicerone.Router
import com.vados.pictureconverter.ui.navigation.AndroidScreens
import moxy.MvpPresenter

class MainPresenter(private val router: Router): MvpPresenter<MainView>() {
    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        router.replaceScreen(AndroidScreens.pictures())
    }
}