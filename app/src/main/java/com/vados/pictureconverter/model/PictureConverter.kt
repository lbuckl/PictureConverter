package com.vados.pictureconverter.model

import android.content.ContentResolver
import android.content.ContentValues
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import androidx.core.content.FileProvider
import com.vados.pictureconverter.App
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream

class PictureConverter {

    /**
     * Функция для конвертирования изображения в формат png и
     * выведения информационного сообщения пользователю о результатае конвертации:
     * (выполнено/ошибка).
     *
     * В зависиомти от версии SDK есть 2 функции конвертирования:
     * saveImageInQ - новый спобос при версии SDK больше 28
     * legacySave - устаревший способ при версии 28 и ниже
     */
    fun convertToPngAndSave(
        bitmapImage: Bitmap,
        fileName: String,
        contentResolver: ContentResolver
    ): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            saveImageInQ(bitmapImage,fileName, contentResolver)
        } else {
            legacySave(bitmapImage,fileName)
        }
    }

    /**
     * Функция для конвертрования и сохранения изображения в формате png
     * формат для версии Android Q
     */
    @RequiresApi(Build.VERSION_CODES.Q)
    private fun saveImageInQ(bitmap: Bitmap,fileName: String, contentResolver: ContentResolver):Boolean {
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

    /**
     * Функция для конвертрования и сохранения изображения в формате png
     * формат для версии Android ниже Q
     */
    private fun legacySave(bitmap: Bitmap, fileName: String): Boolean {
        try {
            val directory =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
            val file = File(directory, "$fileName.png")
            val outStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream)
            outStream.flush()
            outStream.close()
            MediaScannerConnection.scanFile(
                App.instance, arrayOf(file.absolutePath),
                null, null)

            val uri = FileProvider.getUriForFile(
                App.instance, "${App.instance.packageName}.provider",
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
}