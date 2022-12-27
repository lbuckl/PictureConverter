package com.vados.pictureconverter.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.vados.pictureconverter.App
import com.vados.pictureconverter.databinding.FragmentPicturesBinding
import com.vados.pictureconverter.presenters.PicturesPresenter
import com.vados.pictureconverter.presenters.PicturesView
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter

class PicturesFragment: MvpAppCompatFragment(),PicturesView, BackButtonListener {

    companion object {
        fun newInstance() = PicturesFragment()
    }

    private val presenter: PicturesPresenter by moxyPresenter {
        PicturesPresenter(App.instance.router)
    }

    private var _binding: FragmentPicturesBinding? = null
    private val binding: FragmentPicturesBinding
        get() {
            return _binding!!
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPicturesBinding.inflate(inflater,container,false)
        Log.v("@@@", "PicturesFragment")
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun init() {
        //TODO("Not yet implemented")
    }

    override fun updateList() {
        //TODO("Not yet implemented")
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

    override fun backPressed() = presenter.backPressed()
}