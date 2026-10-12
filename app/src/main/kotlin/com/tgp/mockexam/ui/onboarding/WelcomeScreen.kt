package com.tgp.mockexam.ui.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

val BottomNavyColor = Color(0xFF10284B)
val ButtonYellowColor = Color(0xFFFFC436)

// Custom shape to draw the downward curve separating the image and the navy section
val CurvedTopShape = GenericShape { size, _ ->
    moveTo(0f, 0f)
    quadraticBezierTo(
        size.width / 2f, size.height * 0.08f, // Pulls the curve downwards in the middle
        size.width, 0f
    )
    lineTo(size.width, size.height)
    lineTo(0f, size.height)
    close()
}

@Composable
fun WelcomeScreen(
    onNavigateToLogin: () -> Unit,
    onNavigateToSignUp: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        // Top Background Image Placeholder 
        // Note: You will need to export the top background from Figma/Design as a PNG,
        // place it in your res/drawable folder, and replace this Box with an Image composable.
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.65f)
                .background(Color(0xFFE0F2FE))
                .align(Alignment.TopCenter),
            contentAlignment = Alignment.Center
        ) {
            Text(
                "Export Top Image & Add to res/drawable\n(e.g., R.drawable.welcome_bg)", 
                color = Color.DarkGray,
                textAlign = TextAlign.Center
            )
        }

        // Bottom Curved Navy Section
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.42f)
                .align(Alignment.BottomCenter)
                .background(BottomNavyColor, shape = CurvedTopShape)
                .padding(top = 48.dp, bottom = 16.dp, start = 24.dp, end = 24.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Title Area
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Prepare for Telangana Police",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "SI & Constable Exams",
                        color = Color.White.copy(alpha = 0.9f),
                        fontSize = 16.sp
                    )
                }
                
                // Icons Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    FeatureIcon(Icons.Default.Edit, "Mock Tests")
                    FeatureIcon(Icons.Default.DateRange, "Current\nAffairs")
                    FeatureIcon(Icons.Default.CheckCircle, "Daily Quiz")
                    FeatureIcon(Icons.Default.Info, "Study\nMaterials")
                    FeatureIcon(Icons.Default.Notifications, "Updates")
                }

                // Buttons Area
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Button(
                        onClick = onNavigateToSignUp,
                        colors = ButtonDefaults.buttonColors(containerColor = ButtonYellowColor),
                        shape = RoundedCornerShape(50),
                        modifier = Modifier.fillMaxWidth().height(48.dp)
                    ) {
                        Text(
                            text = "Sign Up  ➔",
                            color = BottomNavyColor,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }

                    OutlinedButton(
                        onClick = onNavigateToLogin,
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color.White),
                        shape = RoundedCornerShape(50),
                        modifier = Modifier.fillMaxWidth().height(48.dp)
                    ) {
                        Text(
                            text = "Login",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }
                }

                // Footer Area
                Text(
                    text = "Independent App  |  Not affiliated with Telangana Govt.",
                    color = Color.White.copy(alpha = 0.6f),
                    fontSize = 11.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }
        }
    }
}

@Composable
fun FeatureIcon(icon: ImageVector, label: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = Color.White,
            modifier = Modifier.size(24.dp)
        )
        Text(
            text = label,
            color = Color.White,
            fontSize = 11.sp,
            textAlign = TextAlign.Center,
            lineHeight = 14.sp
        )
    }
}
