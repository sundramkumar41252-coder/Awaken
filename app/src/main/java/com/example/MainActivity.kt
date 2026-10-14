package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.AppScreen
import com.example.ui.AwakenViewModel
import com.example.ui.components.AwakenBottomNavBar
import com.example.ui.screens.AwakeningScreen
import com.example.ui.screens.CalendarScreen
import com.example.ui.screens.EvolutionScreen
import com.example.ui.screens.FocusScreen
import com.example.ui.screens.QuestsScreen
import com.example.ui.screens.StatusScreen
import com.example.ui.screens.SystemOpeningScreen
import com.example.ui.theme.AwakenBackground
import com.example.ui.theme.AwakenHoloCyan
import com.example.ui.theme.AwakenHoloCyanBright
import com.example.ui.theme.AwakenTextWhite
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.RajdhaniFontFamily

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                AwakenApp()
            }
        }
    }
}

@Composable
fun AwakenApp(viewModel: AwakenViewModel = viewModel()) {
    val currentScreen by viewModel.currentScreen.collectAsStateWithLifecycle()
    val profile by viewModel.playerProfile.collectAsStateWithLifecycle()
    val quests by viewModel.quests.collectAsStateWithLifecycle()
    val evolutionTiers by viewModel.evolutionTiers.collectAsStateWithLifecycle()
    val selectedQuestForDetail by viewModel.selectedQuestForDetail.collectAsStateWithLifecycle()
    val timerSecondsRemaining by viewModel.timerSecondsRemaining.collectAsStateWithLifecycle()
    val totalTimerSeconds by viewModel.totalTimerSeconds.collectAsStateWithLifecycle()
    val timerState by viewModel.timerState.collectAsStateWithLifecycle()
    val notificationMsg by viewModel.showSystemNotification.collectAsStateWithLifecycle()

    val calendarYear by viewModel.calendarYear.collectAsStateWithLifecycle()
    val calendarMonth by viewModel.calendarMonth.collectAsStateWithLifecycle()
    val calendarDays by viewModel.calendarDays.collectAsStateWithLifecycle()
    val selectedDayInfo by viewModel.selectedDayInfo.collectAsStateWithLifecycle()

    // Handle back button: return to Daily Quests if on another tab
    if (currentScreen != AppScreen.DAILY_QUESTS && currentScreen != AppScreen.SYSTEM_OPENING) {
        BackHandler {
            viewModel.setScreen(AppScreen.DAILY_QUESTS)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AwakenBackground)
    ) {
        AnimatedContent(
            targetState = currentScreen,
            transitionSpec = {
                fadeIn() togetherWith fadeOut()
            },
            label = "screenTransition",
            modifier = Modifier.fillMaxSize()
        ) { screen ->
            when (screen) {
                AppScreen.SYSTEM_OPENING -> {
                    SystemOpeningScreen(
                        onFinishOpening = {
                            viewModel.setScreen(AppScreen.DAILY_QUESTS)
                        }
                    )
                }

                AppScreen.AWAKENING_INTRO -> {
                    AwakeningScreen(
                        onEnterSystem = {
                            viewModel.setScreen(AppScreen.DAILY_QUESTS)
                        }
                    )
                }

                AppScreen.DAILY_QUESTS -> {
                    QuestsScreen(
                        profile = profile,
                        quests = quests,
                        selectedQuestForDetail = selectedQuestForDetail,
                        onSelectQuest = { quest -> viewModel.openQuestDetail(quest) },
                        onCloseDetail = { viewModel.closeQuestDetail() },
                        onQuickAdd = { id, amt -> viewModel.updateQuestProgress(id, amt) },
                        onToggleCompleted = { id -> viewModel.toggleQuestCompleted(id) },
                        onAddCustomQuest = { title, target, unit ->
                            viewModel.addCustomQuest(title, target, unit)
                        },
                        onCompleteAllDaily = { viewModel.completeAllDaily() },
                        onOpenIntro = { viewModel.replayOpeningAnimation() },
                        onOpenCalendar = { viewModel.setScreen(AppScreen.SYSTEM_CALENDAR) }
                    )
                }

                AppScreen.SYSTEM_CALENDAR -> {
                    CalendarScreen(
                        profile = profile,
                        year = calendarYear,
                        month = calendarMonth,
                        calendarDays = calendarDays,
                        selectedDayInfo = selectedDayInfo,
                        onSelectDay = { day -> viewModel.selectCalendarDay(day) },
                        onPrevMonth = { viewModel.prevMonth() },
                        onNextMonth = { viewModel.nextMonth() },
                        onToday = { viewModel.goToTodayMonth() }
                    )
                }

                AppScreen.STATUS_WINDOW -> {
                    StatusScreen(
                        profile = profile,
                        onAllocateStat = { stat -> viewModel.allocateStat(stat) }
                    )
                }

                AppScreen.FOCUS_QUEST -> {
                    FocusScreen(
                        profile = profile,
                        timerSecondsRemaining = timerSecondsRemaining,
                        totalTimerSeconds = totalTimerSeconds,
                        timerState = timerState,
                        onStartTimer = { viewModel.startTimer() },
                        onPauseTimer = { viewModel.pauseTimer() },
                        onResetTimer = { viewModel.resetTimer() },
                        onSelectPreset = { mins -> viewModel.setTimerPreset(mins) }
                    )
                }

                AppScreen.EVOLUTION_PATH -> {
                    EvolutionScreen(
                        profile = profile,
                        evolutionTiers = evolutionTiers
                    )
                }
            }
        }

        // Floating Pill Navigation Bar (shown on all main screens)
        if (currentScreen != AppScreen.AWAKENING_INTRO && currentScreen != AppScreen.SYSTEM_OPENING) {
            AwakenBottomNavBar(
                currentScreen = currentScreen,
                onSelectScreen = { screen -> viewModel.setScreen(screen) },
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }

        // System Notification Banner Overlay
        AnimatedVisibility(
            visible = notificationMsg != null,
            enter = slideInVertically(initialOffsetY = { -it }) + fadeIn(),
            exit = slideOutVertically(targetOffsetY = { -it }) + fadeOut(),
            modifier = Modifier
                .align(Alignment.TopCenter)
                .statusBarsPadding()
                .padding(top = 12.dp, start = 20.dp, end = 20.dp)
        ) {
            notificationMsg?.let { msg ->
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xF20F091E),
                    border = androidx.compose.foundation.BorderStroke(1.dp, AwakenHoloCyan),
                    shadowElevation = 8.dp,
                    modifier = Modifier.testTag("system_notification_banner")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.NotificationsActive,
                            contentDescription = "System Notice",
                            tint = AwakenHoloCyanBright,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "[ SYSTEM ]: $msg",
                            fontFamily = RajdhaniFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = AwakenTextWhite
                        )
                    }
                }
            }
        }
    }
}
