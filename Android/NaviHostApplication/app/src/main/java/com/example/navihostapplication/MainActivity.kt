package com.example.navihostapplication

import android.animation.ObjectAnimator
import android.app.Application
import android.graphics.Path
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import android.window.SplashScreenView
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider

class MainActivity : AppCompatActivity() {
    private lateinit var vm: MyViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        vm = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory(application)
        )[MyViewModel::class.java]
        keepSplashScreenLonger()

    }

    /**
     * 如果需要在数据准备完毕后再关闭启动动画，可以通过这种方式进行控制
     */
    private fun keepSplashScreenLonger() {
        // 监听Content View的描画时机
        val content: View = findViewById(android.R.id.content)
        content.viewTreeObserver.addOnPreDrawListener(
            object : ViewTreeObserver.OnPreDrawListener {
                override fun onPreDraw(): Boolean {
                    // 准备好了描画放行，反之挂起
                    return if (vm.isDataReady()) {
                        Log.i("wjc", "isDataReady true")
                        content.viewTreeObserver.removeOnPreDrawListener(this)
                        true
                    } else {
                        Log.i("wjc", "isDataReady false")
                        false
                    }
                }
            }
        )
    }

    /**
     * 自定义退出动画
     */
//    private fun customizeSplashScreenExit(splashScreen:SplashScreen) {
//        splashScreen.setOnExitAnimationListener { splashScreenViewProvider ->
//            showSplashExitAnimator(splashScreenViewProvider.view as SplashScreenView)
//        }
//    }
//
//    private fun showSplashExitAnimator(splashScreenView: SplashScreenView) {
//        val path = Path()
//        path.moveTo(1.0f, 1.0f)
//        path.lineTo(0f, 0f)
//        val slideUp = ObjectAnimator.ofFloat(
//            splashScreenView,
//            View.TRANSLATION_Y,
//            0f,
//            -splashScreenView.height.toFloat()
//        )
//        slideUp.doOnEnd {
//            splashScreenView.remove()
//        }
//        slideUp.start()
//    }

}

class MyViewModel(application: Application) : AndroidViewModel(application) {
    companion object {
        const val WORK_DURATION = 2000L
    }

    private val initTime = SystemClock.uptimeMillis()
    fun isDataReady() = SystemClock.uptimeMillis() - initTime > WORK_DURATION
}