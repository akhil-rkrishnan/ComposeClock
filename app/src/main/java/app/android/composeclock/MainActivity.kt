package app.android.composeclock

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.android.composeclock.model.Time
import app.android.composeclock.ui.Clock
import app.android.composeclock.ui.ClockViewModel
import app.android.composeclock.ui.theme.ComposeClockTheme
import java.util.concurrent.Flow

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
                        TitleRow(onTimeRefresh = {
                            viewModel.refreshTimes()
                        })
                        Spacer(modifier = Modifier.height(10.dp))
                        TimeRow(viewModel, onTimeChanged = {
                            viewModel.updateTime(it)
                        })
                        Text(
                            text = viewModel.digitalTime,
                            style = TextStyle(
                                fontFamily = FontFamily(Font(R.font.poppins_semibold)),
                                fontWeight = FontWeight(500),
                                fontSize = 30.sp,
                                color = Color.Black,
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .animateContentSize(),
                            textAlign = TextAlign.Center
                        )
                        Box(modifier = Modifier.fillMaxWidth(0.9f)) {
                            Clock(viewModel)
                        }
                    }
                }
            }

        }
    }

    @OptIn(ExperimentalFoundationApi::class, ExperimentalLayoutApi::class)
    @Composable
    private fun TimeRow(viewModel: ClockViewModel, onTimeChanged: (Time) -> Unit) {
        val times = viewModel.randomTimes
        FlowRow(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .clip(shape = RoundedCornerShape(10.dp))
                    .background(Color.White)
                    .border(
                        BorderStroke(width = 2.dp, Color.DarkGray.copy(alpha = 0.2f)),
                        shape = RoundedCornerShape(10.dp)
                    )
                    .clickable {
                        onTimeChanged(viewModel.getSystemTime())
                    }
            ) {
                Text(
                    modifier = Modifier.padding(all = 15.dp),
                    text = "Get system time!",
                    style = TextStyle(
                        fontFamily = FontFamily(Font(R.font.poppins_medium)),
                        fontWeight = FontWeight(500),
                        fontSize = 16.sp,
                        color = Color.DarkGray
                    )
                )
            }
            times.forEach {
                Box(
                    modifier = Modifier
                        .clip(shape = RoundedCornerShape(10.dp))
                        .background(Color.White)
                        .border(
                            BorderStroke(width = 2.dp, Color.DarkGray.copy(alpha = 0.2f)),
                            shape = RoundedCornerShape(10.dp)
                        )
                        .clickable {
                            onTimeChanged(it)
                        }
                       // .animateItemPlacement()
                ) {
                    Text(
                        modifier = Modifier.padding(all = 15.dp),
                        text = it.toString(),
                        style = TextStyle(
                            fontFamily = FontFamily(Font(R.font.poppins_medium)),
                            fontWeight = FontWeight(500),
                            fontSize = 16.sp,
                            color = Color.DarkGray
                        )
                    )
                }
            }
        }
    }

    @Composable
    private fun TitleRow(onTimeRefresh: () -> Unit) {
        Row(
            modifier = Modifier.fillMaxWidth(0.6f),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Select a time!", style = TextStyle(
                    fontFamily = FontFamily(Font(R.font.poppins_regular)),
                    fontWeight = FontWeight(300),
                    fontSize = 16.sp,
                    color = Color.Black
                )
            )

            Image(
                painter = painterResource(id = R.drawable.refresh),
                contentDescription = "Refresh icon for time",
                modifier = Modifier
                    .size(24.dp)
                    .clickable {
                        onTimeRefresh()
                    }
            )
        }
    }
}


