package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Today
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.CalendarDayInfo
import com.example.model.PlayerProfile
import com.example.ui.components.holoFrame
import com.example.ui.theme.AwakenBackground
import com.example.ui.theme.AwakenCardBg
import com.example.ui.theme.AwakenCardBgElevated
import com.example.ui.theme.AwakenCardBorder
import com.example.ui.theme.AwakenCardBorderGlow
import com.example.ui.theme.AwakenGold
import com.example.ui.theme.AwakenHoloCyan
import com.example.ui.theme.AwakenHoloCyanBright
import com.example.ui.theme.AwakenHpRed
import com.example.ui.theme.AwakenSuccessGreen
import com.example.ui.theme.AwakenTextDim
import com.example.ui.theme.AwakenTextMuted
import com.example.ui.theme.AwakenTextPrimary
import com.example.ui.theme.AwakenTextSecondary
import com.example.ui.theme.AwakenTextWhite
import com.example.ui.theme.AwakenVioletGlow
import com.example.ui.theme.AwakenVioletLight
import com.example.ui.theme.AwakenVioletPrimary
import com.example.ui.theme.CinzelFontFamily
import com.example.ui.theme.RajdhaniFontFamily
import java.text.DateFormatSymbols
import java.util.Locale

@Composable
fun CalendarScreen(
    profile: PlayerProfile,
    year: Int,
    month: Int, // 0-indexed (0 = Jan, 11 = Dec)
    calendarDays: List<CalendarDayInfo>,
    selectedDayInfo: CalendarDayInfo?,
    onSelectDay: (CalendarDayInfo) -> Unit,
    onPrevMonth: () -> Unit,
    onNextMonth: () -> Unit,
    onToday: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    val monthName = DateFormatSymbols.getInstance(Locale.US).months.getOrNull(month)?.uppercase() ?: "MONTH"

    // Calculate completed days in current month
    val completedDaysCount = calendarDays.count { it.isCurrentMonth && it.isCompleted }
    val totalDaysInCurrentMonth = calendarDays.count { it.isCurrentMonth }

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

            // Screen Header: System Chronicle
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
                            text = "SYSTEM CALENDAR",
                            fontFamily = CinzelFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            letterSpacing = 2.sp,
                            color = AwakenTextWhite
                        )
                    }
                    Text(
                        text = "Training Chronicle & Streak Archive",
                        fontFamily = RajdhaniFontFamily,
                        fontSize = 12.sp,
                        color = AwakenTextMuted
                    )
                }

                // Today jump button
                Surface(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .clickable(onClick = onToday)
                        .border(1.dp, AwakenHoloCyan.copy(alpha = 0.6f), RoundedCornerShape(8.dp))
                        .testTag("calendar_today_button"),
                    color = Color(0x330284C7)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Today,
                            contentDescription = "Jump to Today",
                            tint = AwakenHoloCyanBright,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "TODAY",
                            fontFamily = RajdhaniFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            color = AwakenHoloCyanBright
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Monthly Telemetry Overview Card
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                .border(1.dp, AwakenCardBorderGlow, RoundedCornerShape(16.dp)),
                color = Color(0xF4130C25)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "$completedDaysCount / $totalDaysInCurrentMonth",
                            fontFamily = RajdhaniFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = AwakenHoloCyanBright
                        )
                        Text(
                            text = "Quests Cleared",
                            fontFamily = RajdhaniFontFamily,
                            fontSize = 11.sp,
                            color = AwakenTextMuted
                        )
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.LocalFireDepartment,
                                contentDescription = null,
                                tint = AwakenGold,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(2.dp))
                            Text(
                                text = "${profile.currentStreak} Days",
                                fontFamily = RajdhaniFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                color = AwakenGold
                            )
                        }
                        Text(
                            text = "Active Streak",
                            fontFamily = RajdhaniFontFamily,
                            fontSize = 11.sp,
                            color = AwakenTextMuted
                        )
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "0",
                            fontFamily = RajdhaniFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = AwakenSuccessGreen
                        )
                        Text(
                            text = "Penalties",
                            fontFamily = RajdhaniFontFamily,
                            fontSize = 11.sp,
                            color = AwakenTextMuted
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Month Navigation Bar
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .border(1.dp, AwakenCardBorder, RoundedCornerShape(16.dp)),
                color = Color(0xF4100A20)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    // Month & Year Selector Controls
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = onPrevMonth,
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF1B1433))
                                .testTag("calendar_prev_month")
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                                contentDescription = "Previous Month",
                                tint = AwakenTextWhite
                            )
                        }

                        Text(
                            text = "$monthName $year",
                            fontFamily = CinzelFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            letterSpacing = 2.sp,
                            color = AwakenTextWhite,
                            modifier = Modifier.testTag("calendar_month_title")
                        )

                        IconButton(
                            onClick = onNextMonth,
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF1B1433))
                                .testTag("calendar_next_month")
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                contentDescription = "Next Month",
                                tint = AwakenTextWhite
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Days of Week Header
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        listOf("MON", "TUE", "WED", "THU", "FRI", "SAT", "SUN").forEach { dayLabel ->
                            Text(
                                text = dayLabel,
                                fontFamily = RajdhaniFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
                                letterSpacing = 1.sp,
                                color = AwakenTextMuted,
                                modifier = Modifier.weight(1f),
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Calendar Day Grid (7 columns x 6 rows)
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        calendarDays.chunked(7).forEach { week ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                week.forEach { dayInfo ->
                                    CalendarDayCell(
                                        dayInfo = dayInfo,
                                        isSelected = selectedDayInfo?.dateKey == dayInfo.dateKey,
                                        onClick = { onSelectDay(dayInfo) },
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Selected Date Chronicle Panel
            selectedDayInfo?.let { day ->
                SelectedDayChronicleCard(day = day)
            }

            Spacer(modifier = Modifier.height(88.dp))
        }
    }
}

@Composable
private fun CalendarDayCell(
    dayInfo: CalendarDayInfo,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isCurrentMonth = dayInfo.isCurrentMonth
    val isToday = dayInfo.isToday
    val isCompleted = dayInfo.isCompleted
    val hasPenalty = dayInfo.hasPenalty

    val cellBg = when {
        isSelected -> Color(0xFF281C4C)
        isToday -> Color(0x330369A1)
        else -> Color.Transparent
    }

    val borderModifier = when {
        isSelected -> Modifier.border(1.5.dp, AwakenVioletPrimary, RoundedCornerShape(8.dp))
        isToday -> Modifier.border(1.dp, AwakenHoloCyan, RoundedCornerShape(8.dp))
        else -> Modifier
    }

    Box(
        modifier = modifier
            .aspectRatio(1f)
            .clip(RoundedCornerShape(8.dp))
            .background(cellBg)
            .then(borderModifier)
            .clickable(enabled = isCurrentMonth, onClick = onClick)
            .testTag("day_cell_${dayInfo.dateKey}"),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "${dayInfo.day}",
                fontFamily = RajdhaniFontFamily,
                fontWeight = if (isToday || isSelected) FontWeight.Bold else FontWeight.SemiBold,
                fontSize = 14.sp,
                color = when {
                    !isCurrentMonth -> AwakenTextDim
                    isToday -> AwakenHoloCyanBright
                    isSelected -> AwakenTextWhite
                    else -> AwakenTextPrimary
                }
            )

            // Status Indicator Dot / Rune
            if (isCurrentMonth) {
                Spacer(modifier = Modifier.height(2.dp))
                when {
                    isCompleted -> {
                        Box(
                            modifier = Modifier
                                .size(5.dp)
                                .clip(CircleShape)
                                .background(AwakenVioletLight)
                        )
                    }
                    hasPenalty -> {
                        Box(
                            modifier = Modifier
                                .size(5.dp)
                                .clip(CircleShape)
                                .background(AwakenHpRed)
                        )
                    }
                    else -> {
                        Spacer(modifier = Modifier.size(5.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun SelectedDayChronicleCard(
    day: CalendarDayInfo,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .border(1.dp, AwakenHoloCyan.copy(alpha = 0.7f), RoundedCornerShape(16.dp))
            .holoFrame(accentColor = AwakenHoloCyan, bracketLength = 20f),
        color = Color(0xF80E091C)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
        ) {
            // Header: Date and Status Tag
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "[ SYSTEM CHRONICLE ]",
                        fontFamily = RajdhaniFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        letterSpacing = 1.sp,
                        color = AwakenHoloCyanBright
                    )
                    Text(
                        text = day.dateKey,
                        fontFamily = CinzelFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = AwakenTextWhite
                    )
                }

                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = when {
                        day.isCompleted -> Color(0x3310B981)
                        day.hasPenalty -> Color(0x33EF4444)
                        day.isToday -> Color(0x330284C7)
                        else -> Color(0x2264748B)
                    },
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        when {
                            day.isCompleted -> AwakenSuccessGreen
                            day.hasPenalty -> AwakenHpRed
                            day.isToday -> AwakenHoloCyan
                            else -> AwakenCardBorder
                        }
                    )
                ) {
                    Text(
                        text = when {
                            day.isCompleted -> "CLEARED"
                            day.hasPenalty -> "PENALTY"
                            day.isToday -> "ACTIVE TODAY"
                            else -> "SCHEDULED"
                        },
                        fontFamily = RajdhaniFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        color = when {
                            day.isCompleted -> AwakenSuccessGreen
                            day.hasPenalty -> AwakenHpRed
                            day.isToday -> AwakenHoloCyanBright
                            else -> AwakenTextMuted
                        },
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Day Telemetry Stats Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Surface(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp),
                    color = AwakenCardBg,
                    border = androidx.compose.foundation.BorderStroke(1.dp, AwakenCardBorder)
                ) {
                    Column(
                        modifier = Modifier.padding(10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "${day.focusMinutes}m",
                            fontFamily = RajdhaniFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = AwakenHoloCyanBright
                        )
                        Text(
                            text = "Focus Time",
                            fontFamily = RajdhaniFontFamily,
                            fontSize = 10.sp,
                            color = AwakenTextMuted
                        )
                    }
                }

                Surface(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp),
                    color = AwakenCardBg,
                    border = androidx.compose.foundation.BorderStroke(1.dp, AwakenCardBorder)
                ) {
                    Column(
                        modifier = Modifier.padding(10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "+${day.expEarned}",
                            fontFamily = RajdhaniFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = AwakenVioletLight
                        )
                        Text(
                            text = "EXP Awarded",
                            fontFamily = RajdhaniFontFamily,
                            fontSize = 10.sp,
                            color = AwakenTextMuted
                        )
                    }
                }

                Surface(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp),
                    color = AwakenCardBg,
                    border = androidx.compose.foundation.BorderStroke(1.dp, AwakenCardBorder)
                ) {
                    Column(
                        modifier = Modifier.padding(10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = if (day.isCompleted) "100%" else if (day.isToday) "IN PROGRESS" else "0%",
                            fontFamily = RajdhaniFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = if (day.isCompleted) AwakenSuccessGreen else AwakenTextSecondary
                        )
                        Text(
                            text = "Daily Quota",
                            fontFamily = RajdhaniFontFamily,
                            fontSize = 10.sp,
                            color = AwakenTextMuted
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Completed Tasks Breakdown
            Text(
                text = "LOGGED TASKS & PHYSICAL TRAINING",
                fontFamily = RajdhaniFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                letterSpacing = 1.sp,
                color = AwakenTextMuted
            )

            Spacer(modifier = Modifier.height(8.dp))

            if (day.completedTasks.isEmpty()) {
                Text(
                    text = if (day.isToday) "Tasks in progress for today. Complete all to log into the permanent chronicle."
                    else "No training records logged for this date.",
                    fontFamily = RajdhaniFontFamily,
                    fontSize = 12.sp,
                    color = AwakenTextDim
                )
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    day.completedTasks.forEach { task ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = AwakenVioletLight,
                                modifier = Modifier.size(13.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = task,
                                fontFamily = RajdhaniFontFamily,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 13.sp,
                                color = AwakenTextWhite
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // System Notice Footer
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(6.dp),
                color = Color(0x331C1338)
            ) {
                Text(
                    text = if (day.isCompleted)
                        "[NOTICE: Daily Quest Strange Training archived. All objectives met. Penalty averted.]"
                    else if (day.hasPenalty)
                        "[WARNING: Incomplete daily quota. Penalty quest triggered.]"
                    else
                        "[NOTICE: Daily System objectives await completion.]",
                    fontFamily = RajdhaniFontFamily,
                    fontSize = 11.sp,
                    lineHeight = 14.sp,
                    color = if (day.hasPenalty) Color(0xFFFCA5A5) else AwakenTextSecondary,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp)
                )
            }
        }
    }
}
