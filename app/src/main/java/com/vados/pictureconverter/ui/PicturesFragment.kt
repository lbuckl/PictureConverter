package com.vados.pictureconverter.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.vados.pictureconverter.databinding.FragmentPicturesBinding
import com.vados.pictureconverter.presenters.PicturesView
import moxy.MvpAppCompatFragment

class PicturesFragment: MvpAppCompatFragment(),PicturesView {

    companion object {
        fun newInstance() = PicturesFragment()
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
        Log.v("@@@", "PicturesFragment")
        _binding = FragmentPicturesBinding.inflate(inflater,container,false)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun init() {
        //TODO("Not yet implemented")
    }

    override fun updateList() {
        //TODO("Not yet implemented")
    }
}