package com.vados.pictureconverter.presenters

import android.net.Uri
import com.github.terrakok.cicerone.Router
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import moxy.MvpPresenter
import java.io.IOException

class PicturesPresenter(private val router: Router): MvpPresenter<PicturesView>() {


    private  lateinit var rxJavaThread: Disposable

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.init()
    }

    fun showImage(uri: Uri?){
        rxJavaThread = Completable.create{ emitter ->
            try {
                //TODO сохранение URI в БД
                emitter.onComplete()
            }catch (e: IOException){
                emitter.onError(Throwable("rxJavaThread: Image Loading Error"))
            }
        }.subscribeOn(Schedulers.io()).subscribe(
            {
                viewState.displayImage(uri)
            },
            {
                viewState.showError("Ошибка загрузки")
            }
        )

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