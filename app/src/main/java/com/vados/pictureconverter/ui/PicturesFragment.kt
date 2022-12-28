package com.vados.pictureconverter.ui

import android.content.Intent
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
import okhttp3.MediaType.Companion.toMediaType

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
    ): View {
        _binding = FragmentPicturesBinding.inflate(inflater,container,false)
        Log.v("@@@", "PicturesFragment")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonChoose.setOnClickListener{
            //Вызываем стандартную галерею для выбора изображения с помощью Intent.ACTION_PICK:
            val intent = Intent(Intent.ACTION_PICK)
            //Тип получаемых объектов - image:
            intent.type = "image/*"
            //Запускаем переход с ожиданием обратного результата в виде информации об изображении:
            startActivityForResult(intent,it.id)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.v("@@@","onActivityResult")
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