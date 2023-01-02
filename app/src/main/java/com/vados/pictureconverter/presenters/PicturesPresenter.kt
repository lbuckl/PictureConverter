package com.vados.pictureconverter.presenters

import android.content.ContentResolver
import android.content.ContentValues
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.annotation.MainThread
import androidx.annotation.RequiresApi
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.FileProvider
import com.github.terrakok.cicerone.Router
import com.vados.pictureconverter.App
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import moxy.MvpPresenter
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.security.Provider

class PicturesPresenter(private val router: Router): MvpPresenter<PicturesView>() {


    private  lateinit var disposableChooseImage: Disposable
    private  lateinit var disposableSaveImage: Disposable
    private var fileName = ""

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.init()
    }

    fun showImage(uri: Uri?, fileNameFromRequest: String){
        disposableChooseImage = Completable.create{ emitter ->
            try {
                //TODO сохранение URI в БД
                fileName = fileNameFromRequest
                emitter.onComplete()
            }catch (e: IOException){
                emitter.onError(Throwable("Trying get Image:Error"))
            }
        }.subscribeOn(Schedulers.io()).subscribe(
            {
                viewState.displayImage(uri)
            },
            {
                viewState.showError("Ошибка загрузки")
            }
        )
    }

    fun convertToPngAndSave(bitmapImage: Bitmap,contentResolver: ContentResolver){
        viewState.showProgress()
        disposableSaveImage = Completable.create{ emitter ->
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    val res = saveImageInQ(bitmapImage,contentResolver)
                    if (res) emitter.onComplete()
                    else emitter.onError(Throwable("Trying convert and save image:Error"))
                }
                else {
                    val res = legacySave(bitmapImage)
                    if (res) emitter.onComplete()
                    else emitter.onError(Throwable("Trying convert and save image:Error"))
                }
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
            {
                viewState.showInfo("Конвертирование успешно завернено")
            },
            {
                viewState.showError("Ошибка загрузки")
            }
        )
    }

    //Make sure to call this function on a worker thread, else it will block main thread
    @RequiresApi(Build.VERSION_CODES.Q)
    private fun saveImageInQ(bitmap: Bitmap, contentResolver: ContentResolver):Boolean {
        val filename = "$fileName.png"
        var fos: OutputStream?
        var imageUri: Uri?

        try {
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                put(MediaStore.MediaColumns.MIME_TYPE, "image/png")
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
                put(MediaStore.Video.Media.IS_PENDING, 1)
            }

            contentResolver.also { resolver ->
                imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
                fos = imageUri?.let { resolver.openOutputStream(it) }
            }

            fos?.use { bitmap.compress(Bitmap.CompressFormat.PNG, 100, it) }

            contentValues.clear()
            contentValues.put(MediaStore.Video.Media.IS_PENDING, 0)
            contentResolver.update(imageUri!!, contentValues, null, null)
            return true
        }catch (e: IOException){
            e.printStackTrace()
            return false
        }catch (e: IllegalArgumentException){
            e.printStackTrace()
            return false
        }catch (e: NullPointerException){
            e.printStackTrace()
            return false
        }
    }

    private fun legacySave(bitmap: Bitmap): Boolean {
        try {
            val directory =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
            val file = File(directory, "SomeName")//"$fileName.png")
            val outStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream)
            outStream.flush()
            outStream.close()
            MediaScannerConnection.scanFile(
                App.instance, arrayOf(file.absolutePath),
                null, null)

            val uri = FileProvider.getUriForFile(App.instance, "${App.instance.packageName}.provider",
                file)

            return uri != null

        }catch (e: IOException){
            e.printStackTrace()
            return false
        }catch (e: IllegalArgumentException){
            e.printStackTrace()
            return false
        }catch (e: NullPointerException){
            e.printStackTrace()
            return false
        }
    }

    fun backPressed(): Boolean {
        router.exit()
        return true
    }
}