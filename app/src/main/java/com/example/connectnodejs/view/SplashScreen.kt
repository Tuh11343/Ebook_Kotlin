package com.example.connectnodejs.view

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.connectnodejs.MainActivity
import com.example.connectnodejs.view.ui.theme.ConnectNodejsTheme

class SplashScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        window.insetsController?.hide(WindowInsets.Type.statusBars())
//        window.insetsController?.hide(WindowInsets.Type.navigationBars())

        val handler = Handler(Looper.getMainLooper())

        val runnable = Runnable {

            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }

        val delayMillis: Long = 2000
        handler.postDelayed(runnable, delayMillis)

    }
}

