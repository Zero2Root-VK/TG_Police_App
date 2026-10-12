package com.tgp.mockexam.ui.materials

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
fun StudyMaterialsScreen(
    onNavigateTo: (String) -> Unit
) {
    var selectedExamTab by remember { mutableStateOf(0) }
    var selectedCategory by remember { mutableStateOf(0) }

    val examTabs = listOf("SI Exam", "Constable Exam", "Both Exams")
    val categories = listOf("Syllabus", "Subject Notes", "PDFs", "Saved Materials")

    val materials = listOf(
        MaterialDoc("TS Police SI Syllabus 2025", "Detailed syllabus for Sub-Inspector (SI) exam.", "PDF", "1.2 MB", "FREE", true),
        MaterialDoc("TS Police Constable Syllabus 2025", "Detailed syllabus for Constable exam.", "PDF", "1.1 MB", "FREE", true),
        MaterialDoc("Exam Pattern & Marking Scheme", "Complete exam pattern, marks distribution and selection process.", "PDF", "850 KB", "FREE", false),
        MaterialDoc("General Studies (GS) – Important Topics", "Topic-wise important concepts and key points.", "PDF", "2.3 MB", "SI + Constable", true),
        MaterialDoc("Telangana State History & Culture", "Important facts, heritage, freedom movement, etc.", "PDF", "1.5 MB", "SI + Constable", true),
        MaterialDoc("Current Affairs Notes (Monthly)", "Important events, schemes, government updates.", "PDF", "1.8 MB", "FREE", true),
        MaterialDoc("Previous Year Question Papers (Analysis)", "Topic-wise and year-wise analysis (2018–2024).", "PDF", "3.4 MB", "SI + Constable", true)
    )

    Scaffold(
        bottomBar = {
            NavigationBar(containerColor = Color.White, tonalElevation = 8.dp) {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                    label = { Text("Home") },
                    selected = false,
                    onClick = { onNavigateTo("home") }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Assignment, contentDescription = "Mock Tests") },
                    label = { Text("Mock Tests") },
                    selected = false,
                    onClick = { onNavigateTo("tests") }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Article, contentDescription = "Current Affairs") },
                    label = { Text("Current Affairs") },
                    selected = false,
                    onClick = { onNavigateTo("current_affairs") }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.MenuBook, contentDescription = "Study Materials") },
                    label = { Text("Study Materials", fontWeight = FontWeight.Bold) },
                    selected = true,
                    onClick = { },
                    colors = NavigationBarItemDefaults.colors(selectedIconColor = NavyPrimary, selectedTextColor = NavyPrimary, indicatorColor = Color.Transparent)
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.MoreHoriz, contentDescription = "More") },
                    label = { Text("More") },
                    selected = false,
                    onClick = { onNavigateTo("profile") }
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF8FAFC))
                .padding(paddingValues)
        ) {
            // Header Image Placeholder
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(110.dp)
                    .background(Color(0xFF0F4C81)),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White, modifier = Modifier.clickable { onNavigateTo("home") })
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Study Materials", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                        }
                        Text("Learn Today  •  Achieve Tomorrow", color = Color.White.copy(alpha = 0.8f), fontSize = 12.sp, modifier = Modifier.padding(start = 32.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Exam Tabs
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                examTabs.forEachIndexed { index, title ->
                    val isSelected = selectedExamTab == index
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .background(
                                if (isSelected) NavyPrimary else Color.White,
                                RoundedCornerShape(8.dp)
                            )
                            .clickable { selectedExamTab = index }
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            title,
                            color = if (isSelected) Color.White else NavyPrimary,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Category Tiles Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                categories.forEachIndexed { index, catName ->
                    val isSelected = selectedCategory == index
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSelected) Color(0xFFE3F2FD) else Color.White
                        ),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (isSelected) Color(0xFF1976D2) else Color(0xFFE0E0E0)
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .clickable { selectedCategory = index }
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 12.dp, horizontal = 4.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                when (index) {
                                    0 -> Icons.Default.FormatListNumbered
                                    1 -> Icons.Default.MenuBook
                                    2 -> Icons.Default.PictureAsPdf
                                    else -> Icons.Default.Bookmark
                                },
                                contentDescription = null,
                                tint = NavyPrimary,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(catName, color = NavyPrimary, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // List Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Syllabus", color = NavyPrimary, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Text("Download the latest syllabus and exam pattern.", color = Color.Gray, fontSize = 12.sp)
                }

                Text("All ⌄", color = NavyPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Documents List
            LazyColumn(
                contentPadding = PaddingValues(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(materials) { doc ->
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .background(Color(0xFFFFEBEE), RoundedCornerShape(8.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.PictureAsPdf, contentDescription = null, tint = Color(0xFFD32F2F), modifier = Modifier.size(28.dp))
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(doc.title, color = NavyPrimary, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(doc.description, color = Color.Gray, fontSize = 11.sp)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("${doc.type}  |  ${doc.size}", color = Color.Gray, fontSize = 10.sp)
                            }

                            Column(horizontalAlignment = Alignment.End) {
                                Box(
                                    modifier = Modifier
                                        .background(
                                            if (doc.badge == "FREE") Color(0xFFE8F5E9) else Color(0xFFE3F2FD),
                                            RoundedCornerShape(12.dp)
                                        )
                                        .padding(horizontal = 8.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        doc.badge,
                                        color = if (doc.badge == "FREE") Color(0xFF2E7D32) else Color(0xFF1976D2),
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                Button(
                                    onClick = { },
                                    shape = RoundedCornerShape(50),
                                    colors = ButtonDefaults.buttonColors(containerColor = NavyPrimary),
                                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                                    modifier = Modifier.height(32.dp)
                                ) {
                                    Icon(if (doc.isDownload) Icons.Default.Download else Icons.Default.Book, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(if (doc.isDownload) "Download" else "Open", fontSize = 11.sp)
                                }
                            }
                        }
                    }
                }

                item {
                    // Tip Banner
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Info, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Some study materials are free. Premium materials available with All Access.", color = Color.Gray, fontSize = 10.sp)
                        }

                        Text("View All Plans ➔", color = Color(0xFF1976D2), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

data class MaterialDoc(val title: String, val description: String, val type: String, val size: String, val badge: String, val isDownload: Boolean)
