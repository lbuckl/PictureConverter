package com.vados.pictureconverter.model

import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.ImageDecoder.ImageInfo
import android.graphics.ImageFormat
import android.graphics.drawable.Drawable
import android.provider.MediaStore
import android.util.Log
import androidx.core.graphics.drawable.toBitmap
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import kotlin.concurrent.thread
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

/**
 * Объект реализующий функции для сохранения фотографий в память смартфона
 */
object JpgToPngConverter {

    //функция сохраняет картинку формата drawable в память смартфона
    suspend fun savePicture(folderToSave:String,
                            contentResolver: ContentResolver,
                            draw: Drawable, fileName: String
    ) = suspendCoroutine{
        //переменные для создания пути сохранения файла в память сматрфона
        val file = File(folderToSave, "$fileName")

        if (file.isFile){
            it.resume("Файл уже загружен")
            file.delete()
        }
        else{
            thread{
                val fOut: OutputStream?
                try {
                    //Открывает поток
                    fOut = FileOutputStream(file)
                    //преобразуем в битмап и сохраняем в формате png с 50% сжатием
                    val bitmap = draw.toBitmap()
                    bitmap.compress(Bitmap.CompressFormat.PNG,100,fOut)

                    //закрываем поток
                    fOut.flush()
                    fOut.close()

                    // регистрация в фотоальбоме
                    MediaStore.Images.Media.insertImage(contentResolver,
                        bitmap, fileName, fileName
                    )
                    if (file.isFile) it.resume("Файл удачно загружен")
                }catch (e: IOException){
                    it.resume("Файл не загружен!!!")
                    Log.v("@@@","IOException")
                    e.printStackTrace()
                }catch (e:NullPointerException){
                    it.resume("Файл не загружен!!!")
                    Log.v("@@@","NullPointerException")
                    e.printStackTrace()
                }catch (e:IllegalArgumentException){
                    it.resume("Файл не загружен!!!")
                    Log.v("@@@","IllegalArgumentException")
                    e.printStackTrace()
                }
            }
        }
    }
}