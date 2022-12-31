package com.vados.pictureconverter.presenters

import android.content.Intent
import androidx.core.app.ActivityCompat.startActivityForResult
import com.github.terrakok.cicerone.Router
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import moxy.MvpPresenter

class PicturesPresenter(private val router: Router): MvpPresenter<PicturesView>() {

    private val photoID = 1

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.init()
    }

    fun downloadImageFromStorage(){
        Single.create<Intent?>{

        }.subscribeOn(Schedulers.io())
            .subscribe(
                {

                },
                {

                }
            )
    }

    fun backPressed(): Boolean {
        router.exit()
        return true
    }
}