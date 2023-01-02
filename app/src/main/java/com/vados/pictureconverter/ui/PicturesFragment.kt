package com.vados.pictureconverter.ui

import android.content.ContentValues
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment.DIRECTORY_PICTURES
import android.os.Environment.getExternalStoragePublicDirectory
import android.provider.MediaStore
import android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
import android.provider.MediaStore.MediaColumns.*
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.FileProvider
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
import kotlinx.coroutines.launch
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

class PicturesFragment: MvpAppCompatFragment(),PicturesView, BackButtonListener, ExecutePhoto {

    private var fileName = ""
    private val photoID = 1
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    private val presenter: PicturesPresenter by moxyPresenter {
        PicturesPresenter(App.instance.router)
    }

    override var launcher = registerForActivityResult(PictureRequest()){ uri ->
        uri?.let {
            fileName = it.toString().split("/").last()
            presenter.showImage(uri)
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
            coroutineScope.launch {
                Log.v("@@@","Начал сохранение")
                val imageBitMap = binding.imageView.drawable.toBitmap()
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) saveImageInQ(imageBitMap)
                else legacySave(imageBitMap)
            }
        }
    }

    //Make sure to call this function on a worker thread, else it will block main thread
    @RequiresApi(Build.VERSION_CODES.Q)
    private fun saveImageInQ(bitmap: Bitmap): Uri {
        val filename = "$fileName.png"
        var fos: OutputStream?
        var imageUri: Uri?
        val contentValues = ContentValues().apply {
            put(DISPLAY_NAME, filename)
            put(MIME_TYPE, "image/png")
            put(RELATIVE_PATH, DIRECTORY_PICTURES)
            put(MediaStore.Video.Media.IS_PENDING, 1)
        }

        //use application context to get contentResolver
        val contentResolver = requireActivity().contentResolver

        contentResolver.also { resolver ->
            imageUri = resolver.insert(EXTERNAL_CONTENT_URI, contentValues)
            fos = imageUri?.let { resolver.openOutputStream(it) }
        }

        fos?.use { bitmap.compress(Bitmap.CompressFormat.PNG, 100, it) }

        contentValues.clear()
        contentValues.put(MediaStore.Video.Media.IS_PENDING, 0)
        contentResolver.update(imageUri!!, contentValues, null, null)

        return imageUri!!
    }

    //Make sure to call this function on a worker thread, else it will block main thread
    private fun legacySave(bitmap: Bitmap): Uri {
        val directory = getExternalStoragePublicDirectory(DIRECTORY_PICTURES)
        val file = File(directory, "$fileName.png")
        val outStream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream)
        outStream.flush()
        outStream.close()
        MediaScannerConnection.scanFile(requireContext(), arrayOf(file.absolutePath),
            null, null)
        return FileProvider.getUriForFile(requireContext(), "${requireContext().packageName}.provider",
            file)
    }

    override fun init() {
        //TODO("Not yet implemented")
    }

    override fun displayImage(uri: Uri?) {
        if (uri != null) binding.imageView.load(uri)
        else Toast.makeText(requireContext(),"Ошибка загрузки",Toast.LENGTH_SHORT).show()
    }


    override fun onDestroy() {
        _binding = null
        coroutineScope.cancel()
        super.onDestroy()
    }

    override fun backPressed() = presenter.backPressed()
}