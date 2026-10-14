package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material.icons.filled.HourglassBottom
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.ui.AppScreen
import com.example.ui.theme.AwakenCardBorder
import com.example.ui.theme.AwakenCardBorderGlow
import com.example.ui.theme.AwakenHoloCyan
import com.example.ui.theme.AwakenTextMuted
import com.example.ui.theme.AwakenTextWhite
import com.example.ui.theme.AwakenVioletGlow
import com.example.ui.theme.AwakenVioletLight
import com.example.ui.theme.AwakenVioletPrimary

data class NavItem(
    val screen: AppScreen,
    val icon: ImageVector,
    val contentDescription: String,
    val testTag: String
)

@Composable
fun AwakenBottomNavBar(
    currentScreen: AppScreen,
    onSelectScreen: (AppScreen) -> Unit,
    modifier: Modifier = Modifier
) {
    val items = listOf(
        NavItem(AppScreen.DAILY_QUESTS, Icons.Default.Checklist, "Daily Quests", "nav_quests"),
        NavItem(AppScreen.SYSTEM_CALENDAR, Icons.Default.CalendarMonth, "System Calendar", "nav_calendar"),
        NavItem(AppScreen.STATUS_WINDOW, Icons.Default.Shield, "Status Window", "nav_status"),
        NavItem(AppScreen.FOCUS_QUEST, Icons.Default.HourglassBottom, "Focus Quest", "nav_focus"),
        NavItem(AppScreen.EVOLUTION_PATH, Icons.Default.Person, "Evolution & Profile", "nav_evolution")
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(horizontal = 24.dp, vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            modifier = Modifier
                .clip(RoundedCornerShape(28.dp))
                .border(1.dp, AwakenCardBorderGlow, RoundedCornerShape(28.dp)),
            color = Color(0xF2120C24),
            shadowElevation = 8.dp
        ) {
            Row(
                modifier = Modifier
                    .padding(horizontal = 10.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                items.forEach { item ->
                    val isSelected = currentScreen == item.screen
                    val iconTint by animateColorAsState(
                        targetValue = if (isSelected) AwakenTextWhite else AwakenTextMuted,
                        animationSpec = tween(250),
                        label = "iconTint"
                    )

                    val bgModifier = if (isSelected) {
                        Modifier.background(
                            Brush.radialGradient(
                                listOf(AwakenVioletPrimary.copy(alpha = 0.5f), Color.Transparent)
                            )
                        )
                    } else Modifier

                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .then(bgModifier)
                            .clickable { onSelectScreen(item.screen) }
                            .testTag(item.testTag),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.contentDescription,
                            tint = iconTint,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
        }
    }
}
