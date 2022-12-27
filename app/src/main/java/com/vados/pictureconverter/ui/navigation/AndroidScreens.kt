package com.vados.pictureconverter.ui.navigation

import com.github.terrakok.cicerone.Screen
import com.github.terrakok.cicerone.androidx.FragmentScreen
import com.vados.pictureconverter.ui.PicturesFragment

/**
 * Класс для объявления экранов в презентёре
 */
object AndroidScreens : IScreens {
    //Выполняет прикрепление фрагмента PicturesFragment
    override fun pictures() = FragmentScreen { PicturesFragment.newInstance() }
}
