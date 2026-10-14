package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.PlayerProfile
import com.example.model.QuestItem
import com.example.ui.components.QuestCardItem
import com.example.ui.components.QuestDetailModal
import com.example.ui.components.holoFrame
import com.example.ui.theme.AwakenBackground
import com.example.ui.theme.AwakenButtonPurple
import com.example.ui.theme.AwakenCardBg
import com.example.ui.theme.AwakenCardBgElevated
import com.example.ui.theme.AwakenCardBorder
import com.example.ui.theme.AwakenCardBorderGlow
import com.example.ui.theme.AwakenGold
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

@Composable
fun QuestsScreen(
    profile: PlayerProfile,
    quests: List<QuestItem>,
    selectedQuestForDetail: QuestItem?,
    onSelectQuest: (QuestItem) -> Unit,
    onCloseDetail: () -> Unit,
    onQuickAdd: (String, Int) -> Unit,
    onToggleCompleted: (String) -> Unit,
    onAddCustomQuest: (String, Int, String) -> Unit,
    onCompleteAllDaily: () -> Unit,
    onOpenIntro: () -> Unit,
    onOpenCalendar: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showAddQuestDialog by remember { mutableStateOf(false) }

    val activeQuests = quests.filter { !it.isCompleted }
    val completedQuests = quests.filter { it.isCompleted }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(AwakenBackground)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Top App Bar Header (Reference 2 style)
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp, bottom = 4.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // System rune top center
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .clickable(onClick = onOpenIntro)
                                .border(1.dp, AwakenCardBorder, RoundedCornerShape(8.dp)),
                            color = AwakenCardBg
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "❖ AWAKEN",
                                    fontFamily = CinzelFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp,
                                    color = AwakenVioletLight
                                )
                            }
                        }

                        // Streak and Calendar Link
                        Surface(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .clickable(onClick = onOpenCalendar)
                                .border(1.dp, AwakenCardBorder, RoundedCornerShape(8.dp))
                                .testTag("quests_header_calendar_btn"),
                            color = AwakenCardBg
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CalendarMonth,
                                    contentDescription = "Calendar Chronicle",
                                    tint = AwakenHoloCyanBright,
                                    modifier = Modifier.size(15.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Icon(
                                    imageVector = Icons.Default.LocalFireDepartment,
                                    contentDescription = "Streak",
                                    tint = AwakenGold,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(3.dp))
                                Text(
                                    text = "${profile.currentStreak} DAYS",
                                    fontFamily = RajdhaniFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = AwakenGold
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // DAILY QUESTS title
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "DAILY QUESTS",
                            fontFamily = CinzelFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            letterSpacing = 2.sp,
                            color = AwakenTextWhite
                        )

                        Text(
                            text = "${profile.rank} • Lv.${profile.level}",
                            fontFamily = RajdhaniFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = AwakenHoloCyanBright
                        )
                    }
                }
            }

            // Strange Training Hero Quest Card (Reference 2)
            item {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .border(1.dp, AwakenCardBorderGlow, RoundedCornerShape(16.dp))
                        .clickable {
                            if (activeQuests.isNotEmpty()) {
                                onSelectQuest(activeQuests.first())
                            }
                        }
                        .testTag("strange_training_card"),
                    color = Color(0xFF140D27)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "Strange Training",
                                    fontFamily = CinzelFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp,
                                    color = AwakenTextWhite
                                )
                                Text(
                                    text = "${quests.size} Total System Tasks",
                                    fontFamily = RajdhaniFontFamily,
                                    fontSize = 12.sp,
                                    color = AwakenTextMuted
                                )
                            }

                            Surface(
                                shape = CircleShape,
                                color = AwakenVioletPrimary.copy(alpha = 0.2f),
                                border = androidx.compose.foundation.BorderStroke(1.dp, AwakenVioletPrimary)
                            ) {
                                Text(
                                    text = "${completedQuests.size} / ${quests.size}",
                                    fontFamily = RajdhaniFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = AwakenVioletLight,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Daily quest reward preview
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "DAILY CLEAR REWARD: +150 EXP • +3 AP",
                                fontFamily = RajdhaniFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                color = AwakenHoloCyanBright
                            )

                            TextButton(
                                onClick = onCompleteAllDaily,
                                modifier = Modifier.testTag("claim_daily_btn")
                            ) {
                                Text(
                                    text = "Complete All",
                                    fontFamily = RajdhaniFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = AwakenVioletLight
                                )
                            }
                        }
                    }
                }
            }

            // PLAN Section Header (Reference 2)
            item {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "PLAN",
                        fontFamily = CinzelFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        letterSpacing = 1.5.sp,
                        color = AwakenTextSecondary
                    )

                    Surface(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .clickable { showAddQuestDialog = true }
                            .border(1.dp, AwakenCardBorder, RoundedCornerShape(8.dp)),
                        color = AwakenCardBg
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Add Quest",
                                tint = AwakenHoloCyanBright,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "NEW QUEST",
                                fontFamily = RajdhaniFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
                                color = AwakenHoloCyanBright
                            )
                        }
                    }
                }
            }

            // Active Quest items list
            if (activeQuests.isEmpty()) {
                item {
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .border(1.dp, AwakenCardBorder, RoundedCornerShape(12.dp)),
                        color = AwakenCardBg
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "❖ ALL DAILY QUESTS CLEARED",
                                fontFamily = CinzelFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = AwakenVioletLight
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "You have met all daily requirements. Fatigue recovered.",
                                fontFamily = RajdhaniFontFamily,
                                fontSize = 13.sp,
                                color = AwakenTextMuted
                            )
                        }
                    }
                }
            } else {
                items(activeQuests, key = { it.id }) { quest ->
                    QuestCardItem(
                        quest = quest,
                        onClick = { onSelectQuest(quest) },
                        onQuickAdd = { increment -> onQuickAdd(quest.id, increment) },
                        onToggle = { onToggleCompleted(quest.id) }
                    )
                }
            }

            // COMPLETED Section Header (Reference 2)
            if (completedQuests.isNotEmpty()) {
                item {
                    Text(
                        text = "COMPLETED",
                        fontFamily = CinzelFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        letterSpacing = 1.5.sp,
                        color = AwakenTextSecondary,
                        modifier = Modifier.padding(top = 12.dp)
                    )
                }

                items(completedQuests, key = { it.id }) { quest ->
                    QuestCardItem(
                        quest = quest,
                        onClick = { onSelectQuest(quest) },
                        onQuickAdd = {},
                        onToggle = { onToggleCompleted(quest.id) }
                    )
                }
            }

            // Bottom spacing for pill navigation bar
            item {
                Spacer(modifier = Modifier.height(88.dp))
            }
        }

        // Quest Detail Modal (Reference 2 third panel)
        selectedQuestForDetail?.let { quest ->
            QuestDetailModal(
                quest = quest,
                onDismiss = onCloseDetail,
                onAddProgress = { amt -> onQuickAdd(quest.id, amt) },
                onToggleCompleted = { onToggleCompleted(quest.id) },
                onCompleteAll = onCompleteAllDaily
            )
        }

        // Dialog for adding custom quest
        if (showAddQuestDialog) {
            AddQuestDialog(
                onDismiss = { showAddQuestDialog = false },
                onConfirm = { title, target, unit ->
                    onAddCustomQuest(title, target, unit)
                    showAddQuestDialog = false
                }
            )
        }
    }
}

