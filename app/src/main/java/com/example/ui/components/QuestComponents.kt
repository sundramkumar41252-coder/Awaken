package com.example.ui.components

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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.model.QuestItem
import com.example.ui.theme.AwakenCardBg
import com.example.ui.theme.AwakenCardBgElevated
import com.example.ui.theme.AwakenCardBorder
import com.example.ui.theme.AwakenCardBorderGlow
import com.example.ui.theme.AwakenHoloBorder
import com.example.ui.theme.AwakenHoloCyan
import com.example.ui.theme.AwakenHoloCyanBright
import com.example.ui.theme.AwakenHoloDarkBg
import com.example.ui.theme.AwakenHpRed
import com.example.ui.theme.AwakenSuccessGreen
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
 * Compact, polished Quest Card matching Reference 2 Plan & Completed list
 */
@Composable
fun QuestCardItem(
    quest: QuestItem,
    onClick: () -> Unit,
    onQuickAdd: (Int) -> Unit,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    val progressRatio = (quest.currentProgress.toFloat() / quest.targetGoal.toFloat()).coerceIn(0f, 1f)

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .border(
                1.dp,
                if (quest.isCompleted) AwakenVioletGlow.copy(alpha = 0.5f) else AwakenCardBorder,
                RoundedCornerShape(12.dp)
            )
            .clickable(onClick = onClick)
            .testTag("quest_item_${quest.id}"),
        color = AwakenCardBg
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = quest.title,
                        fontFamily = RajdhaniFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp,
                        color = if (quest.isCompleted) AwakenVioletLight else AwakenTextWhite
                    )
                    Text(
                        text = quest.category,
                        fontFamily = RajdhaniFontFamily,
                        fontSize = 12.sp,
                        color = AwakenTextMuted
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "[ ${quest.currentProgress} / ${quest.targetGoal}${if (quest.unit != "reps") " ${quest.unit}" else ""} ]",
                        fontFamily = RajdhaniFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = if (quest.isCompleted) AwakenSuccessGreen else AwakenHoloCyanBright,
                        modifier = Modifier.padding(end = 8.dp)
                    )

                    // Quick complete check button
                    IconButton(
                        onClick = onToggle,
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(
                                if (quest.isCompleted) AwakenVioletPrimary.copy(alpha = 0.25f)
                                else Color(0xFF1E1635)
                            )
                            .border(
                                1.dp,
                                if (quest.isCompleted) AwakenVioletPrimary else AwakenCardBorder,
                                CircleShape
                            )
                            .testTag("quest_toggle_${quest.id}")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Toggle completion",
                            tint = if (quest.isCompleted) AwakenVioletLight else AwakenTextMuted,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            // Progress bar
            Spacer(modifier = Modifier.height(10.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(5.dp)
                    .clip(RoundedCornerShape(3.dp))
                    .background(Color(0xFF0C0717))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(progressRatio)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(3.dp))
                        .background(
                            if (quest.isCompleted) Brush.horizontalGradient(
                                listOf(AwakenVioletPrimary, AwakenVioletLight)
                            )
                            else Brush.horizontalGradient(
                                listOf(AwakenHoloCyan, AwakenVioletPrimary)
                            )
                        )
                )
            }

            // Quick increment chips if not yet completed
            if (!quest.isCompleted) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    val addAmount = if (quest.unit == "km") 1 else 10
                    Surface(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .clickable { onQuickAdd(addAmount) }
                            .border(1.dp, AwakenCardBorder, RoundedCornerShape(6.dp)),
                        color = Color(0xFF1A1333)
                    ) {
                        Text(
                            text = "+$addAmount",
                            fontFamily = RajdhaniFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = AwakenHoloCyanBright,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }
                }
            }
        }
    }
}

/**
 * System Quest Info Modal (Reference 2 third panel)
 * Features "[ QUEST INFO ]", "[Daily Quest: Strange Training has Arrived]",
 * Goal list, Warning about penalty, and completion trigger.
 */
