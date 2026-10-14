package com.example.ui.screens

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.model.EvolutionTier
import com.example.model.PlayerProfile
import com.example.ui.components.RpgGaugeBar
import com.example.ui.theme.AwakenBackground
import com.example.ui.theme.AwakenCardBg
import com.example.ui.theme.AwakenCardBgElevated
import com.example.ui.theme.AwakenCardBorder
import com.example.ui.theme.AwakenCardBorderGlow
import com.example.ui.theme.AwakenHoloCyan
import com.example.ui.theme.AwakenHoloCyanBright
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
 * Evolution Screen & Character Profile matching Reference 3 evolution path
 */
@Composable
fun EvolutionScreen(
    profile: PlayerProfile,
    evolutionTiers: List<EvolutionTier>,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

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

            // Player Profile Hero Card (Reference 3 style)
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .border(1.dp, AwakenCardBorderGlow, RoundedCornerShape(20.dp))
                    .testTag("player_profile_card"),
                color = Color(0xF4130D26)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Avatar with glowing violet frame
                        Box(
                            modifier = Modifier
                                .size(72.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .border(1.5.dp, AwakenVioletPrimary, RoundedCornerShape(16.dp))
                                .background(Color(0xFF1D1438)),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.img_player_avatar),
                                contentDescription = "Hunter Avatar",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = profile.name,
                                    fontFamily = CinzelFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 20.sp,
                                    color = AwakenTextWhite
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Surface(
                                    shape = RoundedCornerShape(4.dp),
                                    color = AwakenVioletPrimary.copy(alpha = 0.25f),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, AwakenVioletPrimary)
                                ) {
                                    Text(
                                        text = "Lv.${profile.level}",
                                        fontFamily = RajdhaniFontFamily,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 11.sp,
                                        color = AwakenVioletLight,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = profile.title,
                                fontFamily = RajdhaniFontFamily,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 13.sp,
                                color = AwakenHoloCyanBright
                            )

                            Text(
                                text = profile.rank,
                                fontFamily = RajdhaniFontFamily,
                                fontSize = 12.sp,
                                color = AwakenTextMuted
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Level XP Gauge
                    RpgGaugeBar(
                        label = "AWAKENING EXP",
                        current = profile.currentXp,
                        max = profile.maxXp,
                        startColor = AwakenVioletPrimary,
                        endColor = AwakenHoloCyan
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Evolution Path Section Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "EVOLUTION PATH",
                    fontFamily = CinzelFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    letterSpacing = 2.sp,
                    color = AwakenTextWhite
                )

                Text(
                    text = "[ 4 STAGES ]",
                    fontFamily = RajdhaniFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    color = AwakenVioletLight
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Evolution Path Tiers (Reference 3 Right panel)
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                evolutionTiers.forEach { tier ->
                    EvolutionTierCard(tier = tier, currentLevel = profile.level)
                }
            }

            Spacer(modifier = Modifier.height(88.dp))
        }
    }
}

@Composable
private fun EvolutionTierCard(
    tier: EvolutionTier,
    currentLevel: Int,
    modifier: Modifier = Modifier
) {
    val isUnlocked = tier.isUnlocked

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .border(
                1.dp,
                if (isUnlocked) AwakenVioletPrimary.copy(alpha = 0.5f) else AwakenCardBorder,
                RoundedCornerShape(14.dp)
            ),
        color = if (isUnlocked) Color(0xFF160E2C) else Color(0xFF0F0B1C)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(
                                if (isUnlocked) AwakenVioletPrimary.copy(alpha = 0.3f) else Color(0xFF1A132B)
                            )
                            .border(
                                1.dp,
                                if (isUnlocked) AwakenVioletPrimary else AwakenCardBorder,
                                CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isUnlocked) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Unlocked",
                                tint = AwakenVioletLight,
                                modifier = Modifier.size(18.dp)
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = "Locked",
                                tint = AwakenTextMuted,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = tier.title,
                            fontFamily = CinzelFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = if (isUnlocked) AwakenTextWhite else AwakenTextMuted
                        )
                        Text(
                            text = "Stage ${tier.tier} • Requires Level ${tier.requiredLevel}",
                            fontFamily = RajdhaniFontFamily,
                            fontSize = 11.sp,
                            color = if (isUnlocked) AwakenHoloCyanBright else AwakenTextMuted
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = if (isUnlocked) Color(0x3310B981) else Color(0x2264748B)
                ) {
                    Text(
                        text = if (isUnlocked) "ACTIVE" else "LOCKED",
                        fontFamily = RajdhaniFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        color = if (isUnlocked) AwakenSuccessGreen else AwakenTextMuted,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = tier.subtitle,
                fontFamily = RajdhaniFontFamily,
                fontSize = 12.sp,
                color = AwakenTextMuted
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Unlocked buffs
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                tier.buffs.forEach { buff ->
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = if (isUnlocked) Color(0xFF231645) else Color(0xFF140F24),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (isUnlocked) AwakenVioletGlow else AwakenCardBorder
                        )
                    ) {
                        Text(
                            text = buff,
                            fontFamily = RajdhaniFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            color = if (isUnlocked) AwakenVioletLight else AwakenTextMuted,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }
        }
    }
}
