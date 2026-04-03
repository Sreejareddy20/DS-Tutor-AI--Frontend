package com.simats.dstutorai.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.simats.dstutorai.ui.theme.DeepNavy

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeleteAccountDialog(
    onDismiss: () -> Unit,
    onDeleteConfirm: () -> Unit
) {
    var deleteText by remember { mutableStateOf("") }
    var isChecked by remember { mutableStateOf(false) }
    val isDeleteEnabled = deleteText.equals("DELETE", ignoreCase = false) && isChecked

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth()
            ) {
                // Header
                Box(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Delete Account",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Red,
                        modifier = Modifier.align(Alignment.Center)
                    )
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.align(Alignment.TopEnd).size(24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = Color.Gray,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "This action is permanent and cannot be undone. Please read the following carefully:",
                    fontSize = 13.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Warning Box
                Surface(
                    color = Color(0xFFFFEBEE).copy(alpha = 0.5f),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, Color(0xFFFFCDD2)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = "Warning:",
                            color = Color(0xFFB71C1C),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        WarningItem("All your chat history will be deleted")
                        WarningItem("Your learning progress will be lost")
                        WarningItem("Your account cannot be recovered")
                        WarningItem("You will be logged out immediately")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Type \"DELETE\" to confirm",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = DeepNavy
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = deleteText,
                    onValueChange = { deleteText = it },
                    placeholder = { Text("DELETE", color = Color.LightGray) },
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFFF8F9FA),
                        unfocusedContainerColor = Color(0xFFF8F9FA),
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    shape = RoundedCornerShape(8.dp),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Checkbox(
                        checked = isChecked,
                        onCheckedChange = { isChecked = it },
                        colors = CheckboxDefaults.colors(checkedColor = Color(0xFF00BFA5))
                    )
                    Text(
                        text = "I understand that this action is permanent and all my data will be deleted",
                        fontSize = 12.sp,
                        color = Color.Gray,
                        lineHeight = 16.sp
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = onDeleteConfirm,
                    enabled = isDeleteEnabled,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Red,
                        disabledContainerColor = Color.Red.copy(alpha = 0.4f)
                    )
                ) {
                    Text("Delete Account", fontWeight = FontWeight.Bold, color = Color.White)
                }

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, Color(0xFFE0E0E0))
                ) {
                    Text("Cancel", fontWeight = FontWeight.Bold, color = DeepNavy)
                }
            }
        }
    }
}

@Composable
fun WarningItem(text: String) {
    Row(modifier = Modifier.padding(vertical = 2.dp)) {
        Text("• ", color = Color(0xFFB71C1C), fontSize = 13.sp)
        Text(text, color = Color(0xFFB71C1C), fontSize = 13.sp)
    }
}
