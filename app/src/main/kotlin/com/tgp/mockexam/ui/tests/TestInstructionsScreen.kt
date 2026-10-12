package com.tgp.mockexam.ui.tests

import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tgp.mockexam.ui.auth.NavyPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TestInstructionsScreen(
    testId: String,
    onStartTest: () -> Unit,
    onCancel: () -> Unit
) {
    var accepted by remember { mutableStateOf(false) }

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
            Text("Header: Test Instructions\nRead carefully before starting", color = Color.White)
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Test Summary Card
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = Modifier.size(50.dp).background(Color(0xFFE0F2FE), CircleShape), contentAlignment = Alignment.Center) {
                            Icon(Icons.Default.Assignment, contentDescription = null, tint = NavyPrimary)
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Full-Length Mock Test", color = Color.Gray, fontSize = 12.sp)
                            Text("Police SI – Paper 1", color = NavyPrimary, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                            Text("Telangana Police SI | General Studies", color = Color.Gray, fontSize = 12.sp)
                        }
                        Box(
                            modifier = Modifier
                                .background(Color(0xFFE8F5E9), RoundedCornerShape(12.dp))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text("FREE", color = Color(0xFF2E7D32), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                        InfoItem(Icons.Default.HelpOutline, "100", "Questions")
                        InfoItem(Icons.Default.Schedule, "120 minutes", "Duration")
                        InfoItem(Icons.Default.Star, "200", "Total Marks")
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Marking Scheme
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.CheckCircleOutline, contentDescription = null, tint = NavyPrimary)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Marking Scheme", color = NavyPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.weight(1f)) {
                            MarkingRow(Icons.Default.CheckCircle, Color(0xFF4CAF50), "Correct Answer", "+2 marks")
                            Spacer(modifier = Modifier.height(8.dp))
                            MarkingRow(Icons.Default.Cancel, Color(0xFFF44336), "Incorrect Answer", "-0.5 marks")
                            Spacer(modifier = Modifier.height(8.dp))
                            MarkingRow(Icons.Default.RemoveCircle, Color(0xFF9E9E9E), "Unattempted Question", "0 marks")
                        }
                        
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .background(Color(0xFFE3F2FD), RoundedCornerShape(8.dp))
                                .padding(12.dp)
                        ) {
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Info, contentDescription = null, tint = NavyPrimary, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Note", color = NavyPrimary, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("There is negative marking. Make sure you are confident before selecting an answer.", color = Color(0xFF1976D2), fontSize = 12.sp)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Instructions
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.ListAlt, contentDescription = null, tint = NavyPrimary)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Important Instructions", color = NavyPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    InstructionItem(1, "The test consists of 100 multiple-choice questions (MCQs).")
                    InstructionItem(2, "Total time provided is 120 minutes. The timer will start immediately after you click 'Begin Test'.")
                    InstructionItem(3, "Each question carries 2 marks. Incorrect answers will be deducted 0.5 marks.")
                    InstructionItem(4, "You can review your answers before submitting (if enabled).")
                    InstructionItem(5, "Do not refresh, close or switch apps during the test. Your progress may be lost.")
                    InstructionItem(6, "Ensure you have a stable internet connection.")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Agreement
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFE8F2F9), RoundedCornerShape(8.dp))
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(checked = accepted, onCheckedChange = { accepted = it })
                Text("I have read and understood all the instructions and agree to follow them.", fontSize = 12.sp, color = NavyPrimary)
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = onStartTest,
                enabled = accepted,
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(containerColor = NavyPrimary),
                modifier = Modifier.fillMaxWidth().height(50.dp)
            ) {
                Text("Begin Test ➔", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(16.dp))

            TextButton(
                onClick = onCancel,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Cancel", color = NavyPrimary, fontWeight = FontWeight.Bold)
            }
            
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun MarkingRow(icon: androidx.compose.ui.graphics.vector.ImageVector, color: Color, label: String, value: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(16.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text(label, color = Color.DarkGray, fontSize = 12.sp, modifier = Modifier.weight(1f))
        Text(value, color = NavyPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun InstructionItem(number: Int, text: String) {
    Row(modifier = Modifier.padding(bottom = 12.dp).fillMaxWidth(), verticalAlignment = Alignment.Top) {
        Box(
            modifier = Modifier.size(20.dp).background(Color(0xFF1976D2), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(number.toString(), color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.width(12.dp))
        Text(text, color = Color.DarkGray, fontSize = 13.sp, lineHeight = 18.sp)
    }
}
