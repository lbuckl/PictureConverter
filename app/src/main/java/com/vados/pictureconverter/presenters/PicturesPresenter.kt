package com.vados.pictureconverter.presenters

import com.github.terrakok.cicerone.Router
import com.vados.pictureconverter.model.GalleryRepo
import moxy.MvpPresenter
import moxy.ktx.MoxyKtxDelegate

class PicturesPresenter(private val usersRepo: GalleryRepo, private val router: Router): MvpPresenter<PicturesView>() {
}