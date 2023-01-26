package com.vados.pictureconverter.ui

import android.content.Context
import android.content.res.Resources.NotFoundException
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
import coil.load
import com.vados.pictureconverter.App
import com.vados.pictureconverter.R
import com.vados.pictureconverter.databinding.FragmentPicturesBinding
import com.vados.pictureconverter.model.IConvertAndSaveImage
import com.vados.pictureconverter.model.IPictureRequest
import com.vados.pictureconverter.model.PictureConverter
import com.vados.pictureconverter.model.PictureRequest
import com.vados.pictureconverter.presenters.PicturesPresenter
import com.vados.pictureconverter.presenters.PicturesView
import com.vados.pictureconverter.utils.ERROR_FILE_NAME
import com.vados.pictureconverter.utils.IMAGE_FILE_NAME
import com.vados.pictureconverter.utils.PREF_SAVE_IMAGE
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.schedulers.Schedulers
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter
import java.io.IOException

/**
 * Фрагмент для взаимодействия и выполнения операций
 * пользователя с изображениями из Галереи
 */
class PicturesFragment:
    MvpAppCompatFragment(),
    PicturesView,
    BackButtonListener,
    IPictureRequest,
    IConvertAndSaveImage{

    private val photoID = 1 // произвольный айди фото

    //Презентёр для фрагмента
    private val presenter: PicturesPresenter by moxyPresenter {
        PicturesPresenter(App.instance.router)
    }

    override val converter = PictureConverter()

    //Коллбэк от активити получающей фото из галереи
    override var launcher = registerForActivityResult(PictureRequest()){ uri ->
        uri?.let {
            showProgress()

            val imageName = uri.toString().split("/").last()

            Completable.create{ emitter ->
                try {
                    saveUri(uri)
                    emitter.onComplete()
                }catch (e: IOException){
                    emitter.onError(Throwable("Trying get Image:Error"))
                }catch (e: NullPointerException){
                    emitter.onError(Throwable("File not found:Error"))
                }
            }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                {
                    presenter.hideLoadingState()
                },
                {
                    presenter.hideLoadingState()
                    showError("Ошибка загрузки изображения")
                }
            )

            presenter.showImage(imageName)
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
            presenter.convertAndSaveImage()
        }
    }

    /**
     * Функция инициализирующая состояние фаргмента при запуске или восстановлении фрагмента
     */
    override fun init() {
        try { // берём дефолтное изображение, если не получилось из uri
            binding.imageView.load(ResourcesCompat.getDrawable(
                requireContext().resources,
                R.drawable.picture_earth,
                requireContext().theme
            ))
        }catch (e: NotFoundException){
            e.printStackTrace()
        }
    }

    /**
     * Функция отображения информации об ошибке для пользователя
     */
    override fun showError(message: String) {
        hideProgress()
        Toast.makeText(requireContext(),"Error: $message",Toast.LENGTH_SHORT).show()
    }

    /**
     * Функция отображения информационного сообщения для пользователя
     */
    override fun showInfo(message: String) {
        hideProgress()
        Toast.makeText(requireContext(),message,Toast.LENGTH_LONG).show()
    }

    /**
     * Функция отображения изображения полученного из галереи
     */
    override fun displayImage() {

        // uri последнего изображения
        val uriString = requireContext()
            .getSharedPreferences(PREF_SAVE_IMAGE, Context.MODE_PRIVATE)
            .getString(IMAGE_FILE_NAME, ERROR_FILE_NAME)
        if (uriString != ERROR_FILE_NAME){
            try { // берём дефолтное изображение, если не получилось из uri
                binding.imageView.load(uriString)
            }catch (e: NotFoundException){
                e.printStackTrace()
            }
        }
        else {
            try { // берём дефолтное изображение, если не получилось из uri
                binding.imageView.load(ResourcesCompat.getDrawable(
                    requireContext().resources,
                    R.drawable.picture_earth,
                    requireContext().theme
                ))
            }catch (e: NotFoundException){
                e.printStackTrace()
            }
        }
    }

    /**
     * Отображение прогрес бара при загрузке
     */
    override fun showProgress() {
        binding.progressBar.visibility = View.VISIBLE
    }

    /**
     * Скрытие прогрес бара при окончании загрузки
     */
    override fun hideProgress() {
        binding.progressBar.let {
            if (it.visibility != View.GONE) it.visibility = View.GONE
        }
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

    /**
     * функция нажатия кнопки "Назад"
     */
    override fun backPressed() = presenter.backPressed()


    override fun convertToPngAndSave(imageName: String) {
        Completable.create{ emitter ->
            if (converter.convertToPngAndSave(
                    binding.imageView.drawable.toBitmap(),
                    imageName,
                    requireActivity().contentResolver)
            ) emitter.onComplete()
            else emitter.onError(Throwable("Convert image error"))
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    presenter.hideLoadingState()
                    showInfo("Конвертирование завершено")
                },
                {
                    presenter.hideLoadingState()
                    showError("Ошибка конвертирования")
                }
            )
    }

    //Функция сохранения URI в getSharedPreferences
    private fun saveUri(uri: Uri?){
        val sharedPrefer =
            App.instance.getSharedPreferences(PREF_SAVE_IMAGE, Context.MODE_PRIVATE)

        val editor = sharedPrefer.edit()

        editor.putString(
            IMAGE_FILE_NAME,
            uri.toString()
        ).apply()
    }
}