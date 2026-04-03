package com.simats.dstutorai.ui

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.simats.dstutorai.ui.theme.DeepNavy
import com.simats.dstutorai.ui.theme.FooterText
import com.simats.dstutorai.ui.theme.TealGreen
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StackVisualizationScreen(
    onBackClick: () -> Unit,
    onChatClick: () -> Unit,
    onTopicsClick: () -> Unit,
    onPracticeClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    val stackElements = remember { mutableStateListOf(5, 7, 10) }
    var inputValue by remember { mutableStateOf("") }
    var statusMessage by remember { mutableStateOf("Stack reset to initial state") }
    var isPlaying by remember { mutableStateOf(false) }
    var animationSpeed by remember { mutableFloatStateOf(1f) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(isPlaying) {
        while (isPlaying) {
            if (stackElements.size < 4) {
                val newVal = (1..20).random()
                stackElements.add(newVal)
                statusMessage = "Pushing $newVal successfully!"
            } else {
                val popped = stackElements.removeAt(stackElements.size - 1)
                statusMessage = "Popped $popped successfully!"
            }
            delay((2000 / animationSpeed).toLong())
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Stack Visualization",
                        color = DeepNavy,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = DeepNavy)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = Color.White,
                tonalElevation = 8.dp
            ) {
                val items = listOf(
                    "Chat" to Icons.Default.ChatBubbleOutline,
                    "Topics" to Icons.AutoMirrored.Filled.MenuBook,
                    "Visualizer" to Icons.Default.Visibility,
                    "Practice" to Icons.Default.School,
                    "Profile" to Icons.Default.PersonOutline
                )
                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        icon = { Icon(item.second, contentDescription = item.first) },
                        label = { Text(item.first, fontSize = 12.sp) },
                        selected = index == 2,
                        onClick = {
                            when (index) {
                                0 -> onChatClick()
                                1 -> onTopicsClick()
                                3 -> onPracticeClick()
                                4 -> onProfileClick()
                            }
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = TealGreen,
                            selectedTextColor = TealGreen,
                            unselectedIconColor = Color.Gray,
                            unselectedTextColor = Color.Gray,
                            indicatorColor = Color.Transparent
                        )
                    )
                }
            }
        },
        containerColor = Color(0xFFF9FAFB)
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(8.dp))
                // Visualization Box
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(240.dp)
                        .clip(RoundedCornerShape(16.dp)),
                    color = Color.White,
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFF3F4F6))
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Box(
                            modifier = Modifier.weight(1f),
                            contentAlignment = Alignment.BottomCenter
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier.padding(bottom = 16.dp)
                            ) {
                                // Reverse to show top at the top
                                stackElements.reversed().forEach { value ->
                                    StackElement(value = value)
                                }
                            }
                        }
                        
                        // Top indicator
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(bottom = 16.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(10.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF3B82F6))
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Top of Stack",
                                color = Color.Gray,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }

            item {
                // Status Message
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xFFEFF6FF),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFDBEAFE))
                ) {
                    Text(
                        text = statusMessage,
                        modifier = Modifier.padding(12.dp),
                        color = Color(0xFF2563EB),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            item {
                // Animation Controls
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(16.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFF3F4F6))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Animation Controls",
                            color = DeepNavy,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Button(
                                onClick = { 
                                    isPlaying = !isPlaying 
                                },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = TealGreen)
                            ) {
                                Icon(
                                    imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                    contentDescription = null
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(if (isPlaying) "Pause" else "Play")
                            }
                            Button(
                                onClick = { 
                                    if (stackElements.size < 4) {
                                        val newVal = (1..20).random()
                                        stackElements.add(newVal)
                                        statusMessage = "Step: Pushed $newVal"
                                    } else {
                                        val popped = stackElements.removeAt(stackElements.size - 1)
                                        statusMessage = "Step: Popped $popped"
                                    }
                                },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3B82F6))
                            ) {
                                Icon(imageVector = Icons.Default.SkipNext, contentDescription = null)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Step")
                            }
                            Button(
                                onClick = {
                                    stackElements.clear()
                                    stackElements.addAll(listOf(5, 7, 10))
                                    statusMessage = "Stack reset to initial state"
                                    isPlaying = false
                                },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE5E7EB), contentColor = DeepNavy)
                            ) {
                                Icon(imageVector = Icons.Default.Refresh, contentDescription = null)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Reset")
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Animation Speed: ${animationSpeed.toInt()}x",
                            color = DeepNavy,
                            fontSize = 14.sp
                        )
                        Slider(
                            value = animationSpeed,
                            onValueChange = { animationSpeed = it },
                            valueRange = 1f..5f,
                            steps = 3,
                            colors = SliderDefaults.colors(
                                thumbColor = Color(0xFFE5E7EB),
                                activeTrackColor = Color(0xFF3B82F6),
                                inactiveTrackColor = Color(0xFFE5E7EB)
                            )
                        )
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Slow", fontSize = 12.sp, color = Color.Gray)
                            Text("Fast", fontSize = 12.sp, color = Color.Gray)
                        }
                    }
                }
            }

            item {
                // Stack Operations
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(16.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFF3F4F6))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Stack Operations",
                            color = DeepNavy,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(text = "Value to Push", fontSize = 14.sp, color = Color.Gray)
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = inputValue,
                            onValueChange = { inputValue = it },
                            placeholder = { Text("Enter a number") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFFE5E7EB),
                                unfocusedBorderColor = Color(0xFFE5E7EB)
                            )
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Button(
                                onClick = {
                                    if (inputValue.isNotEmpty()) {
                                        val value = inputValue.toIntOrNull()
                                        if (value != null) {
                                            stackElements.add(value)
                                            statusMessage = "Pushed $value successfully!"
                                            inputValue = ""
                                        }
                                    }
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(50.dp),
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = TealGreen)
                            ) {
                                Text("Push", fontWeight = FontWeight.Bold)
                            }
                            Button(
                                onClick = {
                                    if (stackElements.isNotEmpty()) {
                                        val popped = stackElements.removeAt(stackElements.size - 1)
                                        statusMessage = "Popped $popped successfully!"
                                    } else {
                                        statusMessage = "Stack Underflow!"
                                    }
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(50.dp),
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF3B30))
                            ) {
                                Text("Pop", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            item {
                // How Stack Works
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    color = Color(0xFFF5F3FF),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFEDE9FE))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "How Stack Works",
                            color = Color(0xFF5B21B6),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Stack follows Last In First Out (LIFO). The last inserted element is removed first. Think of it like a stack of plates - you can only add or remove from the top.",
                            color = Color(0xFF6D28D9),
                            fontSize = 14.sp,
                            lineHeight = 20.sp
                        )
                    }
                }
            }

            item {
                // Time Complexity
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(16.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFF3F4F6))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Time Complexity",
                            color = DeepNavy,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        ComplexityRow("Push", "O(1)")
                        Spacer(modifier = Modifier.height(8.dp))
                        ComplexityRow("Pop", "O(1)")
                        Spacer(modifier = Modifier.height(8.dp))
                        ComplexityRow("Peek/Top", "O(1)")
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Powered by SIMATS",
                    color = FooterText,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun StackElement(value: Int) {
    Surface(
        modifier = Modifier
            .width(140.dp)
            .height(48.dp)
            .clip(RoundedCornerShape(8.dp)),
        color = TealGreen
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = value.toString(),
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun ComplexityRow(label: String, complexity: String) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        color = Color(0xFFF0FDF4)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = label, color = Color(0xFF166534), fontSize = 15.sp, fontWeight = FontWeight.Medium)
            Text(text = complexity, color = Color(0xFF15803D), fontSize = 15.sp, fontWeight = FontWeight.Bold)
        }
    }
}
