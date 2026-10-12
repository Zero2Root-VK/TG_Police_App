package com.tgp.mockexam.ui.tests

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tgp.mockexam.ui.auth.NavyPrimary
import com.tgp.mockexam.ui.auth.YellowAccent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TestPlayerScreen(
    testId: String,
    onSubmit: () -> Unit
) {
    var selectedOption by remember { mutableStateOf<Int?>(0) } // Default to option A selected for prototype
    var currentQuestion by remember { mutableStateOf(12) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF3F6F9))
    ) {
        // Top Exam Header Bar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF0F4C81))
                .padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Assignment, contentDescription = null, tint = Color.White, modifier = Modifier.size(24.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text("Full-Length Mock Test", color = Color.White.copy(alpha = 0.8f), fontSize = 11.sp)
                        Text("Paper 1 (SI & Constable)", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    }
                }

                Row(horizontalArrangement = Arrangement.spacedBy(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    HeaderMetaItem(Icons.Default.HelpOutline, "Questions", "100")
                    HeaderMetaItem(Icons.Default.Schedule, "Time Left", "01:56:32", isAlert = true)
                    HeaderMetaItem(Icons.Default.Star, "Total Marks", "200")
                    Icon(Icons.Default.Menu, contentDescription = "Menu", tint = Color.White, modifier = Modifier.size(24.dp))
                }
            }
        }

        Row(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Left Column: Question & Options
            Column(
                modifier = Modifier
                    .weight(0.65f)
                    .fillMaxHeight()
                    .verticalScroll(rememberScrollState())
            ) {
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .background(NavyPrimary, RoundedCornerShape(16.dp))
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Text("Q. $currentQuestion / 100", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            }
                            Text("Marks for this question: 2", color = Color.Gray, fontSize = 12.sp)
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            "Which of the following articles in the Indian Constitution deals with the Fundamental Rights?",
                            color = NavyPrimary,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            lineHeight = 22.sp
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        val options = listOf("A. Article 32", "B. Article 21", "C. Article 14", "D. Article 368")
                        options.forEachIndexed { index, optionText ->
                            val isSelected = selectedOption == index
                            Card(
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (isSelected) Color(0xFFE3F2FD) else Color.White
                                ),
                                border = BorderStroke(
                                    1.dp,
                                    if (isSelected) Color(0xFF1976D2) else Color(0xFFE0E0E0)
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 6.dp)
                                    .clickable { selectedOption = index }
                            ) {
                                Row(
                                    modifier = Modifier.padding(14.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    RadioButton(
                                        selected = isSelected,
                                        onClick = { selectedOption = index },
                                        colors = RadioButtonDefaults.colors(selectedColor = Color(0xFF1976D2))
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        optionText,
                                        color = NavyPrimary,
                                        fontSize = 14.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            OutlinedButton(
                                onClick = { },
                                border = BorderStroke(1.dp, NavyPrimary),
                                shape = RoundedCornerShape(50),
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(Icons.Default.BookmarkBorder, contentDescription = null, tint = NavyPrimary, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Mark for Review", color = NavyPrimary, fontSize = 12.sp)
                            }

                            OutlinedButton(
                                onClick = { selectedOption = null },
                                border = BorderStroke(1.dp, NavyPrimary),
                                shape = RoundedCornerShape(50),
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(Icons.Default.DeleteOutline, contentDescription = null, tint = NavyPrimary, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Clear Response", color = NavyPrimary, fontSize = 12.sp)
                            }
                        }
                    }
                }
            }

            // Right Column: Question Palette
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier
                    .weight(0.35f)
                    .fillMaxHeight()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text("Question Palette", color = NavyPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)

                    Spacer(modifier = Modifier.height(12.dp))

                    // Legend
                    PaletteLegend(Color(0xFF4CAF50), "Answered")
                    PaletteLegend(Color(0xFF9C27B0), "Marked for Review")
                    PaletteLegend(Color(0xFFE0E0E0), "Not Answered")

                    Spacer(modifier = Modifier.height(12.dp))

                    // Grid 1..20
                    val answeredList = setOf(1, 2, 3, 5, 6, 7, 8, 9)
                    val reviewList = setOf(4)
                    
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        for (row in 0..3) {
                            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                for (col in 1..5) {
                                    val qNum = row * 5 + col
                                    val isCurrent = qNum == currentQuestion
                                    val isAnswered = answeredList.contains(qNum)
                                    val isReview = reviewList.contains(qNum)

                                    val bgColor = when {
                                        isCurrent -> Color(0xFF1976D2)
                                        isAnswered -> Color(0xFF4CAF50)
                                        isReview -> Color(0xFF9C27B0)
                                        else -> Color(0xFFF0F0F0)
                                    }
                                    val textColor = if (isCurrent || isAnswered || isReview) Color.White else Color.DarkGray

                                    Box(
                                        modifier = Modifier
                                            .size(28.dp)
                                            .background(bgColor, RoundedCornerShape(6.dp))
                                            .clickable { currentQuestion = qNum },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(qNum.toString(), color = textColor, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    // Tip
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFFE3F2FD), RoundedCornerShape(8.dp))
                            .padding(8.dp)
                    ) {
                        Row(verticalAlignment = Alignment.Top) {
                            Icon(Icons.Default.Info, contentDescription = null, tint = Color(0xFF1976D2), modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("You can go back and change your answer anytime before submitting.", color = Color(0xFF1976D2), fontSize = 10.sp, lineHeight = 12.sp)
                        }
                    }
                }
            }
        }

        // Action Navigation Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            OutlinedButton(
                onClick = { if (currentQuestion > 1) currentQuestion-- },
                shape = RoundedCornerShape(50),
                border = BorderStroke(1.dp, NavyPrimary)
            ) {
                Icon(Icons.Default.ArrowBack, contentDescription = null, tint = NavyPrimary, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Previous", color = NavyPrimary, fontWeight = FontWeight.Bold)
            }

            Button(
                onClick = { if (currentQuestion < 100) currentQuestion++ },
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(containerColor = NavyPrimary)
            ) {
                Text("Save & Next", color = Color.White, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.width(6.dp))
                Icon(Icons.Default.ArrowForward, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
            }
        }

        // Submit Test Banner at Bottom
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.VerifiedUser, contentDescription = null, tint = NavyPrimary, modifier = Modifier.size(28.dp))
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text("Submit Test", color = NavyPrimary, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Text("Are you sure you want to submit the test?", color = Color.Gray, fontSize = 11.sp)
                    }
                }

                Button(
                    onClick = onSubmit,
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(containerColor = NavyPrimary),
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 10.dp)
                ) {
                    Icon(Icons.Default.Send, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Submit Test", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }
            }
        }
    }
}

@Composable
fun HeaderMetaItem(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, value: String, isAlert: Boolean = false) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, contentDescription = null, tint = if (isAlert) Color(0xFFFF5252) else Color.White, modifier = Modifier.size(16.dp))
        Spacer(modifier = Modifier.width(4.dp))
        Column {
            Text(label, color = Color.White.copy(alpha = 0.7f), fontSize = 9.sp)
            Text(value, color = if (isAlert) Color(0xFFFF5252) else Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun PaletteLegend(color: Color, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 2.dp)) {
        Box(modifier = Modifier.size(12.dp).background(color, RoundedCornerShape(3.dp)))
        Spacer(modifier = Modifier.width(6.dp))
        Text(label, color = Color.DarkGray, fontSize = 10.sp)
    }
}
