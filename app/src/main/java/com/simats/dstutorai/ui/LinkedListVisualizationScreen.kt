package com.simats.dstutorai.ui

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
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
fun LinkedListVisualizationScreen(
    onBackClick: () -> Unit,
    onChatClick: () -> Unit,
    onTopicsClick: () -> Unit,
    onPracticeClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    val linkedListElements = remember { mutableStateListOf(5, 7, 6) }
    var inputValue by remember { mutableStateOf("") }
    var posValue by remember { mutableStateOf("") }
    var statusMessage by remember { mutableStateOf("Linked List reset to initial state") }
    var isPlaying by remember { mutableStateOf(false) }
    var animationSpeed by remember { mutableFloatStateOf(1f) }
    val scope = rememberCoroutineScope()
    val listState = rememberLazyListState()

    LaunchedEffect(isPlaying) {
        while (isPlaying) {
            if (linkedListElements.size < 5) {
                val newVal = (1..20).random()
                linkedListElements.add(newVal)
                statusMessage = "Inserted $newVal at the end!"
            } else {
                val removed = linkedListElements.removeAt(0)
                statusMessage = "Deleted node with value $removed!"
            }
            delay((2000 / animationSpeed).toLong())
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Linked List Visualization",
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
                                2 -> { /* Already here */ }
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
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        LazyRow(
                            state = listState,
                            contentPadding = PaddingValues(horizontal = 16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(bottom = 16.dp)
                        ) {
                            itemsIndexed(linkedListElements) { index, value ->
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    NodeElement(value = value, isHead = index == 0)
                                    if (index < linkedListElements.size - 1) {
                                        Icon(
                                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                            contentDescription = null,
                                            tint = Color(0xFF14B8A6),
                                            modifier = Modifier.size(24.dp).padding(horizontal = 4.dp)
                                        )
                                    }
                                }
                            }
                        }
                        
                        // Legend
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(modifier = Modifier.size(10.dp).clip(CircleShape).background(TealGreen))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Head Node", color = Color.Gray, fontSize = 14.sp)
                            }
                            Spacer(modifier = Modifier.width(24.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                    contentDescription = null,
                                    tint = Color(0xFF14B8A6),
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Next Pointer", color = Color.Gray, fontSize = 14.sp)
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
                                modifier = Modifier.weight(1f).height(50.dp),
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
                                    if (linkedListElements.size < 5) {
                                        val newVal = (1..20).random()
                                        linkedListElements.add(newVal)
                                        statusMessage = "Step: Inserted $newVal"
                                    } else {
                                        val removed = linkedListElements.removeAt(0)
                                        statusMessage = "Step: Deleted $removed"
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
                                    linkedListElements.clear()
                                    linkedListElements.addAll(listOf(5, 7, 6))
                                    statusMessage = "Linked List reset to initial state"
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
                // Linked List Operations
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(16.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFF3F4F6))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Linked List Operations",
                            color = DeepNavy,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        Text(text = "Value to Insert", fontSize = 14.sp, color = Color.Gray)
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
                        
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(text = "Position (0 to ${linkedListElements.size})", fontSize = 14.sp, color = Color.Gray)
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = posValue,
                            onValueChange = { posValue = it },
                            placeholder = { Text("Enter position") },
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
                                            linkedListElements.add(value)
                                            statusMessage = "Inserted $value at the end!"
                                            inputValue = ""
                                        }
                                    }
                                },
                                modifier = Modifier.weight(1f).height(50.dp),
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = TealGreen)
                            ) {
                                Text("Insert at End", fontWeight = FontWeight.Bold)
                            }
                            Button(
                                onClick = {
                                    if (inputValue.isNotEmpty() && posValue.isNotEmpty()) {
                                        val value = inputValue.toIntOrNull()
                                        val pos = posValue.toIntOrNull()
                                        if (value != null && pos != null && pos >= 0 && pos <= linkedListElements.size) {
                                            linkedListElements.add(pos, value)
                                            statusMessage = "Inserted $value at position $pos!"
                                            inputValue = ""
                                            posValue = ""
                                        }
                                    }
                                },
                                modifier = Modifier.weight(1f).height(50.dp),
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3B82F6))
                            ) {
                                Text("Insert at Pos", fontWeight = FontWeight.Bold)
                            }
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Button(
                            onClick = {
                                if (posValue.isNotEmpty()) {
                                    val pos = posValue.toIntOrNull()
                                    if (pos != null && pos >= 0 && pos < linkedListElements.size) {
                                        val removed = linkedListElements.removeAt(pos)
                                        statusMessage = "Deleted node with value $removed!"
                                        posValue = ""
                                    } else {
                                        statusMessage = "Invalid position!"
                                    }
                                } else if (linkedListElements.isNotEmpty()) {
                                    val removed = linkedListElements.removeAt(linkedListElements.size - 1)
                                    statusMessage = "Deleted last node with value $removed!"
                                }
                            },
                            modifier = Modifier.fillMaxWidth().height(50.dp),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF3B30))
                        ) {
                            Text("Delete at Position", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            item {
                // How Linked List Works
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    color = Color(0xFFECFDF5),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFD1FAE5))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "How Linked List Works",
                            color = Color(0xFF065F46),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "A Linked List consists of nodes where each node contains data and a pointer to the next node. Unlike arrays, linked lists don't require contiguous memory and can easily grow or shrink.",
                            color = Color(0xFF047857),
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
                        ComplexityRow("Insert at Head", "O(1)", Color(0xFFF0FDF4), Color(0xFF166534))
                        Spacer(modifier = Modifier.height(8.dp))
                        ComplexityRow("Insert at Position", "O(n)", Color(0xFFFFFBEB), Color(0xFF92400E))
                        Spacer(modifier = Modifier.height(8.dp))
                        ComplexityRow("Delete at Position", "O(n)", Color(0xFFFFFBEB), Color(0xFF92400E))
                        Spacer(modifier = Modifier.height(8.dp))
                        ComplexityRow("Search", "O(n)", Color(0xFFFFFBEB), Color(0xFF92400E))
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
fun NodeElement(value: Int, isHead: Boolean) {
    Box(
        modifier = Modifier
            .padding(4.dp)
            .border(
                width = if (isHead) 2.dp else 1.dp,
                color = if (isHead) TealGreen else Color(0xFFE5E7EB),
                shape = RoundedCornerShape(12.dp)
            )
            .padding(2.dp)
    ) {
        Row(
            modifier = Modifier
                .width(100.dp)
                .height(60.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(Color.White),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Data part
            Box(
                modifier = Modifier
                    .weight(1.2f)
                    .fillMaxHeight()
                    .background(TealGreen),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = value.toString(),
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            // Pointer part
            Box(
                modifier = Modifier
                    .weight(0.8f)
                    .fillMaxHeight()
                    .background(Color(0xFFF9FAFB)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    tint = Color(0xFF14B8A6),
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
fun ComplexityRow(label: String, complexity: String, bgColor: Color, textColor: Color) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        color = bgColor
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = label, color = textColor, fontSize = 15.sp, fontWeight = FontWeight.Medium)
            Text(text = complexity, color = textColor, fontSize = 15.sp, fontWeight = FontWeight.Bold)
        }
    }
}
