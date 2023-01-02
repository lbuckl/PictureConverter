package com.vados.pictureconverter.presenters

import android.net.Uri
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import com.github.terrakok.cicerone.Router
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import moxy.MvpPresenter

class PicturesPresenter(private val router: Router): MvpPresenter<PicturesView>() {


    private  lateinit var single: Disposable

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.init()
    }

    fun showImage(uri: Uri?){
        viewState.displayImage(uri)

        /*single = Single.create<Boolean>{ emitter ->
        }.subscribeOn(Schedulers.io())
            .subscribe(
                {
                    Log.v("@@@","success")
                },
                {
                    Log.v("@@@","success")
                }
            )*/
    }

    fun backPressed(): Boolean {
        router.exit()
        return true
    }
}