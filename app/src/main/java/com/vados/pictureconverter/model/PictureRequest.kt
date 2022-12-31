package com.vados.pictureconverter.model

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.activity.result.contract.ActivityResultContract

class PictureRequest: ActivityResultContract<Int, Uri?>() {

    override fun createIntent(context: Context, input: Int): Intent {
        //Вызываем стандартную галерею для выбора изображения с помощью Intent.ACTION_PICK:
        val intent = Intent(Intent.ACTION_PICK)
        //Тип получаемых объектов - image:
        intent.type = "image/png"
        Log.v("@@@","createIntent")
        return intent
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Uri? {
        Log.v("@@@","parseResult")
        if (resultCode == Activity.RESULT_OK){
            Log.v("@@@","RESULT_OK")
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