@Composable
fun QuestDetailModal(
    quest: QuestItem,
    onDismiss: () -> Unit,
    onAddProgress: (Int) -> Unit,
    onToggleCompleted: () -> Unit,
    onCompleteAll: () -> Unit,
    modifier: Modifier = Modifier
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .border(1.5.dp, AwakenHoloCyan.copy(alpha = 0.8f), RoundedCornerShape(16.dp))
                .holoFrame(accentColor = AwakenHoloCyan, bracketLength = 20f),
            color = Color(0xF80B0716)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header: [ QUEST INFO ] with close button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = AwakenHoloCyan.copy(alpha = 0.2f),
                            border = androidx.compose.foundation.BorderStroke(1.dp, AwakenHoloCyan)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = null,
                                tint = AwakenHoloCyanBright,
                                modifier = Modifier
                                    .padding(4.dp)
                                    .size(16.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "QUEST INFO",
                            fontFamily = RajdhaniFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            letterSpacing = 1.5.sp,
                            color = AwakenTextWhite
                        )
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.size(32.dp).testTag("close_quest_modal")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = AwakenTextMuted
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Title banner
                Text(
                    text = "[ ${quest.category} has Arrived ]",
                    fontFamily = RajdhaniFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = AwakenTextSecondary
                )

                Spacer(modifier = Modifier.height(12.dp))

                // GOAL
                Text(
                    text = "GOAL",
                    fontFamily = CinzelFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    letterSpacing = 2.sp,
                    color = AwakenTextWhite
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Task details card
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .border(1.dp, AwakenCardBorder, RoundedCornerShape(12.dp)),
                    color = AwakenCardBg
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = quest.title,
                                fontFamily = RajdhaniFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 17.sp,
                                color = AwakenTextWhite
                            )
                            Text(
                                text = "[ ${quest.currentProgress} / ${quest.targetGoal} ${quest.unit} ]",
                                fontFamily = RajdhaniFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = if (quest.isCompleted) AwakenSuccessGreen else AwakenHoloCyanBright
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Quick buttons to add reps
                        Text(
                            text = "LOG PROGRESS",
                            fontFamily = RajdhaniFontFamily,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = AwakenTextMuted
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            val amounts = if (quest.unit == "km") listOf(1, 2, 5) else listOf(5, 10, 25)
                            amounts.forEach { amt ->
                                Button(
                                    onClick = { onAddProgress(amt) },
                                    modifier = Modifier.weight(1f).height(36.dp),
                                    shape = RoundedCornerShape(8.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(0xFF22173F)
                                    )
                                ) {
                                    Text(
                                        text = "+$amt",
                                        fontFamily = RajdhaniFontFamily,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        color = AwakenHoloCyanBright
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Reward Tag
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "REWARDS:",
                                fontFamily = RajdhaniFontFamily,
                                fontSize = 12.sp,
                                color = AwakenTextMuted
                            )
                            Text(
                                text = "+${quest.expReward} EXP  •  ${quest.statReward}",
                                fontFamily = RajdhaniFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = AwakenVioletLight
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Warning Notice (Reference 2 exact detail)
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .border(1.dp, AwakenHpRed.copy(alpha = 0.4f), RoundedCornerShape(8.dp)),
                    color = Color(0x333F0F1A)
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = "Warning",
                            tint = AwakenHpRed,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "WARNING: Failure to complete the daily quest will result in an appropriate penalty.",
                            fontFamily = RajdhaniFontFamily,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 11.sp,
                            lineHeight = 14.sp,
                            color = Color(0xFFFCA5A5)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Action buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = onToggleCompleted,
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp)
                            .testTag("modal_toggle_completed"),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (quest.isCompleted) Color(0xFF2E234D) else AwakenVioletPrimary
                        )
                    ) {
                        Text(
                            text = if (quest.isCompleted) "Mark Incomplete" else "Mark Complete",
                            fontFamily = RajdhaniFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = AwakenTextWhite
                        )
                    }

                    Button(
                        onClick = onCompleteAll,
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp)
                            .testTag("modal_complete_all"),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF1E133A)
                        )
                    ) {
                        Text(
                            text = "Claim Daily",
                            fontFamily = RajdhaniFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = AwakenHoloCyanBright
                        )
                    }
                }
            }
        }
    }
}
