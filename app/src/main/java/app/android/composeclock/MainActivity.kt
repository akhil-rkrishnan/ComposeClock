package app.android.composeclock

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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

