package com.example.ui.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.AwakenBackground
import com.example.ui.theme.AwakenButtonPurple
import com.example.ui.theme.AwakenHoloCyan
import com.example.ui.theme.AwakenTextMuted
import com.example.ui.theme.AwakenTextWhite
import com.example.ui.theme.AwakenVioletLight
import com.example.ui.theme.AwakenVioletPrimary
import com.example.ui.theme.CinzelFontFamily
import com.example.ui.theme.RajdhaniFontFamily

/**
 * Awakening Intro Screen matching Reference 2 left panel
 */
@Composable
fun AwakeningScreen(
    onEnterSystem: () -> Unit,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "awakeningAura")
    val glowPulse by infiniteTransition.animateFloat(
        initialValue = 0.6f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glowPulse"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(AwakenBackground)
    ) {
        // Hero Art Layer
        Image(
            painter = painterResource(id = R.drawable.img_awakening_hero),
            contentDescription = "Awakening Shadow Monarch",
            modifier = Modifier
                .fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Dark Atmospheric Gradient Overlay (fades into deep black purple at bottom and top)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            AwakenBackground.copy(alpha = 0.85f),
                            Color.Transparent,
                            Color(0xBB08060E),
                            AwakenBackground.copy(alpha = 0.98f)
                        )
                    )
                )
        )

        // Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(horizontal = 28.dp, vertical = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Top System Brand
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(top = 16.dp)
            ) {
                // System Rune Emblem
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(AwakenVioletPrimary.copy(alpha = 0.2f))
                        .border(1.dp, AwakenVioletPrimary.copy(alpha = glowPulse), RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "❖",
                        fontSize = 18.sp,
                        color = AwakenVioletLight
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "AWAKEN",
                    fontFamily = CinzelFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                    letterSpacing = 6.sp,
                    color = AwakenTextWhite
                )

                Text(
                    text = "[ SYSTEM V2.4 ONLINE ]",
                    fontFamily = RajdhaniFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 12.sp,
                    letterSpacing = 2.sp,
                    color = AwakenHoloCyan.copy(alpha = 0.9f)
                )
            }

            // Bottom Typography & Action Button (Reference 2 style)
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp)
            ) {
                Text(
                    text = "YOUR\nAWAKENING\nBEGINS NOW!",
                    fontFamily = CinzelFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 32.sp,
                    lineHeight = 40.sp,
                    letterSpacing = 2.5.sp,
                    color = AwakenTextWhite,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Walk your path. Take action. Conquer.\nBecome a legend!",
                    fontFamily = RajdhaniFontFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 15.sp,
                    lineHeight = 22.sp,
                    letterSpacing = 0.5.sp,
                    color = AwakenTextMuted,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(28.dp))

                Button(
                    onClick = onEnterSystem,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .clip(RoundedCornerShape(28.dp))
                        .border(
                            1.dp,
                            AwakenVioletLight.copy(alpha = 0.6f),
                            RoundedCornerShape(28.dp)
                        )
                        .testTag("get_started_button"),
                    shape = RoundedCornerShape(28.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFC4B5FD)
                    )
                ) {
                    Text(
                        text = "Get Started",
                        fontFamily = CinzelFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp,
                        letterSpacing = 1.sp,
                        color = Color(0xFF0F0A1F)
                    )
                }
            }
        }
    }
}
