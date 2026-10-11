package com.tgp.mockexam.core.designsystem.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.tgp.mockexam.core.designsystem.R

/** Brand colors that fall outside the Material color roles. */
object TgpBrand {
    val Navy = Color(0xFF1B3A6B)
    val NavyDeep = Color(0xFF0E2547)
    val NavyBright = Color(0xFF2C5CA8)
    val Gold = Color(0xFFB8860B)
    val GoldBright = Color(0xFFFFD24D)
    val Khaki = Color(0xFF5A5F3A)

    val HeroGradient = Brush.linearGradient(listOf(NavyBright, Navy, NavyDeep))
    val GoldGradient = Brush.linearGradient(listOf(GoldBright, Gold))
}

/** The TGP police-shield mark. Use [onDark] on navy/gradient surfaces. */
@Composable
fun TgpShield(
    modifier: Modifier = Modifier,
    onDark: Boolean = false,
    contentDescription: String? = "TGP Mock Exam",
) {
    Image(
        painter = painterResource(
            if (onDark) R.drawable.ic_tgp_shield_light else R.drawable.ic_tgp_shield,
        ),
        contentDescription = contentDescription,
        modifier = modifier,
        contentScale = ContentScale.Fit,
    )
}

/** Shield on a rounded gradient tile — the app's primary brand lockup. */
@Composable
fun TgpLogoBadge(
    modifier: Modifier = Modifier,
    size: Int = 96,
) {
    Box(
        modifier = modifier
            .size(size.dp)
            .clip(RoundedCornerShape(percent = 28))
            .background(TgpBrand.GoldGradient),
        contentAlignment = Alignment.Center,
    ) {
        TgpShield(
            modifier = Modifier.size((size * 0.72f).toInt().dp),
            onDark = false,
        )
    }
}
