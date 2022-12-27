package com.vados.pictureconverter

import android.util.Log
import com.github.terrakok.cicerone.Router
import com.vados.pictureconverter.ui.navigation.AndroidScreens
import moxy.MvpPresenter

class MainPresenter(private val router: Router): MvpPresenter<MainView>() {
    override fun onFirstViewAttach() {
        Log.v("@@@","MainPresenter")
        router.replaceScreen(AndroidScreens.pictures())
        super.onFirstViewAttach()
    }
}