package com.tgp.mockexam.ui.currentaffairs

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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tgp.mockexam.ui.auth.NavyPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurrentAffairsScreen(
    onNavigateTo: (String) -> Unit
) {
    var selectedCategory by remember { mutableStateOf(0) }
    val categories = listOf("Latest News", "National", "State (Telangana)", "Schemes", "Important")

    val articles = listOf(
        NewsItem("SC directs Centre to frame rules for implementation of e-Courts project", "National", "13 Oct 2026", "The Supreme Court has directed the Union Government to frame rules for the full implementation of the e-Courts project..."),
        NewsItem("Telangana Govt announces ₹12,000 crore for infrastructure development", "Telangana", "12 Oct 2026", "The Telangana government has sanctioned ₹12,000 crore for infrastructure development in the state, focusing on roads, water supply and urban growth..."),
        NewsItem("India's GDP grows by 7.8% in Q2 FY26, beats estimates", "National", "11 Oct 2026", "India's economy grew by 7.8% in the July-September quarter of FY26, driven by strong performance in manufacturing and services sectors..."),
        NewsItem("ISRO successfully launches SSLV-D3 with earth observation satellite", "National", "10 Oct 2026", "The Indian Space Research Organisation (ISRO) on Saturday successfully launched the SSLV-D3 rocket carrying the EOS-09 satellite from Sriharikota..."),
        NewsItem("Digital India initiative completes 10 years, transforms governance", "Schemes", "09 Oct 2026", "The Digital India initiative, launched in 2015, has completed 10 years, bringing digital services to millions of citizens across the country...")
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
                    label = { Text("Current Affairs", fontWeight = FontWeight.Bold) },
                    selected = true,
                    onClick = { },
                    colors = NavigationBarItemDefaults.colors(selectedIconColor = NavyPrimary, selectedTextColor = NavyPrimary, indicatorColor = Color.Transparent)
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
                            Text("Current Affairs", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                        }
                        Text("Stay updated  •  Be exam ready", color = Color.White.copy(alpha = 0.8f), fontSize = 12.sp, modifier = Modifier.padding(start = 32.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Categories Row
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(categories.size) { index ->
                    val isSelected = selectedCategory == index
                    FilterChip(
                        selected = isSelected,
                        onClick = { selectedCategory = index },
                        label = { Text(categories[index], fontSize = 12.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal) },
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

            // Sub-header Banner
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
                        Icon(Icons.Default.DateRange, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Daily Current Affairs", color = NavyPrimary, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Text("Stay updated with important national & state news, government schemes and more.", color = Color.DarkGray, fontSize = 11.sp, lineHeight = 14.sp)
                    }
                    Text("Updated Daily", color = Color(0xFF1976D2), fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Articles List
            LazyColumn(
                contentPadding = PaddingValues(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(articles) { article ->
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(80.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color(0xFFE0E0E0)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.Image, contentDescription = null, tint = Color.Gray)
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .background(
                                                if (article.category == "Telangana") Color(0xFFE8F5E9) else Color(0xFFE3F2FD),
                                                RoundedCornerShape(12.dp)
                                            )
                                            .padding(horizontal = 8.dp, vertical = 2.dp)
                                    ) {
                                        Text(
                                            article.category,
                                            color = if (article.category == "Telangana") Color(0xFF2E7D32) else Color(0xFF1976D2),
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }

                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Default.DateRange, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(12.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(article.date, color = Color.Gray, fontSize = 10.sp)
                                    }
                                }

                                Spacer(modifier = Modifier.height(6.dp))

                                Text(article.title, color = NavyPrimary, fontWeight = FontWeight.Bold, fontSize = 13.sp, lineHeight = 17.sp)

                                Spacer(modifier = Modifier.height(4.dp))

                                Text(article.summary, color = Color.Gray, fontSize = 11.sp, maxLines = 2, lineHeight = 14.sp)
                            }

                            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Color.Gray, modifier = Modifier.align(Alignment.CenterVertically))
                        }
                    }
                }

                item {
                    // PYQ Banner
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD)),
                        modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Lightbulb, contentDescription = null, tint = NavyPrimary, modifier = Modifier.size(24.dp))
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text("Previous Year Questions (PYQs) Analysis", color = NavyPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                    Text("Understand the trend and important topics from past papers.", color = Color.Gray, fontSize = 10.sp)
                                }
                            }
                            Text("View Analysis ➔", color = Color(0xFF1976D2), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

data class NewsItem(val title: String, val category: String, val date: String, val summary: String)
