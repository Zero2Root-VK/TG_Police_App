package com.tgp.mockexam.ui.quiz

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tgp.mockexam.ui.auth.NavyPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DailyQuizResultsScreen(
    onNavigateHome: () -> Unit,
    onTryAgain: () -> Unit
) {
    val questions = listOf(
        ReviewItem(
            num = 1,
            question = "Which city is known as the \"City of Lakes\" in India?",
            options = "A. Udaipur   B. Jaipur   C. Bengaluru   D. Chennai",
            userAnswer = "A",
            correctAnswer = "A",
            isCorrect = true,
            explanation = "Udaipur is famously known as the \"City of Lakes\" due to its numerous lakes and beautiful water bodies."
        ),
        ReviewItem(
            num = 2,
            question = "The Indian Constitution was adopted on which date?",
            options = "A. 15 August 1947   B. 26 January 1950\nC. 2 October 1949   D. 1 November 1956",
            userAnswer = "B",
            correctAnswer = "B",
            isCorrect = true,
            explanation = "The Indian Constitution was adopted on 26 November 1949 and came into force on 26 January 1950."
        ),
        ReviewItem(
            num = 3,
            question = "Which is the highest peak in India?",
            options = "A. K2   B. Kanchenjunga   C. Nanda Devi   D. Everest",
            userAnswer = "B",
            correctAnswer = "B",
            isCorrect = true,
            explanation = "Kanchenjunga (8,586 m) is the third highest peak in the world and the highest peak in India."
        ),
        ReviewItem(
            num = 4,
            question = "What is the capital of Telangana?",
            options = "A. Hyderabad   B. Vijayawada   C. Amaravati   D. Warangal",
            userAnswer = "C",
            correctAnswer = "A",
            isCorrect = false,
            explanation = "Hyderabad is the capital of Telangana, while Amaravati is the capital of Andhra Pradesh."
        ),
        ReviewItem(
            num = 5,
            question = "Which Act gave India the right to self-rule in 1947?",
            options = "A. Indian Penal Code   B. Government of India Act 1935\nC. Rowlatt Act   D. Quit India Act",
            userAnswer = "B",
            correctAnswer = "B",
            isCorrect = true,
            explanation = "The Government of India Act 1935 laid the foundation for self-governance in India and paved the way for independence."
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF3F6F9))
    ) {
        // Top Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .background(Color(0xFF0F4C81)),
            contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text("Daily Quiz Results", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                        Text("Keep going! Every quiz makes you stronger.", color = Color.White.copy(alpha = 0.8f), fontSize = 11.sp)
                    }
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Score Banner
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Left Score Column
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier.size(54.dp).background(Color(0xFF1565C0), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.EmojiEvents, contentDescription = null, tint = Color(0xFFFFC107), modifier = Modifier.size(32.dp))
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text("Your Score", color = Color.Gray, fontSize = 12.sp)
                            Text("4 / 5", color = NavyPrimary, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                            Box(
                                modifier = Modifier
                                    .background(Color(0xFFE8F5E9), RoundedCornerShape(12.dp))
                                    .padding(horizontal = 8.dp, vertical = 2.dp)
                            ) {
                                Text("★ Great job! Keep it up!", color = Color(0xFF2E7D32), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    // Middle Stats
                    Column {
                        StatItem(Icons.Default.CheckCircle, Color(0xFF4CAF50), "Correct", "4")
                        Spacer(modifier = Modifier.height(4.dp))
                        StatItem(Icons.Default.Cancel, Color(0xFFF44336), "Incorrect", "1")
                        Spacer(modifier = Modifier.height(4.dp))
                        StatItem(Icons.Default.Schedule, Color(0xFF1976D2), "Time Taken", "02:18")
                    }

                    // Right Streak Column
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.LocalFireDepartment, contentDescription = null, tint = Color(0xFFFF9800), modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Column {
                                Text("Daily Streak", color = Color.Gray, fontSize = 10.sp)
                                Text("3 Days", color = NavyPrimary, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            StreakDot("Mon", true)
                            StreakDot("Tue", true)
                            StreakDot("Wed", true)
                            StreakDot("Thu", false)
                            StreakDot("Fri", false)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text("Question Review", color = NavyPrimary, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Text("Here are the correct answers and explanations for all 5 questions.", color = Color.Gray, fontSize = 12.sp)

            Spacer(modifier = Modifier.height(16.dp))

            // Review Items List
            questions.forEach { item ->
                ReviewCard(item)
                Spacer(modifier = Modifier.height(12.dp))
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Bottom Actions
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = onNavigateHome,
                    shape = RoundedCornerShape(50),
                    border = BorderStroke(1.dp, NavyPrimary),
                    modifier = Modifier.weight(1f).height(48.dp)
                ) {
                    Icon(Icons.Default.Home, contentDescription = null, tint = NavyPrimary, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Back to Home", color = NavyPrimary, fontWeight = FontWeight.Bold)
                }

                Button(
                    onClick = onTryAgain,
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(containerColor = NavyPrimary),
                    modifier = Modifier.weight(1f).height(48.dp)
                ) {
                    Icon(Icons.Default.Refresh, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Try Again", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        Text("(If allowed)", color = Color.White.copy(alpha = 0.7f), fontSize = 9.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

data class ReviewItem(
    val num: Int,
    val question: String,
    val options: String,
    val userAnswer: String,
    val correctAnswer: String,
    val isCorrect: Boolean,
    val explanation: String
)

@Composable
fun StatItem(icon: androidx.compose.ui.graphics.vector.ImageVector, tint: Color, label: String, value: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, contentDescription = null, tint = tint, modifier = Modifier.size(14.dp))
        Spacer(modifier = Modifier.width(4.dp))
        Column {
            Text(label, color = Color.Gray, fontSize = 9.sp)
            Text(value, color = NavyPrimary, fontWeight = FontWeight.Bold, fontSize = 11.sp)
        }
    }
}

@Composable
fun StreakDot(day: String, isDone: Boolean) {
    Box(
        modifier = Modifier
            .size(18.dp)
            .background(if (isDone) Color(0xFF4CAF50) else Color(0xFFE0E0E0), CircleShape),
        contentAlignment = Alignment.Center
    ) {
        if (isDone) Icon(Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(12.dp))
    }
}

@Composable
fun ReviewCard(item: ReviewItem) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Row(modifier = Modifier.weight(1f)) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .background(if (item.isCorrect) Color(0xFF4CAF50) else Color(0xFFF44336), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(item.num.toString(), color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(item.question, color = NavyPrimary, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(item.options, color = Color.Gray, fontSize = 11.sp, lineHeight = 14.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Row {
                            Text("Your Answer: ", color = Color.Gray, fontSize = 11.sp)
                            Text(item.userAnswer, color = if (item.isCorrect) Color(0xFF4CAF50) else Color(0xFFF44336), fontWeight = FontWeight.Bold, fontSize = 11.sp)
                            Spacer(modifier = Modifier.width(16.dp))
                            Text("Correct Answer: ", color = Color.Gray, fontSize = 11.sp)
                            Text(item.correctAnswer, color = Color(0xFF4CAF50), fontWeight = FontWeight.Bold, fontSize = 11.sp)
                        }
                    }
                }

                Box(
                    modifier = Modifier
                        .background(if (item.isCorrect) Color(0xFFE8F5E9) else Color(0xFFFFEBEE), RoundedCornerShape(12.dp))
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text(
                        if (item.isCorrect) "✓ Correct" else "✗ Incorrect",
                        color = if (item.isCorrect) Color(0xFF2E7D32) else Color(0xFFC62828),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Explanation Box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFE3F2FD), RoundedCornerShape(8.dp))
                    .padding(10.dp)
            ) {
                Row(verticalAlignment = Alignment.Top) {
                    Icon(Icons.Default.Lightbulb, contentDescription = null, tint = Color(0xFF1976D2), modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text("Explanation:", color = Color(0xFF1976D2), fontWeight = FontWeight.Bold, fontSize = 11.sp)
                        Text(item.explanation, color = Color(0xFF0D47A1), fontSize = 11.sp, lineHeight = 14.sp)
                    }
                }
            }
        }
    }
}
