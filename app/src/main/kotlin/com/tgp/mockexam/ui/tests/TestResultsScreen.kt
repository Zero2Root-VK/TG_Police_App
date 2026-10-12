package com.tgp.mockexam.ui.tests

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TestResultsScreen(
    onNavigateHome: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Test Results") })
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Score: 75/100", style = MaterialTheme.typography.displaySmall, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(32.dp))
            
            ResultRow("Correct", "75")
            ResultRow("Wrong", "15")
            ResultRow("Skipped", "10")
            ResultRow("Accuracy", "83%")

            Spacer(modifier = Modifier.height(48.dp))
            Button(onClick = onNavigateHome, modifier = Modifier.fillMaxWidth()) {
                Text("Back to Home")
            }
        }
    }
}

@Composable
fun ResultRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, style = MaterialTheme.typography.titleMedium)
        Text(value, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
    }
}
