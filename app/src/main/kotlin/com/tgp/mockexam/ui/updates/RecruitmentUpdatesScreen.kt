package com.tgp.mockexam.ui.updates

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
fun RecruitmentUpdatesScreen(
    onNavigateTo: (String) -> Unit
) {
    var selectedTab by remember { mutableStateOf(0) }
    var selectedFilter by remember { mutableStateOf(0) }

    val tabs = listOf("All Updates", "Notifications", "Exam Schedule", "Results")
    val filters = listOf("All", "Application Dates", "Hall Tickets", "Answer Keys", "Results")

    val updatesList = listOf(
        UpdateNotice("Telangana Police SI & Constable Recruitment Notification 2025", "Online applications are invited for the posts of Sub-Inspector (SI) and Constable in Telangana Police.", "Official Notice", Color(0xFFE8F5E9), Color(0xFF2E7D32), "10 Oct 2025", "Application Start: 15 Oct 2025", "Last Date: 14 Nov 2025", "View on TSLPRB >"),
        UpdateNotice("TS Police SI & Constable Hall Ticket 2025", "Hall tickets for the written examination will be available on the official website.", "Hall Ticket", Color(0xFFE3F2FD), Color(0xFF1976D2), "25 Nov 2025", "Exam: 07 Dec 2025", "", "View on TSLPRB >"),
        UpdateNotice("Exam Schedule – Telangana Police 2025", "Detailed exam schedule for SI and Constable posts has been released. Check dates, shifts and important instructions.", "Important", Color(0xFFFFF8E1), Color(0xFFF57F17), "20 Nov 2025", "Exam: 07 Dec 2025 – 21 Dec 2025", "", "View Details >"),
        UpdateNotice("Answer Key – SI & Constable 2025", "Provisional answer key for the written examination is now available. Objections can be raised within the given time.", "Published", Color(0xFFE8F5E9), Color(0xFF2E7D32), "10 Dec 2025", "Last Date for Objections: 14 Dec 2025", "", "View Answer Key >"),
        UpdateNotice("SI & Constable Results 2025", "Final results for the written examination will be published on the official website.", "Upcoming", Color(0xFFE3F2FD), Color(0xFF1976D2), "To be announced", "Expected: Dec 2025", "", "View Details >"),
        UpdateNotice("Important Instructions for Candidates", "Read the general instructions, document verification process, and other important guidelines before the exam.", "General", Color(0xFFE3F2FD), Color(0xFF1976D2), "08 Dec 2025", "For all candidates", "", "View Notice >")
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
                    selected = false,
                    onClick = { onNavigateTo("materials") }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Notifications, contentDescription = "Notifications") },
                    label = { Text("Notifications", fontWeight = FontWeight.Bold) },
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
            // Top Banner Placeholder
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
                            Text("Recruitment Updates", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                        }
                        Text("Telangana Police SI & Constable", color = Color.White.copy(alpha = 0.8f), fontSize = 12.sp, modifier = Modifier.padding(start = 32.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Main Category Tabs
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                tabs.forEachIndexed { index, title ->
                    val isSelected = selectedTab == index
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .background(if (isSelected) NavyPrimary else Color.White, RoundedCornerShape(8.dp))
                            .clickable { selectedTab = index }
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            title,
                            color = if (isSelected) Color.White else NavyPrimary,
                            fontSize = 11.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            maxLines = 1
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Sub-filter Chips
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filters.size) { index ->
                    val isSelected = selectedFilter == index
                    FilterChip(
                        selected = isSelected,
                        onClick = { selectedFilter = index },
                        label = { Text(filters[index], fontSize = 11.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal) },
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

            Spacer(modifier = Modifier.height(16.dp))

            // Updates List
            LazyColumn(
                contentPadding = PaddingValues(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(updatesList) { notice ->
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
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
                                        modifier = Modifier.size(40.dp).background(Color(0xFFE3F2FD), CircleShape),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(Icons.Default.Notifications, contentDescription = null, tint = NavyPrimary, modifier = Modifier.size(20.dp))
                                    }
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column {
                                        Text(notice.title, color = NavyPrimary, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(notice.description, color = Color.Gray, fontSize = 11.sp, lineHeight = 14.sp)
                                    }
                                }

                                Box(
                                    modifier = Modifier
                                        .background(notice.badgeBg, RoundedCornerShape(12.dp))
                                        .padding(horizontal = 8.dp, vertical = 2.dp)
                                ) {
                                    Text(notice.badgeText, color = notice.badgeColor, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.DateRange, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(12.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(notice.date, color = Color.Gray, fontSize = 10.sp)

                                    if (notice.detail1.isNotEmpty()) {
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text("  |  ${notice.detail1}", color = Color.Gray, fontSize = 10.sp)
                                    }
                                }

                                Text(notice.actionText, color = Color(0xFF1976D2), fontSize = 11.sp, fontWeight = FontWeight.Bold)
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
                        Text("Always refer to the official website (tslprb.in) for the latest and authentic updates.", color = Color.Gray, fontSize = 10.sp)
                    }
                }
            }
        }
    }
}

data class UpdateNotice(
    val title: String,
    val description: String,
    val badgeText: String,
    val badgeBg: Color,
    val badgeColor: Color,
    val date: String,
    val detail1: String,
    val detail2: String,
    val actionText: String
)
