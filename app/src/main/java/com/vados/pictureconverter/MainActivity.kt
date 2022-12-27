package com.vados.pictureconverter

import android.os.Bundle
import com.github.terrakok.cicerone.androidx.AppNavigator
import com.vados.pictureconverter.databinding.ActivityMainBinding
import com.vados.pictureconverter.ui.BackButtonListener
import moxy.MvpAppCompatActivity
import moxy.ktx.moxyPresenter

class MainActivity : MvpAppCompatActivity(),MainView {
    private var binding: ActivityMainBinding? = null
    private val navigator = AppNavigator(this, R.id.container)

    private val presenter by moxyPresenter {
        MainPresenter(App.instance.router)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        //Инициализируем навигатор
        App.instance.navigatorHolder.setNavigator(navigator)
    }

    override fun onPause() {
        super.onPause()
        //Удаляем навигатор
        App.instance.navigatorHolder.removeNavigator()
    }

    /**
     * Функция прослушивает нажатие на кнопку "Назад"
     * и передаёт команду презентёру
     */
    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        supportFragmentManager.fragments.forEach {
            if(it is BackButtonListener && it.backPressed()){
                return
            }
        }
        presenter.backClicked()
    }
}