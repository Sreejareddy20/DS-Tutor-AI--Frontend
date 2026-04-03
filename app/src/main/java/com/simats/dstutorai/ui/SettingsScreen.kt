package com.simats.dstutorai.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.simats.dstutorai.ui.theme.DeepNavy
import com.simats.dstutorai.ui.theme.FooterText
import com.simats.dstutorai.ui.theme.SubtextContent
import com.simats.dstutorai.ui.theme.TealGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    notificationsEnabled: Boolean,
    onNotificationsChange: (Boolean) -> Unit,
    onBackClick: () -> Unit,
    onChatHistoryClick: () -> Unit,
    onClearChatHistoryClick: () -> Unit,
    onLogoutClick: () -> Unit,
    onAccountDeleteClick: () -> Unit
) {
    var showClearDialog by remember { mutableStateOf(false) }
    var showLogoutDialog by remember { mutableStateOf(false) }
    var showDeleteAccountDialog by remember { mutableStateOf(false) }
    var showSuccess by remember { mutableStateOf(false) }
    var successMessage by remember { mutableStateOf("") }

    // Auto-hide success message after 3 seconds
    LaunchedEffect(showSuccess) {
        if (showSuccess) {
            kotlinx.coroutines.delay(3000)
            showSuccess = false
        }
    }

    // 1. DELETE ACCOUNT DIALOG
    if (showDeleteAccountDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteAccountDialog = false },
            title = { Text("Delete Account?", fontWeight = FontWeight.Bold, color = Color.Red) },
            text = { Text("This is permanent. All your data, profile, and chat history will be deleted forever.") },
            confirmButton = {
                Button(
                    onClick = {
                        showDeleteAccountDialog = false
                        onAccountDeleteClick()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("YES, Delete Forever", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteAccountDialog = false }) {
                    Text("Cancel", color = Color.Gray)
                }
            },
            containerColor = Color.White,
            shape = RoundedCornerShape(16.dp)
        )
    }

    // 2. LOGOUT DIALOG
    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("Confirm Logout", fontWeight = FontWeight.Bold) },
            text = { Text("Are you sure you want to logout?") },
            confirmButton = {
                Button(
                    onClick = {
                        showLogoutDialog = false
                        onLogoutClick()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = TealGreen),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("YES, Logout", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text("Cancel", color = Color.Gray)
                }
            },
            containerColor = Color.White,
            shape = RoundedCornerShape(16.dp)
        )
    }

    // 3. CLEAR HISTORY DIALOG
    if (showClearDialog) {
        AlertDialog(
            onDismissRequest = { showClearDialog = false },
            title = { Text("Clear Chat History", fontWeight = FontWeight.Bold) },
            text = { Text("This will permanently delete all your conversations. This action cannot be undone.") },
            confirmButton = {
                Button(
                    onClick = {
                        onClearChatHistoryClick()
                        showClearDialog = false
                        successMessage = "History cleared successfully!"
                        showSuccess = true
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Clear All", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showClearDialog = false }) {
                    Text("Cancel", color = Color.Gray)
                }
            },
            containerColor = Color.White,
            shape = RoundedCornerShape(16.dp)
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Settings",
                        color = DeepNavy,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = DeepNavy
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        containerColor = Color.White,
        bottomBar = {
            if (showSuccess) {
                Box(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Surface(
                        color = Color(0xFF4CAF50),
                        shape = RoundedCornerShape(8.dp),
                        tonalElevation = 4.dp
                    ) {
                        Text(
                            text = successMessage,
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp),
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            SettingsSectionTitle("PREFERENCES")
            SettingsItem(
                icon = Icons.Outlined.Notifications,
                title = "Notifications",
                subtitle = "Enable push notifications",
                trailing = {
                    Switch(
                        checked = notificationsEnabled,
                        onCheckedChange = { onNotificationsChange(it) },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = TealGreen,
                            uncheckedThumbColor = Color.White,
                            uncheckedTrackColor = Color(0xFFE0E0E0),
                            uncheckedBorderColor = Color.Transparent
                        )
                    )
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            SettingsSectionTitle("DATA")
            SettingsItem(
                icon = Icons.Outlined.ChatBubbleOutline,
                title = "Chat History",
                subtitle = "Manage your conversations",
                onClick = onChatHistoryClick
            )
            SettingsItem(
                icon = Icons.Outlined.Delete,
                title = "Clear Chat History",
                subtitle = "Delete all conversations",
                titleColor = Color.Red,
                iconColor = Color.Red,
                containerColor = Color(0xFFFFEBEE).copy(alpha = 0.5f),
                onClick = { showClearDialog = true }
            )

            Spacer(modifier = Modifier.height(24.dp))

            SettingsSectionTitle("ACCOUNT")
            SettingsItem(
                icon = Icons.AutoMirrored.Filled.Logout,
                title = "Logout",
                subtitle = "Sign out of your account",
                titleColor = Color.Red,
                iconColor = Color.Red,
                containerColor = Color(0xFFFFEBEE).copy(alpha = 0.5f),
                onClick = { showLogoutDialog = true }
            )

            SettingsItem(
                icon = Icons.Outlined.PersonOff,
                title = "Delete Account",
                subtitle = "Permanently remove your profile",
                titleColor = Color.Red,
                iconColor = Color.Red,
                containerColor = Color(0xFFFFEBEE).copy(alpha = 0.8f),
                onClick = { showDeleteAccountDialog = true }
            )

            Spacer(modifier = Modifier.height(40.dp))

            Box(
                modifier = Modifier.fillMaxWidth().padding(vertical = 24.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Powered by SIMATS",
                    color = FooterText,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
fun SettingsSectionTitle(title: String) {
    Text(
        text = title,
        color = Color.Gray,
        fontSize = 13.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(bottom = 12.dp)
    )
}

@Composable
fun SettingsItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    titleColor: Color = DeepNavy,
    iconColor: Color = Color.Gray,
    containerColor: Color = Color(0xFFF8F9FA),
    trailing: @Composable (() -> Unit)? = null,
    onClick: () -> Unit = {}
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        color = containerColor
    ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    color = titleColor,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = subtitle,
                    color = SubtextContent,
                    fontSize = 13.sp
                )
            }
            if (trailing != null) {
                trailing()
            }
        }
    }
}
