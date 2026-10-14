package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.components.holoFrame
import com.example.ui.theme.AwakenBackground
import com.example.ui.theme.AwakenCardBorder
import com.example.ui.theme.AwakenHoloCyan
import com.example.ui.theme.AwakenHoloCyanBright
import com.example.ui.theme.AwakenTextMuted
import com.example.ui.theme.AwakenTextPrimary
import com.example.ui.theme.AwakenTextWhite
import com.example.ui.theme.AwakenVioletGlow
import com.example.ui.theme.AwakenVioletLight
import com.example.ui.theme.AwakenVioletPrimary
import com.example.ui.theme.CinzelFontFamily
import com.example.ui.theme.RajdhaniFontFamily
import kotlinx.coroutines.delay
import kotlin.random.Random

private data class SparkParticle(
    val xRatio: Float,
    var yRatio: Float,
    val speed: Float,
    val size: Float,
    val isCyan: Boolean
)

@Composable
fun SystemOpeningScreen(
    onFinishOpening: () -> Unit,
    modifier: Modifier = Modifier
) {
    var stepIndex by remember { mutableIntStateOf(0) }
    val titleScale = remember { Animatable(0.7f) }
    val titleAlpha = remember { Animatable(0f) }
    val emblemScale = remember { Animatable(0.4f) }
    val emblemAlpha = remember { Animatable(0f) }

    // Particles data
    val particles = remember {
        List(28) {
            SparkParticle(
                xRatio = Random.nextFloat(),
                yRatio = Random.nextFloat(),
                speed = 0.0015f + Random.nextFloat() * 0.003f,
                size = 2f + Random.nextFloat() * 4f,
                isCyan = Random.nextBoolean()
            )
        }
    }

    val infiniteTransition = rememberInfiniteTransition(label = "openingSpin")
    val innerRotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(12000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "innerRotation"
    )
    val outerRotation by infiniteTransition.animateFloat(
        initialValue = 360f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(16000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "outerRotation"
    )
    val glowPulse by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glowPulse"
    )

    // Sequence timeline
    LaunchedEffect(Unit) {
        // Step 0: Emblem appears with scale
        emblemAlpha.animateTo(1f, animationSpec = tween(500))
        emblemScale.animateTo(1f, animationSpec = tween(600, easing = FastOutSlowInEasing))
        stepIndex = 1 // [ INITIALIZING THE SYSTEM... ]

        delay(600)
        stepIndex = 2 // [ PLAYER AUTHENTICATED: JIN-WOO ]

        delay(650)
        stepIndex = 3 // [ DAILY QUESTS LOADED ]

        delay(600)
        stepIndex = 4 // [ WELCOME BACK, HUNTER ]
        // Title reveals dramatically
        titleAlpha.animateTo(1f, animationSpec = tween(400))
        titleScale.animateTo(1f, animationSpec = tween(600, easing = FastOutSlowInEasing))

        // Wait to complete sequence
        delay(900)
        onFinishOpening()
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(AwakenBackground),
        contentAlignment = Alignment.Center
    ) {
        // Floating Sparks Canvas Layer
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height

            particles.forEach { p ->
                p.yRatio -= p.speed
                if (p.yRatio < 0f) p.yRatio = 1f

                val cx = p.xRatio * w
                val cy = p.yRatio * h
                val col = if (p.isCyan) AwakenHoloCyan.copy(alpha = 0.6f) else AwakenVioletLight.copy(alpha = 0.5f)
                drawCircle(color = col, radius = p.size, center = Offset(cx, cy))
            }
        }

        // Concentric Holographic Runic Arrays
        Box(
            modifier = Modifier.size(320.dp),
            contentAlignment = Alignment.Center
        ) {
            // Outer Runic Circle (counter-clockwise)
            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .rotate(outerRotation)
            ) {
                val r = size.minDimension / 2f - 16f
                drawCircle(
                    color = AwakenVioletPrimary.copy(alpha = 0.25f * glowPulse),
                    radius = r,
                    style = Stroke(
                        width = 2f,
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(18f, 10f, 6f, 10f), 0f)
                    )
                )

                // Outer tech brackets
                for (i in 0 until 4) {
                    val angle = (i * 90f) * (Math.PI / 180.0)
                    val cx = center.x + (r * Math.cos(angle)).toFloat()
                    val cy = center.y + (r * Math.sin(angle)).toFloat()
                    drawCircle(
                        color = AwakenHoloCyanBright.copy(alpha = 0.8f),
                        radius = 4f,
                        center = Offset(cx, cy)
                    )
                }
            }

            // Inner Runic Circle (clockwise)
            Canvas(
                modifier = Modifier
                    .size(240.dp)
                    .rotate(innerRotation)
            ) {
                val r = size.minDimension / 2f - 12f
                drawCircle(
                    color = AwakenHoloCyan.copy(alpha = 0.35f * glowPulse),
                    radius = r,
                    style = Stroke(width = 2.5f)
                )

                // Runic ticks
                for (i in 0 until 8) {
                    val angle = (i * 45f) * (Math.PI / 180.0)
                    val cx1 = center.x + ((r - 10f) * Math.cos(angle)).toFloat()
                    val cy1 = center.y + ((r - 10f) * Math.sin(angle)).toFloat()
                    val cx2 = center.x + ((r + 4f) * Math.cos(angle)).toFloat()
                    val cy2 = center.y + ((r + 4f) * Math.sin(angle)).toFloat()
                    drawLine(
                        color = AwakenVioletLight.copy(alpha = 0.7f),
                        start = Offset(cx1, cy1),
                        end = Offset(cx2, cy2),
                        strokeWidth = 2.5f
                    )
                }
            }

            // Radial Energy Glow Halo
            Box(
                modifier = Modifier
                    .size(170.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            listOf(
                                AwakenVioletPrimary.copy(alpha = 0.45f * glowPulse),
                                AwakenHoloCyan.copy(alpha = 0.15f * glowPulse),
                                Color.Transparent
                            )
                        )
                    )
            )

            // Center Monarch Crest / Rune
            Box(
                modifier = Modifier
                    .size(90.dp)
                    .scale(emblemScale.value)
                    .alpha(emblemAlpha.value)
                    .clip(RoundedCornerShape(22.dp))
                    .background(Color(0xFF160E2A))
                    .border(
                        1.5.dp,
                        AwakenHoloCyan.copy(alpha = glowPulse),
                        RoundedCornerShape(22.dp)
                    )
                    .holoFrame(accentColor = AwakenVioletPrimary, bracketLength = 16f),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_launcher_fg_img),
                    contentDescription = "Awaken Emblem",
                    modifier = Modifier.size(54.dp)
                )
            }
        }

        // Top-Right Skip Button
        Box(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(20.dp),
            contentAlignment = Alignment.TopEnd
        ) {
            Surface(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .clickable(onClick = onFinishOpening)
                    .border(1.dp, AwakenCardBorder, RoundedCornerShape(8.dp))
                    .testTag("skip_opening_btn"),
                color = Color(0x6618112C)
            ) {
                Text(
                    text = "SKIP >>",
                    fontFamily = RajdhaniFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    letterSpacing = 1.sp,
                    color = AwakenTextMuted,
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                )
            }
        }

        // Bottom Branding & Animated Terminal Sequence
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 70.dp, start = 24.dp, end = 24.dp),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Main App Title (Cinematic reveal)
            Box(
                modifier = Modifier
                    .scale(titleScale.value)
                    .alpha(titleAlpha.value),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "AWAKEN",
                        fontFamily = CinzelFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 38.sp,
                        letterSpacing = 8.sp,
                        color = AwakenTextWhite,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "❖ THE SYSTEM HAS AWAKENED ❖",
                        fontFamily = RajdhaniFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        letterSpacing = 3.sp,
                        color = AwakenHoloCyanBright,
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // System Terminal Telemetry Status Line
            Surface(
                modifier = Modifier
                    .fillMaxWidth(0.92f)
                    .clip(RoundedCornerShape(8.dp))
                    .border(1.dp, AwakenCardBorder, RoundedCornerShape(8.dp)),
                color = Color(0xF2100A1F)
            ) {
                Row(
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(7.dp)
                            .clip(CircleShape)
                            .background(
                                if (stepIndex >= 4) AwakenHoloCyanBright else AwakenVioletLight
                            )
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = when (stepIndex) {
                            0 -> "[ SYSTEM BOOT : INITIALIZING... ]"
                            1 -> "[ SYNCHRONIZING PLAYER TELEMETRY... ]"
                            2 -> "[ HUNTER : SUNG JIN-WOO • E-RANK ]"
                            3 -> "[ DAILY QUESTS LOADED • PENALTY READY ]"
                            else -> "[ WELCOME BACK, HUNTER ]"
                        },
                        fontFamily = RajdhaniFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        letterSpacing = 1.sp,
                        color = when (stepIndex) {
                            4 -> AwakenHoloCyanBright
                            else -> AwakenTextPrimary
                        },
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}
