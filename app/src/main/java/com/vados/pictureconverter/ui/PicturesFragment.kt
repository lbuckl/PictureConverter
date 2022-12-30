package com.vados.pictureconverter.ui

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import coil.load
import com.vados.pictureconverter.App
import com.vados.pictureconverter.databinding.FragmentPicturesBinding
import com.vados.pictureconverter.model.JpgToPngConverter
import com.vados.pictureconverter.presenters.PicturesPresenter
import com.vados.pictureconverter.presenters.PicturesView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter

class PicturesFragment: MvpAppCompatFragment(),PicturesView, BackButtonListener {

    private val photoID = 1
    private var fileName = ""
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

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
            //Вызываем стандартную галерею для выбора изображения с помощью Intent.ACTION_PICK:
            val intent = Intent(Intent.ACTION_PICK)
            //Тип получаемых объектов - image:
            intent.type = "image/*.jpg"
            //Запускаем переход с ожиданием обратного результата в виде информации об изображении:
            startActivityForResult(intent,photoID)
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK){
            try {
                val image = data?.data
                fileName = image.toString().split("/").last()
                binding.imageView.load(image)
            }catch (e:RuntimeException){
                e.printStackTrace()
            }
        }
    }

    /**
     * Кнопка реализующая функицю конвертирования из .jpg в .png
     * и загрузку изображения в память смартфона
     */
    private fun initButtonConvert(){
        binding.buttonConvert.setOnClickListener {
            coroutineScope.launch {
                Log.v("@@@","Начал сохранение")
                JpgToPngConverter.savePicture(requireContext().filesDir.toString(),
                    requireContext().contentResolver,
                    binding.imageView.drawable,
                    fileName
                )
            }
        }
    }

    override fun init() {
        //TODO("Not yet implemented")
    }

    override fun updateList() {
        //TODO("Not yet implemented")
    }



    override fun onDestroy() {
        _binding = null
        coroutineScope.cancel()
        super.onDestroy()
    }

    override fun backPressed() = presenter.backPressed()
}