package com.example.ui.screens

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.PlayerProfile
import com.example.ui.TimerState
import com.example.ui.components.RpgGaugeBar
import com.example.ui.theme.AwakenBackground
import com.example.ui.theme.AwakenCardBg
import com.example.ui.theme.AwakenCardBgElevated
import com.example.ui.theme.AwakenCardBorder
import com.example.ui.theme.AwakenCardBorderGlow
import com.example.ui.theme.AwakenHoloCyan
import com.example.ui.theme.AwakenHoloCyanBright
import com.example.ui.theme.AwakenTextMuted
import com.example.ui.theme.AwakenTextPrimary
import com.example.ui.theme.AwakenTextSecondary
import com.example.ui.theme.AwakenTextWhite
import com.example.ui.theme.AwakenVioletGlow
import com.example.ui.theme.AwakenVioletLight
import com.example.ui.theme.AwakenVioletPrimary
import com.example.ui.theme.CinzelFontFamily
import com.example.ui.theme.RajdhaniFontFamily

/**
 * Focus Quest Screen matching Reference 3 (Pomodoro timer & telemetry)
 */
@Composable
fun FocusScreen(
    profile: PlayerProfile,
    timerSecondsRemaining: Int,
    totalTimerSeconds: Int,
    timerState: TimerState,
    onStartTimer: () -> Unit,
    onPauseTimer: () -> Unit,
    onResetTimer: () -> Unit,
    onSelectPreset: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    val minutes = timerSecondsRemaining / 60
    val seconds = timerSecondsRemaining % 60
    val formattedTime = String.format("%02d:%02d", minutes, seconds)

    val progress = if (totalTimerSeconds > 0) {
        timerSecondsRemaining.toFloat() / totalTimerSeconds.toFloat()
    } else 0f

    val infiniteTransition = rememberInfiniteTransition(label = "runeSpin")
    val runeRotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(25000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "runeRotation"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(AwakenBackground)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .verticalScroll(scrollState)
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            // Main Focus Quest Card (Reference 3 tablet layout adapted to mobile)
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .border(1.dp, AwakenCardBorderGlow, RoundedCornerShape(20.dp)),
                color = Color(0xF4120B24)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Header: Focus Quest / Persistent Pomodoro sessions
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "❖ ",
                                    fontSize = 16.sp,
                                    color = AwakenVioletLight
                                )
                                Text(
                                    text = "Focus Quest",
                                    fontFamily = CinzelFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp,
                                    color = AwakenTextWhite
                                )
                            }
                            Text(
                                text = "Persistent Pomodoro sessions",
                                fontFamily = RajdhaniFontFamily,
                                fontSize = 12.sp,
                                color = AwakenTextMuted
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = AwakenVioletPrimary.copy(alpha = 0.2f),
                            border = androidx.compose.foundation.BorderStroke(1.dp, AwakenVioletPrimary)
                        ) {
                            Text(
                                text = "Lv ${profile.level}",
                                fontFamily = RajdhaniFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = AwakenVioletLight,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Timer Circular Graphic with animated runic markings
                    Box(
                        modifier = Modifier
                            .size(220.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        // Background Runic Ring
                        Canvas(
                            modifier = Modifier
                                .fillMaxSize()
                                .rotate(if (timerState == TimerState.RUNNING) runeRotation else 0f)
                        ) {
                            drawCircle(
                                color = AwakenCardBorder,
                                radius = size.minDimension / 2f - 10f,
                                style = Stroke(width = 3f)
                            )
                            // Decorative runes markers
                            for (i in 0 until 12) {
                                val angle = (i * 30f) * (Math.PI / 180.0)
                                val r = size.minDimension / 2f - 10f
                                val cx = center.x + (r * Math.cos(angle)).toFloat()
                                val cy = center.y + (r * Math.sin(angle)).toFloat()
                                drawCircle(
                                    color = AwakenHoloCyan.copy(alpha = 0.4f),
                                    radius = 3.5f,
                                    center = androidx.compose.ui.geometry.Offset(cx, cy)
                                )
                            }
                        }

                        // Active Progress Arc
                        Canvas(modifier = Modifier.fillMaxSize().padding(10.dp)) {
                            drawArc(
                                brush = Brush.sweepGradient(
                                    listOf(AwakenVioletPrimary, AwakenHoloCyan, AwakenVioletLight)
                                ),
                                startAngle = -90f,
                                sweepAngle = progress * 360f,
                                useCenter = false,
                                style = Stroke(width = 8.dp.toPx(), cap = StrokeCap.Round)
                            )
                        }

                        // Center Digital Time Display
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = if (timerState == TimerState.RUNNING) "FOCUSING" else "READY",
                                fontFamily = RajdhaniFontFamily,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 12.sp,
                                letterSpacing = 2.sp,
                                color = if (timerState == TimerState.RUNNING) AwakenVioletLight else AwakenTextMuted
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = formattedTime,
                                fontFamily = RajdhaniFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 48.sp,
                                letterSpacing = 2.sp,
                                color = AwakenTextWhite,
                                modifier = Modifier.testTag("timer_display")
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Timer Preset Chips (15m, 25m, 45m, 60m)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf(15, 25, 45, 60).forEach { mins ->
                            val isCurrent = totalTimerSeconds == mins * 60
                            Surface(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(8.dp))
                                    .clickable { onSelectPreset(mins) }
                                    .border(
                                        1.dp,
                                        if (isCurrent) AwakenVioletPrimary else AwakenCardBorder,
                                        RoundedCornerShape(8.dp)
                                    ),
                                color = if (isCurrent) AwakenVioletPrimary.copy(alpha = 0.2f) else AwakenCardBg
                            ) {
                                Text(
                                    text = "${mins}m",
                                    fontFamily = RajdhaniFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = if (isCurrent) AwakenVioletLight else AwakenTextMuted,
                                    modifier = Modifier.padding(vertical = 8.dp),
                                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Action Controls: Start / Pause / Reset (Reference 3)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Button(
                            onClick = {
                                if (timerState == TimerState.RUNNING) onPauseTimer() else onStartTimer()
                            },
                            modifier = Modifier
                                .weight(1.5f)
                                .height(48.dp)
                                .testTag("timer_start_pause_btn"),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (timerState == TimerState.RUNNING) Color(0xFF6D28D9) else AwakenVioletPrimary
                            )
                        ) {
                            Icon(
                                imageVector = if (timerState == TimerState.RUNNING) Icons.Default.Pause else Icons.Default.PlayArrow,
                                contentDescription = null,
                                tint = AwakenTextWhite,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (timerState == TimerState.RUNNING) "Pause" else "Start",
                                fontFamily = RajdhaniFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = AwakenTextWhite
                            )
                        }

                        Button(
                            onClick = onResetTimer,
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp)
                                .testTag("timer_reset_btn"),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = AwakenCardBg
                            ),
                            border = androidx.compose.foundation.BorderStroke(1.dp, AwakenCardBorder)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = "Reset",
                                tint = AwakenTextMuted,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Reset",
                                fontFamily = RajdhaniFontFamily,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 14.sp,
                                color = AwakenTextMuted
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Level XP Bar
                    RpgGaugeBar(
                        label = "❖ LEVEL ${profile.level}",
                        current = profile.currentXp,
                        max = profile.maxXp,
                        startColor = AwakenVioletPrimary,
                        endColor = AwakenHoloCyan
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // TODAY Telemetry Grid (Reference 3)
                    Text(
                        text = "❖ TODAY'S TELEMETRY",
                        fontFamily = CinzelFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        letterSpacing = 1.5.sp,
                        color = AwakenTextSecondary,
                        modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        StatTile(
                            label = "Focus Minutes",
                            value = "${profile.todayFocusMinutes}m",
                            modifier = Modifier.weight(1f)
                        )
                        StatTile(
                            label = "Daily Goal",
                            value = "${profile.todayFocusMinutes}/${profile.dailyFocusGoalMinutes}",
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        StatTile(
                            label = "Current Streak",
                            value = "${profile.currentStreak} Days",
                            modifier = Modifier.weight(1f)
                        )
                        StatTile(
                            label = "Sessions Done",
                            value = "${profile.completedSessions}",
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(88.dp))
        }
    }
}

@Composable
private fun StatTile(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .border(1.dp, AwakenCardBorder, RoundedCornerShape(10.dp)),
        color = AwakenCardBg
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = value,
                fontFamily = RajdhaniFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp,
                color = AwakenHoloCyanBright
            )
            Text(
                text = label,
                fontFamily = RajdhaniFontFamily,
                fontSize = 11.sp,
                color = AwakenTextMuted
            )
        }
    }
}
