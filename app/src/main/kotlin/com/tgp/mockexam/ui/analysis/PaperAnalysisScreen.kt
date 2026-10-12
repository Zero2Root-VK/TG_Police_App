package com.tgp.mockexam.ui.analysis

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
fun PaperAnalysisScreen(
    onNavigateTo: (String) -> Unit
) {
    var selectedTrack by remember { mutableStateOf(0) }
    var selectedCategory by remember { mutableStateOf(0) }

    val categories = listOf("All", "Question Repetition", "Concepts & Topics", "Question Models", "Year-wise Weightage")

    val yearsList = listOf(
        PyqItem("2024", "SI", "Telangana SI Previous Year Paper - 2024", "12 Jan 2025", "100", "General Studies, Mental Ability, Arithmetic, Reasoning, English"),
        PyqItem("2023", "SI", "Telangana SI Previous Year Paper - 2023", "18 Dec 2023", "100", "General Studies, Mental Ability, Arithmetic, Reasoning, English"),
        PyqItem("2022", "SI", "Telangana SI Previous Year Paper - 2022", "16 Oct 2022", "100", "General Studies, Mental Ability, Arithmetic, Reasoning, English"),
        PyqItem("2021", "Constable", "Telangana Constable Previous Year Paper - 2021", "17 Oct 2021", "100", "General Studies, Mental Ability, Arithmetic, Reasoning, English"),
        PyqItem("2020", "Constable", "Telangana Constable Previous Year Paper - 2020", "15 Oct 2020", "100", "General Studies, Mental Ability, Arithmetic, Reasoning, English")
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
                    icon = { Icon(Icons.Default.MenuBook, contentDescription = "Study Materials") },
                    label = { Text("Study Materials") },
                    selected = true,
                    onClick = { onNavigateTo("materials") },
                    colors = NavigationBarItemDefaults.colors(selectedIconColor = NavyPrimary, selectedTextColor = NavyPrimary, indicatorColor = Color.Transparent)
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Notifications, contentDescription = "Notifications") },
                    label = { Text("Notifications") },
                    selected = false,
                    onClick = { onNavigateTo("updates") }
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
                            Text("Previous Year Paper Analysis", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        }
                        Text("Understand the pattern  •  Spot important topics  •  Prepare smarter", color = Color.White.copy(alpha = 0.8f), fontSize = 11.sp, modifier = Modifier.padding(start = 32.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Track Switcher (SI vs Constable)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .background(if (selectedTrack == 0) NavyPrimary else Color.White, RoundedCornerShape(8.dp))
                        .clickable { selectedTrack = 0 }
                        .padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Security, contentDescription = null, tint = if (selectedTrack == 0) Color.White else NavyPrimary, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("SI (Sub-Inspector)", color = if (selectedTrack == 0) Color.White else NavyPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .background(if (selectedTrack == 1) NavyPrimary else Color.White, RoundedCornerShape(8.dp))
                        .clickable { selectedTrack = 1 }
                        .padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Badge, contentDescription = null, tint = if (selectedTrack == 1) Color.White else NavyPrimary, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Constable", color = if (selectedTrack == 1) Color.White else NavyPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Category Filter Chips
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(categories.size) { index ->
                    val isSelected = selectedCategory == index
                    FilterChip(
                        selected = isSelected,
                        onClick = { selectedCategory = index },
                        label = { Text(categories[index], fontSize = 11.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = NavyPrimary,
                            selectedLabelColor = Color.White,
                            containerColor = Color.White,
                            labelColor = NavyPrimary
                        ),
                        shape = RoundedCornerShape(50)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Why PYQ Banner
            Card(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD))
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(modifier = Modifier.size(40.dp).background(NavyPrimary, CircleShape), contentAlignment = Alignment.Center) {
                        Icon(Icons.Default.TrackChanges, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text("Why Previous Year Papers?", color = NavyPrimary, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        Text("Get insights into frequently asked questions, important concepts, question patterns and topic-wise weightage to understand the exam trend.", color = Color.DarkGray, fontSize = 10.sp, lineHeight = 13.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // List Header
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Select a Year", color = NavyPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Text("All Years ⌄", color = NavyPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Papers List
            LazyColumn(
                contentPadding = PaddingValues(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(yearsList) { pyq ->
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
                                    .size(60.dp, 64.dp)
                                    .background(Color(0xFFE3F2FD), RoundedCornerShape(8.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(pyq.year, color = NavyPrimary, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                                    Text(pyq.track, color = Color(0xFF1976D2), fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                                }
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(pyq.title, color = NavyPrimary, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                Spacer(modifier = Modifier.height(4.dp))
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.DateRange, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(12.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Exam Date: ${pyq.examDate}", color = Color.Gray, fontSize = 10.sp)
                                }
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Assignment, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(12.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Total Questions: ${pyq.questions}", color = Color.Gray, fontSize = 10.sp)
                                }
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.MenuBook, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(12.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(pyq.subjects, color = Color.Gray, fontSize = 10.sp, maxLines = 1)
                                }
                            }

                            Column(horizontalAlignment = Alignment.End) {
                                Text("Detailed Analysis ➔", color = Color(0xFF1976D2), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }

                item {
                    // Tip Banner
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Info, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Analysis includes repeated questions, important concepts, question models and topic-wise/year-wise weightage.", color = Color.Gray, fontSize = 10.sp, lineHeight = 13.sp)
                    }
                }
            }
        }
    }
}

data class PyqItem(val year: String, val track: String, val title: String, val examDate: String, val questions: String, val subjects: String)
