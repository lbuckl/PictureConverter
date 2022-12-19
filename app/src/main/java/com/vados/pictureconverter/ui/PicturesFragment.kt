package com.vados.pictureconverter.ui

import com.vados.pictureconverter.presenters.PicturesView
import moxy.MvpAppCompatFragment

class PicturesFragment: MvpAppCompatFragment(),PicturesView {

    companion object {
        fun newInstance() = PicturesFragment()
    }


    override fun init() {
        TODO("Not yet implemented")
    }

    override fun updateList() {
        TODO("Not yet implemented")
    }
}