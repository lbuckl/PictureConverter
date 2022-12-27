package com.vados.pictureconverter

import com.github.terrakok.cicerone.Router
import com.vados.pictureconverter.ui.navigation.AndroidScreens
import moxy.MvpPresenter

/**
 * Презенрёр связывает между собой модель и вью
 * @param router - отвечает за навигацию cicerone
 * [onFirstViewAttach] - выполняет действие при первом присоединении View
 * [backClicked] - дейстиве при нажатии кнопки "Назад"
 */
class MainPresenter(private val router: Router): MvpPresenter<MainView>() {

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        router.replaceScreen(AndroidScreens.pictures())
    }

    /**
     * Команда роутеру на действие "назад"
     */
    fun backClicked() {
        router.exit()
    }
}