@Composable
private fun AddQuestDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, Int, String) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var targetText by remember { mutableStateOf("50") }
    var unit by remember { mutableStateOf("reps") }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color(0xFF140D26),
        title = {
            Text(
                text = "[ REGISTER SYSTEM QUEST ]",
                fontFamily = RajdhaniFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = AwakenHoloCyanBright
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Quest Name (e.g. Pull-ups, Coding)") },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = AwakenVioletPrimary,
                        unfocusedBorderColor = AwakenCardBorder,
                        focusedLabelColor = AwakenVioletLight,
                        focusedTextColor = AwakenTextWhite,
                        unfocusedTextColor = AwakenTextWhite
                    ),
                    modifier = Modifier.fillMaxWidth().testTag("new_quest_title_input")
                )

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = targetText,
                        onValueChange = { targetText = it },
                        label = { Text("Goal") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = AwakenVioletPrimary,
                            unfocusedBorderColor = AwakenCardBorder,
                            focusedTextColor = AwakenTextWhite,
                            unfocusedTextColor = AwakenTextWhite
                        ),
                        modifier = Modifier.weight(1f).testTag("new_quest_goal_input")
                    )

                    OutlinedTextField(
                        value = unit,
                        onValueChange = { unit = it },
                        label = { Text("Unit") },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = AwakenVioletPrimary,
                            unfocusedBorderColor = AwakenCardBorder,
                            focusedTextColor = AwakenTextWhite,
                            unfocusedTextColor = AwakenTextWhite
                        ),
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val target = targetText.toIntOrNull() ?: 50
                    if (title.isNotBlank()) {
                        onConfirm(title, target, unit)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = AwakenVioletPrimary),
                modifier = Modifier.testTag("confirm_add_quest_btn")
            ) {
                Text(
                    text = "Register",
                    fontFamily = RajdhaniFontFamily,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = "Cancel",
                    fontFamily = RajdhaniFontFamily,
                    color = AwakenTextMuted
                )
            }
        }
    )
}
