package app.android.composeclock

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import app.android.composeclock.ui.Clock
import app.android.composeclock.ui.ClockViewModel
import app.android.composeclock.ui.theme.ComposeClockTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeClockTheme {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    val viewModel by viewModels<ClockViewModel>()
                    Clock(viewModel)
                }
            }

        }
    }
}

