package com.example.ui.components

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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AwakenCardBg
import com.example.ui.theme.AwakenCardBgElevated
import com.example.ui.theme.AwakenCardBorder
import com.example.ui.theme.AwakenHoloCyan
import com.example.ui.theme.AwakenHoloCyanBright
import com.example.ui.theme.AwakenTextMuted
import com.example.ui.theme.AwakenTextPrimary
import com.example.ui.theme.AwakenTextSecondary
import com.example.ui.theme.AwakenTextWhite
import com.example.ui.theme.AwakenVioletGlow
import com.example.ui.theme.AwakenVioletLight
import com.example.ui.theme.AwakenVioletPrimary
import com.example.ui.theme.AwakenButtonPurpleDark
import com.example.ui.theme.RajdhaniFontFamily

/**
 * Draws holographic tech brackets and glowing notches inspired by Reference 1 & 2
 */
fun Modifier.holoFrame(
    accentColor: Color = AwakenHoloCyan,
    bracketLength: Float = 24f,
    strokeWidth: Float = 3f,
    showTechNotches: Boolean = true
): Modifier = this.drawBehind {
    val w = size.width
    val h = size.height

    // Corner brackets
    // Top-left
    drawLine(accentColor, Offset(0f, 0f), Offset(bracketLength, 0f), strokeWidth)
    drawLine(accentColor, Offset(0f, 0f), Offset(0f, bracketLength), strokeWidth)

    // Top-right
    drawLine(accentColor, Offset(w, 0f), Offset(w - bracketLength, 0f), strokeWidth)
    drawLine(accentColor, Offset(w, 0f), Offset(w, bracketLength), strokeWidth)

    // Bottom-left
    drawLine(accentColor, Offset(0f, h), Offset(bracketLength, h), strokeWidth)
    drawLine(accentColor, Offset(0f, h), Offset(0f, h - bracketLength), strokeWidth)

    // Bottom-right
    drawLine(accentColor, Offset(w, h), Offset(w - bracketLength, h), strokeWidth)
    drawLine(accentColor, Offset(w, h), Offset(w, h - bracketLength), strokeWidth)

    if (showTechNotches) {
        // Tech notches along top and bottom
        val midX = w / 2f
        drawLine(accentColor.copy(alpha = 0.5f), Offset(midX - 30f, 0f), Offset(midX + 30f, 0f), strokeWidth / 1.5f)
        drawLine(accentColor.copy(alpha = 0.5f), Offset(midX - 20f, h), Offset(midX + 20f, h), strokeWidth / 1.5f)
        // Outer decorative markers
        drawRect(accentColor, Offset(-4f, h / 2f - 12f), Size(2f, 24f))
        drawRect(accentColor, Offset(w + 2f, h / 2f - 12f), Size(2f, 24f))
    }
}

/**
 * RPG Gauge Bar for HP, MP, EXP with animated fill and glowing gradient
 */
@Composable
fun RpgGaugeBar(
    label: String,
    current: Int,
    max: Int,
    startColor: Color,
    endColor: Color,
    modifier: Modifier = Modifier,
    height: Dp = 14.dp,
    icon: @Composable (() -> Unit)? = null
) {
    val progress = if (max > 0) (current.toFloat() / max.toFloat()).coerceIn(0f, 1f) else 0f

    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (icon != null) {
                    icon()
                    Spacer(modifier = Modifier.width(6.dp))
                }
                Text(
                    text = label,
                    fontFamily = RajdhaniFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = AwakenTextWhite
                )
            }
            Text(
                text = "$current / $max",
                fontFamily = RajdhaniFontFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 12.sp,
                color = AwakenTextSecondary
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(height)
                .clip(RoundedCornerShape(height / 2))
                .background(Color(0xFF0F0A1C))
                .border(1.dp, AwakenCardBorder, RoundedCornerShape(height / 2))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(progress)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(height / 2))
                    .background(
                        Brush.horizontalGradient(
                            listOf(startColor, endColor)
                        )
                    )
            )
        }
    }
}

/**
 * RPG Attribute Row (STR, VIT, AGI, INT, PER) with Stat AP allocation button
 */
@Composable
fun StatAttributeRow(
    statName: String,
    statFullLabel: String,
    value: Int,
    icon: String,
    availableAp: Int,
    onAllocate: () -> Unit,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 0.9f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glowAlpha"
    )

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .border(
                1.dp,
                if (availableAp > 0) AwakenHoloCyan.copy(alpha = glowAlpha) else AwakenCardBorder,
                RoundedCornerShape(12.dp)
            ),
        color = AwakenCardBgElevated
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = icon,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(end = 10.dp)
                )
                Column {
                    Text(
                        text = "$statName :",
                        fontFamily = RajdhaniFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = AwakenHoloCyanBright
                    )
                    Text(
                        text = statFullLabel,
                        fontFamily = RajdhaniFontFamily,
                        fontSize = 11.sp,
                        color = AwakenTextMuted
                    )
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "$value",
                    fontFamily = RajdhaniFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = AwakenTextWhite,
                    modifier = Modifier.padding(end = 12.dp)
                )

                if (availableAp > 0) {
                    IconButton(
                        onClick = onAllocate,
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.linearGradient(
                                    listOf(AwakenVioletPrimary, AwakenButtonPurpleDark)
                                )
                            )
                            .testTag("allocate_${statName.lowercase()}")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Allocate to $statName",
                            tint = AwakenTextWhite,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }
}
