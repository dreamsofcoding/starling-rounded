package com.cd.rounded

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.cd.rounded.business.RoundUpView
import com.cd.rounded.ui.theme.RoundedTheme
import com.cd.rounded.ui.theme.StarlingColors
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RoundedTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = StarlingColors.StarlingBackground
                ) {
                    RoundUpView()
                }
            }
        }
    }
}

