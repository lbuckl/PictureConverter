package com.vados.pictureconverter.ui

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.graphics.drawable.toBitmap
import coil.load
import com.vados.pictureconverter.App
import com.vados.pictureconverter.databinding.FragmentPicturesBinding
import com.vados.pictureconverter.model.ExecutePhoto
import com.vados.pictureconverter.model.PictureRequest
import com.vados.pictureconverter.presenters.PicturesPresenter
import com.vados.pictureconverter.presenters.PicturesView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter

class PicturesFragment: MvpAppCompatFragment(),PicturesView, BackButtonListener, ExecutePhoto {

    private val photoID = 1
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    private val presenter: PicturesPresenter by moxyPresenter {
        PicturesPresenter(App.instance.router)
    }

    override var launcher = registerForActivityResult(PictureRequest()){ uri ->
        uri?.let {
            val fileName = it.toString().split("/").last()
            presenter.showImage(uri, fileName)
        }
    }

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
    ): View {
        _binding = FragmentPicturesBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Загружаем .jpg изображение из галереи
        initButtonChoose()
        //Конвертируем в .png и сохраняем
        initButtonConvert()
    }

    /**
     * Кнопка реализующая функицю чтения фото из памяти смартфона
     */
    private fun initButtonChoose(){
        binding.buttonChoose.setOnClickListener{
            launcher.launch(photoID)
        }
    }

    /**
     * Кнопка реализующая функицю конвертирования из .jpg в .png
     * и загрузку изображения в память смартфона
     */
    private fun initButtonConvert(){
        binding.buttonConvert.setOnClickListener {
            presenter.convertToPngAndSave(binding.imageView.drawable.toBitmap(),requireActivity().contentResolver)
        }
    }

    override fun init() {
        //TODO("Not yet implemented")
    }

    override fun showError(message: String) {
        goneProgressBar()
        Toast.makeText(requireContext(),message,Toast.LENGTH_SHORT).show()
    }

    override fun showInfo(message: String) {
        goneProgressBar()
        Toast.makeText(requireContext(),message,Toast.LENGTH_LONG).show()
    }

    override fun displayImage(uri: Uri?) {
        if (uri != null) binding.imageView.load(uri)
        else showError("Ошибка загрузки")
    }

    override fun showProgress() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun goneProgressBar(){
        binding.progressBar.let {
            if (it.visibility != View.GONE) it.visibility = View.GONE
        }
    }


    override fun onDestroy() {
        _binding = null
        coroutineScope.cancel()
        super.onDestroy()
    }

    override fun backPressed() = presenter.backPressed()
}