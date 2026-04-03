package com.simats.dstutorai.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.simats.dstutorai.ui.theme.DeepNavy
import com.simats.dstutorai.ui.theme.FooterText
import com.simats.dstutorai.ui.theme.SubtextContent
import com.simats.dstutorai.ui.theme.TealGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewChatScreen(
    onBackClick: () -> Unit,
    onSuggestionClick: (String) -> Unit
) {
    val suggestions = listOf(
        "Explain stack with example",
        "Write C program for queue",
        "Explain BFS algorithm",
        "Generate linked list quiz",
        "Time complexity of merge sort",
        "Binary search tree operations",
        "Implement doubly linked list in Python",
        "Graph traversal techniques"
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "New Chat",
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
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        },
        containerColor = Color.White
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            // AI/Sparkle Icon
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFE1F5FE)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = null,
                    modifier = Modifier.size(40.dp),
                    tint = TealGreen
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "What would you like to learn?",
                color = DeepNavy,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Choose a suggestion or ask your own question",
                color = SubtextContent,
                fontSize = 16.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Suggestions List
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(suggestions) { suggestion ->
                    SuggestionCard(suggestion) {
                        onSuggestionClick(suggestion)
                    }
                }
            }

            // Footer
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp),
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
fun SuggestionCard(text: String, onClick: () -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE0E0E0)),
        color = Color.White
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(16.dp),
            color = DeepNavy,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
        )
    }
}
