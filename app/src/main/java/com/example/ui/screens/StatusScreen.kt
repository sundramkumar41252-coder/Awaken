package com.example.ui.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Shield
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.PlayerProfile
import com.example.model.StatType
import com.example.ui.components.RpgGaugeBar
import com.example.ui.components.StatAttributeRow
import com.example.ui.components.holoFrame
import com.example.ui.theme.AwakenBackground
import com.example.ui.theme.AwakenCardBg
import com.example.ui.theme.AwakenCardBgElevated
import com.example.ui.theme.AwakenCardBorder
import com.example.ui.theme.AwakenCardBorderGlow
import com.example.ui.theme.AwakenExpViolet
import com.example.ui.theme.AwakenGold
import com.example.ui.theme.AwakenHoloCyan
import com.example.ui.theme.AwakenHoloCyanBright
import com.example.ui.theme.AwakenHpRed
import com.example.ui.theme.AwakenMpBlue
import com.example.ui.theme.AwakenTextMuted
import com.example.ui.theme.AwakenTextPrimary
import com.example.ui.theme.AwakenTextSecondary
import com.example.ui.theme.AwakenTextWhite
import com.example.ui.theme.AwakenVioletLight
import com.example.ui.theme.AwakenVioletPrimary
import com.example.ui.theme.CinzelFontFamily
import com.example.ui.theme.RajdhaniFontFamily

/**
 * Dedicated Holographic Status Window inspired by Reference Image 1
 * integrated cleanly into the dark purple & deep black mobile theme
 */
