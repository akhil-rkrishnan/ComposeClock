package app.android.composeclock

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.android.composeclock.model.Time
import app.android.composeclock.ui.Clock
import app.android.composeclock.ui.ClockViewModel
import app.android.composeclock.ui.theme.ComposeClockTheme
import app.android.composeclock.utils.FrameColor

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
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(vertical = 50.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        TimeRow(viewModel, onTimeChanged = {
                            viewModel.updateTime(it)
                        })
                        Text(text = viewModel.digitalTime, style = TextStyle(
                            fontFamily = FontFamily(Font(R.font.monoton_regular)),
                            fontWeight = FontWeight(500),
                            fontSize = 30.sp,
                            color = Color.Black
                        ))
                        Box(modifier = Modifier.fillMaxWidth(0.8f)) {
                            Clock(viewModel)
                        }
                    }
                }
            }

        }
    }

    @Composable
    fun TimeRow(viewModel: ClockViewModel, onTimeChanged: (Time) -> Unit) {
        val times by remember {
            mutableStateOf(viewModel.getRandomTimes())
        }
        LazyRow(
            modifier = Modifier.fillMaxWidth(0.7f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            times.forEach {
                item {
                    Box(
                        modifier = Modifier
                            .background(Color.White)
                            .border(
                                BorderStroke(width = 2.dp, Color.DarkGray.copy(alpha = 0.2f)),
                                shape = RoundedCornerShape(10.dp)
                            )
                            .clickable {
                                onTimeChanged(it)
                            }
                    ) {
                        Text(
                            modifier = Modifier.padding(all = 15.dp),
                            text = it.toString(),
                            style = TextStyle(
                                fontFamily = FontFamily(Font(R.font.roboto_bold)),
                                fontWeight = FontWeight(500),
                                fontSize = 16.sp,
                                color = Color.DarkGray
                            )
                        )
                    }
                }
            }
        }
    }
}


