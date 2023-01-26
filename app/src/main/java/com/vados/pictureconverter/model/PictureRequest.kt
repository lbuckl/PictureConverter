package com.vados.pictureconverter.model

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.activity.result.contract.ActivityResultContract

/**
 * Класс реализующий выбор фото из Галереии смартфона
 *
 */
class PictureRequest: ActivityResultContract<Int, Uri?>() {

    /**
     * Создание интента на просмотр фото в галерее
     * @param context - передаётся автоматически по умолчанию из активити или фрагмента
     * @param input - произволный айди для интента
     */
    override fun createIntent(context: Context, input: Int): Intent {
        //Вызываем стандартную галерею для выбора изображения с помощью Intent.ACTION_PICK:
        val intent = Intent(Intent.ACTION_PICK)
        //Тип получаемых объектов - image:
        intent.type = "image/"
        return intent
    }

    /**
     * Результат выполнения интента
     * возвращает uri фото в галерее или null
     */
    override fun parseResult(resultCode: Int, intent: Intent?): Uri? {
        Log.v("@@@",Thread.currentThread().name)
        if (resultCode == Activity.RESULT_OK){
            return try {
                val image = intent?.data
                image
            }catch (e:RuntimeException){
                e.printStackTrace()
                null
            }
        }
        return null
    }

}