@Composable
fun StatusScreen(
    profile: PlayerProfile,
    onAllocateStat: (StatType) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    val infiniteTransition = rememberInfiniteTransition(label = "holoPulse")
    val holoPulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.6f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "holoPulseAlpha"
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

            // Main Holographic Status Frame (Reference 1)
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .border(
                        1.5.dp,
                        AwakenHoloCyan.copy(alpha = holoPulseAlpha),
                        RoundedCornerShape(16.dp)
                    )
                    .holoFrame(
                        accentColor = AwakenHoloCyan.copy(alpha = holoPulseAlpha),
                        bracketLength = 28f
                    ),
                color = Color(0xF60B0818)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Holographic Status Title Box (Reference 1 style)
                    Surface(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .border(1.dp, AwakenHoloCyan, RoundedCornerShape(6.dp)),
                        color = Color(0x330284C7)
                    ) {
                        Text(
                            text = "STATUS",
                            fontFamily = CinzelFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 22.sp,
                            letterSpacing = 4.sp,
                            color = AwakenHoloCyanBright,
                            modifier = Modifier.padding(horizontal = 32.dp, vertical = 6.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Player Meta: Level, Rank, Title (Reference 1)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "LEVEL",
                                fontFamily = RajdhaniFontFamily,
                                fontSize = 12.sp,
                                color = AwakenTextMuted,
                                letterSpacing = 1.sp
                            )
                            Text(
                                text = "${profile.level}",
                                fontFamily = RajdhaniFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 28.sp,
                                color = AwakenHoloCyanBright
                            )
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "RANK: ",
                                    fontFamily = RajdhaniFontFamily,
                                    fontSize = 12.sp,
                                    color = AwakenTextMuted
                                )
                                Text(
                                    text = profile.rank,
                                    fontFamily = RajdhaniFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = AwakenVioletLight
                                )
                            }
                            Spacer(modifier = Modifier.height(2.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "TITLE: ",
                                    fontFamily = RajdhaniFontFamily,
                                    fontSize = 12.sp,
                                    color = AwakenTextMuted
                                )
                                Text(
                                    text = profile.title,
                                    fontFamily = RajdhaniFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = AwakenTextWhite
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // HP & MP Gauges (Reference 1 style)
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .border(1.dp, AwakenCardBorder, RoundedCornerShape(12.dp)),
                        color = AwakenCardBg
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            // HP Bar
                            RpgGaugeBar(
                                label = "+ HP",
                                current = profile.currentHp,
                                max = profile.maxHp,
                                startColor = Color(0xFFEF4444),
                                endColor = Color(0xFFF97316),
                                icon = {
                                    Icon(
                                        imageVector = Icons.Default.Favorite,
                                        contentDescription = "HP",
                                        tint = AwakenHpRed,
                                        modifier = Modifier.size(14.dp)
                                    )
                                }
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            // MP Bar
                            RpgGaugeBar(
                                label = "✦ MP",
                                current = profile.currentMp,
                                max = profile.maxMp,
                                startColor = Color(0xFF0284C7),
                                endColor = Color(0xFF38BDF8),
                                icon = {
                                    Icon(
                                        imageVector = Icons.Default.ElectricBolt,
                                        contentDescription = "MP",
                                        tint = AwakenMpBlue,
                                        modifier = Modifier.size(14.dp)
                                    )
                                }
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            // EXP Bar
                            RpgGaugeBar(
                                label = "❖ EXP",
                                current = profile.currentXp,
                                max = profile.maxXp,
                                startColor = Color(0xFF7C3AED),
                                endColor = Color(0xFFA855F7),
                                icon = {
                                    Icon(
                                        imageVector = Icons.Default.AutoAwesome,
                                        contentDescription = "EXP",
                                        tint = AwakenVioletLight,
                                        modifier = Modifier.size(14.dp)
                                    )
                                }
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            // Fatigue status
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "FATIGUE: ${profile.fatigue}%",
                                    fontFamily = RajdhaniFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    color = if (profile.fatigue > 50) AwakenHpRed else AwakenHoloCyanBright
                                )
                                Text(
                                    text = "STATE: OPTIMAL",
                                    fontFamily = RajdhaniFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    color = AwakenVioletLight
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // AP (Available Points) Banner
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .border(
                                1.dp,
                                if (profile.availableAp > 0) AwakenHoloCyan else AwakenCardBorder,
                                RoundedCornerShape(10.dp)
                            ),
                        color = if (profile.availableAp > 0) Color(0x330369A1) else Color(0xFF130E22)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 14.dp, vertical = 10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "AP (Available Points) :",
                                    fontFamily = RajdhaniFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = if (profile.availableAp > 0) AwakenHoloCyanBright else AwakenTextMuted
                                )
                            }
                            Text(
                                text = "${profile.availableAp}",
                                fontFamily = RajdhaniFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp,
                                color = if (profile.availableAp > 0) AwakenGold else AwakenTextMuted,
                                modifier = Modifier.testTag("available_ap_count")
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Attributes List (STR, VIT, AGI, INT, PER)
                    Text(
                        text = "ATTRIBUTES",
                        fontFamily = CinzelFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        letterSpacing = 2.sp,
                        color = AwakenTextSecondary,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        StatAttributeRow(
                            statName = "STR",
                            statFullLabel = "Strength (Physical Power)",
                            value = profile.strength,
                            icon = "⚔",
                            availableAp = profile.availableAp,
                            onAllocate = { onAllocateStat(StatType.STR) }
                        )

                        StatAttributeRow(
                            statName = "VIT",
                            statFullLabel = "Vitality (Health & Stamina)",
                            value = profile.vitality,
                            icon = "♥",
                            availableAp = profile.availableAp,
                            onAllocate = { onAllocateStat(StatType.VIT) }
                        )

                        StatAttributeRow(
                            statName = "AGI",
                            statFullLabel = "Agility (Speed & Reflexes)",
                            value = profile.agility,
                            icon = "⚡",
                            availableAp = profile.availableAp,
                            onAllocate = { onAllocateStat(StatType.AGI) }
                        )

                        StatAttributeRow(
                            statName = "INT",
                            statFullLabel = "Intelligence (Focus & Mana)",
                            value = profile.intelligence,
                            icon = "🧠",
                            availableAp = profile.availableAp,
                            onAllocate = { onAllocateStat(StatType.INT) }
                        )

                        StatAttributeRow(
                            statName = "PER",
                            statFullLabel = "Perception (Discipline & Habit)",
                            value = profile.perception,
                            icon = "👁",
                            availableAp = profile.availableAp,
                            onAllocate = { onAllocateStat(StatType.PER) }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(88.dp))
        }
    }
}
