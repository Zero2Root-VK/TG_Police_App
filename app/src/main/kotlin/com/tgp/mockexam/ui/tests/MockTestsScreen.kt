package com.tgp.mockexam.ui.tests

import androidx.compose.foundation.BorderStroke
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
fun MockTestsScreen(
    onTestSelected: (String) -> Unit
) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Full-Length Tests", "Subject-Wise Tests", "All Access")

    val fullLengthTests = listOf(
        MockTestItem("Full Length Mock Test - Paper 1", true, "Available"),
        MockTestItem("Full Length Mock Test - Paper 2", true, "Available"),
        MockTestItem("Full Length Mock Test - Paper 3", false, "Locked"),
        MockTestItem("Full Length Mock Test - Paper 4", false, "Locked")
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF3F6F9))
    ) {
        // Top Header Placeholder
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp)
                .background(Color(0xFF0F4C81)),
            contentAlignment = Alignment.Center
        ) {
            Text("Header: Mock Tests\nPractice Improve Succeed", color = Color.White)
        }

        // Exam Selection Dropdown Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .offset(y = (-30).dp),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier.size(48.dp).background(NavyPrimary, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Security, contentDescription = null, tint = Color(0xFFFFC107))
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text("Selected Exam", color = Color.Gray, fontSize = 12.sp)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Both Exams", color = NavyPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        Icon(Icons.Default.KeyboardArrowDown, contentDescription = null, tint = NavyPrimary)
                    }
                }
                Spacer(modifier = Modifier.weight(1f))
                Column(horizontalAlignment = Alignment.End) {
                    Text("SI & Constable", color = NavyPrimary, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    Text("Telangana Police", color = Color.Gray, fontSize = 12.sp)
                }
            }
        }

        // Tabs
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .offset(y = (-15).dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            tabs.forEachIndexed { index, title ->
                val isSelected = selectedTab == index
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 4.dp)
                        .background(
                            if (isSelected) NavyPrimary else Color.White,
                            RoundedCornerShape(8.dp)
                        )
                        .clickable { selectedTab = index }
                        .padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = title,
                        color = if (isSelected) Color.White else NavyPrimary,
                        fontSize = 12.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                }
            }
        }

        Text(
            "Full-Length Mock Tests",
            color = NavyPrimary,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Text(
            "Complete papers based on real exam pattern",
            color = Color.Gray,
            fontSize = 14.sp,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(fullLengthTests) { test ->
                TestCard(test, onTestSelected)
            }
            item { Spacer(modifier = Modifier.height(80.dp)) } // Bottom nav space
        }
    }
}

data class MockTestItem(val name: String, val isFree: Boolean, val status: String)

@Composable
fun TestCard(test: MockTestItem, onClick: (String) -> Unit) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color(0xFFE0E0E0)),
        modifier = Modifier.fillMaxWidth().clickable { onClick(test.name) }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(50.dp).background(Color(0xFFE0F2FE), CircleShape), contentAlignment = Alignment.Center) {
                        Icon(Icons.Default.Assignment, contentDescription = null, tint = NavyPrimary)
                        if (!test.isFree) {
                            Icon(Icons.Default.Lock, contentDescription = null, tint = NavyPrimary, modifier = Modifier.align(Alignment.BottomEnd).size(16.dp))
                        }
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(test.name, color = NavyPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.width(8.dp))
                            Box(
                                modifier = Modifier
                                    .background(if (test.isFree) Color(0xFF388E3C) else Color(0xFFD32F2F), RoundedCornerShape(12.dp))
                                    .padding(horizontal = 8.dp, vertical = 2.dp)
                            ) {
                                Text(if (test.isFree) "FREE" else "PAID", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Row {
                            InfoItem(Icons.Default.HelpOutline, "100", "Questions")
                            Spacer(modifier = Modifier.width(16.dp))
                            InfoItem(Icons.Default.Schedule, "120 min", "Duration")
                            Spacer(modifier = Modifier.width(16.dp))
                            InfoItem(Icons.Default.Star, "200", "Total Marks")
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(color = Color(0xFFF0F0F0))
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("• Based on previous year pattern", color = Color.Gray, fontSize = 12.sp)
                if (test.isFree) {
                    Button(
                        onClick = { onClick(test.name) },
                        colors = ButtonDefaults.buttonColors(containerColor = NavyPrimary),
                        shape = RoundedCornerShape(50),
                        contentPadding = PaddingValues(horizontal = 24.dp)
                    ) {
                        Text("Start Test ➔", fontSize = 12.sp)
                    }
                } else {
                    OutlinedButton(
                        onClick = { },
                        border = BorderStroke(1.dp, Color.Gray),
                        shape = RoundedCornerShape(50),
                        contentPadding = PaddingValues(horizontal = 24.dp)
                    ) {
                        Icon(Icons.Default.Lock, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Unlock Test", color = Color.Gray, fontSize = 12.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun InfoItem(icon: androidx.compose.ui.graphics.vector.ImageVector, value: String, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, contentDescription = null, tint = NavyPrimary, modifier = Modifier.size(16.dp))
        Spacer(modifier = Modifier.width(4.dp))
        Column {
            Text(value, color = NavyPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Text(label, color = Color.Gray, fontSize = 10.sp)
        }
    }
}
