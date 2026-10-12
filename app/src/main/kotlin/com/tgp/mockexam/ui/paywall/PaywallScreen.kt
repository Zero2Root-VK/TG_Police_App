package com.tgp.mockexam.ui.paywall

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaywallScreen() {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Unlock Premium Content") })
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
            Text("All Access Pass", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))
            Text("Get access to all mock tests, subject tests, and premium features.", style = MaterialTheme.typography.bodyLarge)
            
            Spacer(modifier = Modifier.height(32.dp))
            
            Button(
                onClick = { /* Launch Billing Flow */ },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Purchase - ₹499")
            }
        }
    }
}
