package com.tgp.mockexam.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tgp.mockexam.ui.auth.NavyPrimary
import com.tgp.mockexam.ui.auth.YellowAccent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateTo: (String) -> Unit
) {
    val modules = listOf(
        ModuleData("Mock Tests", "Practice with real\nexam pattern", Icons.Default.Edit, Color(0xFFE3F2FD), "tests"),
        ModuleData("Current Affairs", "Latest updates &\nimportant topics", Icons.Default.Article, Color(0xFFE8F5E9), "current_affairs"),
        ModuleData("Daily Quiz", "5 questions every day\nboost your knowledge", Icons.Default.Lightbulb, Color(0xFFF3E5F5), "quiz"),
        ModuleData("Study Materials", "Notes, PDFs &\nimportant topics", Icons.Default.MenuBook, Color(0xFFFFF3E0), "materials"),
        ModuleData("Recruitment Updates", "Latest notifications &\njob alerts", Icons.Default.Notifications, Color(0xFFFFEBEE), "updates"),
        ModuleData("Previous Year Paper Analysis", "Patterns, concepts &\ntopic weightage", Icons.Default.Analytics, Color(0xFFE3F2FD), "analysis")
    )

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = Color.White,
                tonalElevation = 8.dp
            ) {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                    label = { Text("Home", fontWeight = FontWeight.Bold) },
                    selected = true,
                    onClick = { },
                    colors = NavigationBarItemDefaults.colors(selectedIconColor = NavyPrimary, selectedTextColor = NavyPrimary, indicatorColor = Color.Transparent)
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
                    icon = { Icon(Icons.Default.Lightbulb, contentDescription = "Quiz") },
                    label = { Text("Quiz") },
                    selected = false,
                    onClick = { onNavigateTo("quiz") }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.MoreHoriz, contentDescription = "More") },
                    label = { Text("More") },
                    selected = false,
                    onClick = { }
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
                    .height(140.dp)
                    .background(Color(0xFFE8F2F9)),
                contentAlignment = Alignment.Center
            ) {
                Text("Export Top Banner Image\nPlace in res/drawable", color = Color.Gray, textAlign = TextAlign.Center)
                // Simulated Top Bar inside image
                Row(
                    modifier = Modifier.fillMaxWidth().align(Alignment.TopCenter).padding(16.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    Icon(Icons.Default.Notifications, contentDescription = "Notifications", tint = NavyPrimary)
                    Spacer(modifier = Modifier.width(16.dp))
                    Icon(Icons.Default.AccountCircle, contentDescription = "Profile", tint = NavyPrimary)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                // Greeting Banner
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFE3F2FD), RoundedCornerShape(12.dp))
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.WavingHand, contentDescription = "Wave", tint = Color(0xFFFFC107), modifier = Modifier.size(32.dp))
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Hello, Aspirant!", color = NavyPrimary, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        Text("Keep going! Your preparation matters.", color = NavyPrimary, fontSize = 14.sp)
                    }
                    // Badge Placeholder
                    Text("Better\nPreparation", color = Color(0xFF1976D2), fontSize = 12.sp, fontWeight = FontWeight.Medium, textAlign = TextAlign.Right)
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Selected Exam Card
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE0E0E0)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier.size(48.dp).background(NavyPrimary, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Security, contentDescription = null, tint = YellowAccent)
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Selected Exam", color = Color.Gray, fontSize = 12.sp)
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("Both Exams", color = NavyPrimary, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.width(4.dp))
                                Icon(Icons.Default.Edit, contentDescription = "Edit", tint = NavyPrimary, modifier = Modifier.size(14.dp))
                            }
                            Text("SI & Constable  |  Telangana Police", color = Color.Gray, fontSize = 12.sp)
                        }
                        
                        OutlinedButton(
                            onClick = { },
                            shape = RoundedCornerShape(50),
                            contentPadding = PaddingValues(horizontal = 12.dp)
                        ) {
                            Text("Change >", color = NavyPrimary, fontSize = 12.sp)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Grid
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    items(modules) { module ->
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = module.bgColor),
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(1f)
                                .clickable { onNavigateTo(module.route) }
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(16.dp),
                                verticalArrangement = Arrangement.SpaceBetween
                            ) {
                                Box(
                                    modifier = Modifier.size(40.dp).background(Color.White, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(module.icon, contentDescription = null, tint = NavyPrimary)
                                }
                                
                                Column {
                                    Text(module.title, color = NavyPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(module.subtitle, color = Color.DarkGray, fontSize = 11.sp, lineHeight = 14.sp)
                                }
                                
                                Icon(Icons.Default.ArrowForward, contentDescription = null, tint = NavyPrimary, modifier = Modifier.align(Alignment.End).size(16.dp))
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Bottom Banner
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(NavyPrimary, RoundedCornerShape(8.dp))
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.TrackChanges, contentDescription = "Target", tint = Color.Red, modifier = Modifier.size(36.dp))
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text("Small Steps Every Day", color = Color.White, fontSize = 14.sp)
                        Text("Lead to Big Results", color = YellowAccent, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

data class ModuleData(val title: String, val subtitle: String, val icon: ImageVector, val bgColor: Color, val route: String)
