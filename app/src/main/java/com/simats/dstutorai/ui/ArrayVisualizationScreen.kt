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
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArrayVisualizationScreen(
    onBackClick: () -> Unit,
    onChatClick: () -> Unit,
    onTopicsClick: () -> Unit,
    onPracticeClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    val arrayElements = remember { mutableStateListOf(10, 20, 30, 40, 50) }
    var inputValue by remember { mutableStateOf("") }
    var indexValue by remember { mutableStateOf("") }
    var statusMessage by remember { mutableStateOf("Array initialized with 5 elements") }
    var isPlaying by remember { mutableStateOf(false) }
    var animationSpeed by remember { mutableFloatStateOf(1f) }

    LaunchedEffect(isPlaying) {
        if (isPlaying) {
            while (isPlaying) {
                if (arrayElements.isNotEmpty()) {
                    val randomIndex = Random.nextInt(arrayElements.size)
                    val newValue = Random.nextInt(1, 100)
                    arrayElements[randomIndex] = newValue
                    statusMessage = "Auto Update: Index $randomIndex set to $newValue"
                }
                delay((2000 / animationSpeed).toLong())
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Array Visualization",
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
                        .height(200.dp)
                        .clip(RoundedCornerShape(16.dp)),
                    color = Color.White,
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFF3F4F6))
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            modifier = Modifier.padding(8.dp)
                        ) {
                            arrayElements.forEachIndexed { index, value ->
                                ArrayElement(index = index, value = value)
                            }
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
                                    arrayElements.clear()
                                    arrayElements.addAll(listOf(10, 20, 30, 40, 50))
                                    statusMessage = "Array reset to initial state"
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
                    }
                }
            }

            item {
                // Array Operations
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(16.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFF3F4F6))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Array Operations",
                            color = DeepNavy,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedTextField(
                                value = indexValue,
                                onValueChange = { indexValue = it },
                                label = { Text("Index") },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                            )
                            OutlinedTextField(
                                value = inputValue,
                                onValueChange = { inputValue = it },
                                label = { Text("Value") },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                            )
                        }
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Button(
                                onClick = {
                                    val idx = indexValue.toIntOrNull()
                                    val value = inputValue.toIntOrNull()
                                    if (idx != null && value != null && idx >= 0 && idx < arrayElements.size) {
                                        arrayElements[idx] = value
                                        statusMessage = "Updated index $idx to $value"
                                        indexValue = ""
                                        inputValue = ""
                                    } else {
                                        statusMessage = "Invalid index or value!"
                                    }
                                },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = TealGreen)
                            ) {
                                Text("Update")
                            }
                            Button(
                                onClick = {
                                    val value = inputValue.toIntOrNull()
                                    if (value != null && arrayElements.size < 8) {
                                        arrayElements.add(value)
                                        statusMessage = "Added $value to array"
                                        inputValue = ""
                                    } else if (arrayElements.size >= 8) {
                                        statusMessage = "Array full (max 8 for demo)"
                                    }
                                },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3B82F6))
                            ) {
                                Text("Add")
                            }
                            Button(
                                onClick = {
                                    val idx = indexValue.toIntOrNull()
                                    if (idx != null && idx >= 0 && idx < arrayElements.size) {
                                        val removed = arrayElements.removeAt(idx)
                                        statusMessage = "Removed $removed from index $idx"
                                        indexValue = ""
                                    }
                                },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF3B30))
                            ) {
                                Text("Remove")
                            }
                        }
                    }
                }
            }

            item {
                // How Array Works
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    color = Color(0xFFF5F3FF),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFEDE9FE))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "How Array Works",
                            color = Color(0xFF5B21B6),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "An array is a collection of items stored at contiguous memory locations. The idea is to store multiple items of the same type together.",
                            color = Color(0xFF6D28D9),
                            fontSize = 14.sp,
                            lineHeight = 20.sp
                        )
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
fun ArrayElement(index: Int, value: Int) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Surface(
            modifier = Modifier
                .size(width = 50.dp, height = 60.dp)
                .border(1.dp, Color.LightGray, RoundedCornerShape(4.dp)),
            color = TealGreen,
            shape = RoundedCornerShape(4.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(
                    text = value.toString(),
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = "[$index]", fontSize = 12.sp, color = Color.Gray)
    }
}
