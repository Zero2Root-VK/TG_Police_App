package com.tgp.mockexam.feature.admin

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tgp.mockexam.core.designsystem.theme.TgpSpacing
import com.tgp.mockexam.core.model.Paper
import com.tgp.mockexam.core.model.Question

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ApiKeyDialog(
    currentKey: String,
    onSave: (String) -> Unit,
    onDismiss: () -> Unit,
) {
    var key by remember(currentKey) { mutableStateOf(currentKey) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Set Admin API Key") },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = key,
                    onValueChange = { key = it },
                    label = { Text("Server API Key") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                )
                Spacer(Modifier.height(TgpSpacing.xs))
                Text(
                    text = "Enter Appwrite server key (X-Appwrite-Key) to enable writes.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        },
        confirmButton = { TextButton(onClick = { onSave(key) }) { Text("Save") } },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } },
    )
}
