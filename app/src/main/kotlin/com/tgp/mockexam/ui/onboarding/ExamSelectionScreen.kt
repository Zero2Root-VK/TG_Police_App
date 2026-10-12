package com.tgp.mockexam.ui.onboarding

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tgp.mockexam.ui.auth.NavyPrimary

@Composable
fun ExamSelectionScreen(
    onSelectionComplete: (List<String>) -> Unit
) {
    var selectedOption by remember { mutableStateOf<String?>(null) }
    
    val blueHighlight = Color(0xFFE3F2FD)
    val yellowHighlight = Color(0xFFFFF8E1)
    val yellowBorder = Color(0xFFFFC107)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF3F6F9))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 100.dp) // Space for bottom bar
        ) {
            // Header Image Placeholder
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(Color(0xFFE8F2F9)),
                contentAlignment = Alignment.Center
            ) {
                Text("Export Top Image (Logo & Charminar)\nPlace in res/drawable", color = Color.Gray)
            }

            Column(modifier = Modifier.padding(24.dp)) {
                Text(
                    text = "Choose Your Exam",
                    color = NavyPrimary,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.ExtraBold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Select the exam you want to prepare for",
                    color = Color.Gray,
                    fontSize = 16.sp
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Card 1: Police SI
                ExamCard(
                    title = "Police SI",
                    subtitle = "Sub-Inspector of Police",
                    badgeText = "Higher Rank",
                    badgeColor = Color(0xFF1976D2),
                    features = listOf(
                        Feature(Icons.Default.VerifiedUser, "Leadership Role"),
                        Feature(Icons.Default.Star, "More Responsibilities")
                    ),
                    isSelected = selectedOption == "SI",
                    selectedBgColor = blueHighlight,
                    selectedBorderColor = NavyPrimary,
                    onClick = { selectedOption = "SI" }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Card 2: Police Constable
                ExamCard(
                    title = "Police Constable",
                    subtitle = "Serve | Protect | Build a Safer Telangana",
                    badgeText = "Entry Level",
                    badgeColor = Color(0xFF388E3C),
                    features = listOf(
                        Feature(Icons.Default.VerifiedUser, "Ground Level Role"),
                        Feature(Icons.Default.Star, "Great Opportunities")
                    ),
                    isSelected = selectedOption == "Constable",
                    selectedBgColor = blueHighlight,
                    selectedBorderColor = NavyPrimary,
                    onClick = { selectedOption = "Constable" }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Card 3: Both Exams
                ExamCard(
                    title = "Both Exams",
                    subtitle = "Prepare for SI & Constable Together",
                    badgeText = "Complete Preparation",
                    badgeColor = yellowBorder,
                    features = listOf(
                        Feature(Icons.Default.CheckCircle, "Save Time"),
                        Feature(Icons.Default.MenuBook, "Better Preparation"),
                        Feature(Icons.Default.TrendingUp, "More Chances")
                    ),
                    isSelected = selectedOption == "Both",
                    selectedBgColor = yellowHighlight,
                    selectedBorderColor = yellowBorder,
                    onClick = { selectedOption = "Both" }
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Tip Box
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFE8F2F9), RoundedCornerShape(12.dp))
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Lightbulb, contentDescription = "Tip", tint = yellowBorder, modifier = Modifier.size(24.dp))
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text("No matter which path you choose,", color = NavyPrimary, fontSize = 13.sp)
                        Text("we are here to support your journey.", color = NavyPrimary, fontSize = 13.sp)
                    }
                    Text("Jai Telangana", color = Color(0xFF1976D2), fontSize = 14.sp, modifier = Modifier.padding(start = 8.dp))
                }
            }
        }

        // Bottom Bar (Mocked curve)
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(NavyPrimary, shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
                .padding(24.dp)
        ) {
            Button(
                onClick = { 
                    val result = when(selectedOption) {
                        "SI" -> listOf("SI")
                        "Constable" -> listOf("Constable")
                        "Both" -> listOf("SI", "Constable")
                        else -> emptyList()
                    }
                    onSelectionComplete(result)
                },
                enabled = selectedOption != null,
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(containerColor = NavyPrimary),
                border = BorderStroke(1.dp, Color.White),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Continue", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(Icons.Default.ArrowForward, contentDescription = "Arrow Right", tint = Color.White, modifier = Modifier.size(18.dp))
                }
            }
        }
    }
}

data class Feature(val icon: ImageVector, val label: String)

@Composable
fun ExamCard(
    title: String,
    subtitle: String,
    badgeText: String,
    badgeColor: Color,
    features: List<Feature>,
    isSelected: Boolean,
    selectedBgColor: Color,
    selectedBorderColor: Color,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = if (isSelected) selectedBgColor else Color.White),
        border = BorderStroke(
            width = if (isSelected) 2.dp else 1.dp,
            color = if (isSelected) selectedBorderColor else Color.LightGray
        ),
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Image Placeholder
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(Color.LightGray),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Person, contentDescription = null, tint = Color.White)
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(title, color = NavyPrimary, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .background(badgeColor, RoundedCornerShape(12.dp))
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text(badgeText, color = if(badgeColor == Color(0xFFFFC107)) NavyPrimary else Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(subtitle, color = Color.Gray, fontSize = 13.sp)

                Spacer(modifier = Modifier.height(12.dp))

                // Features
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    features.forEach { feature ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(feature.icon, contentDescription = null, tint = NavyPrimary, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(feature.label, color = Color.DarkGray, fontSize = 11.sp)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Radio Button
            RadioButton(
                selected = isSelected,
                onClick = null, // handled by card
                colors = RadioButtonDefaults.colors(selectedColor = NavyPrimary)
            )
        }
    }
}
