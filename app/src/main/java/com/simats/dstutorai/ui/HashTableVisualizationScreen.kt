package com.simats.dstutorai.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.simats.dstutorai.ui.theme.DeepNavy
import com.simats.dstutorai.ui.theme.FooterText
import com.simats.dstutorai.ui.theme.TealGreen
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HashTableVisualizationScreen(
    onBackClick: () -> Unit,
    onChatClick: () -> Unit,
    onTopicsClick: () -> Unit,
    onPracticeClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    val tableSize = 8
    // Using a list to hold state for each slot
    var table by remember { mutableStateOf(List<Int?>(tableSize) { null }) }
    var inputValue by remember { mutableStateOf("") }
    var statusMessage by remember { mutableStateOf("Hash Table (Size $tableSize) ready. Using Linear Probing.") }
    var isPlaying by remember { mutableStateOf(false) }
    var animationSpeed by remember { mutableFloatStateOf(2f) }
    
    var activeIndex by remember { mutableStateOf<Int?>(null) }
    var successIndex by remember { mutableStateOf<Int?>(null) }
    var probedIndices by remember { mutableStateOf(setOf<Int>()) }

    val scope = rememberCoroutineScope()

    fun hashFunction(key: Int): Int = key % tableSize

    suspend fun insert(value: Int) {
        isPlaying = true
        probedIndices = emptySet()
        successIndex = null
        val initialHash = hashFunction(value)
        statusMessage = "Inserting $value: Initial Index = $value % $tableSize = $initialHash"
        delay((1000 / animationSpeed).toLong())
        
        var i = 0
        while (i < tableSize) {
            val index = (initialHash + i) % tableSize
            activeIndex = index
            probedIndices = probedIndices + index
            
            statusMessage = "Probing Index $index..."
            delay((800 / animationSpeed).toLong())
            
            if (table[index] == null || table[index] == -1) { 
                statusMessage = "Slot $index is empty. Inserting $value here."
                val newList = table.toMutableList()
                newList[index] = value
                table = newList
                successIndex = index
                delay((1000 / animationSpeed).toLong())
                break
            } else {
                statusMessage = "Slot $index occupied by ${table[index]}. Collision! Moving forward."
                delay((600 / animationSpeed).toLong())
            }
            i++
        }
        
        if (i == tableSize) {
            statusMessage = "Overflow! Table is completely full."
        }
        
        activeIndex = null
        isPlaying = false
    }

    suspend fun search(value: Int) {
        isPlaying = true
        probedIndices = emptySet()
        successIndex = null
        val initialHash = hashFunction(value)
        statusMessage = "Searching for $value: Start at $initialHash"
        delay((1000 / animationSpeed).toLong())
        
        var i = 0
        while (i < tableSize) {
            val index = (initialHash + i) % tableSize
            activeIndex = index
            probedIndices = probedIndices + index
            
            statusMessage = "Checking Index $index..."
            delay((800 / animationSpeed).toLong())
            
            if (table[index] == value) {
                statusMessage = "Match found! $value is at Index $index."
                successIndex = index
                delay((1000 / animationSpeed).toLong())
                break
            } else if (table[index] == null) {
                statusMessage = "Index $index is NULL. $value is not in the table."
                delay((1000 / animationSpeed).toLong())
                break
            } else {
                statusMessage = "Value ${table[index]} at Index $index doesn't match. Probing next."
                delay((600 / animationSpeed).toLong())
            }
            i++
        }
        
        if (successIndex == null && i == tableSize) {
            statusMessage = "$value not found after full scan."
        }
        
        activeIndex = null
        isPlaying = false
    }

    suspend fun delete(value: Int) {
        isPlaying = true
        probedIndices = emptySet()
        successIndex = null
        val initialHash = hashFunction(value)
        statusMessage = "Deleting $value: Start at $initialHash"
        delay((1000 / animationSpeed).toLong())
        
        var i = 0
        while (i < tableSize) {
            val index = (initialHash + i) % tableSize
            activeIndex = index
            probedIndices = probedIndices + index
            
            statusMessage = "Checking Index $index..."
            delay((800 / animationSpeed).toLong())
            
            if (table[index] == value) {
                statusMessage = "Found $value at $index. Marking as Deleted (Lazy Deletion)."
                val newList = table.toMutableList()
                newList[index] = -1 
                table = newList
                successIndex = index
                delay((1000 / animationSpeed).toLong())
                break
            } else if (table[index] == null) {
                statusMessage = "$value not found (reached NULL at $index)."
                delay((1000 / animationSpeed).toLong())
                break
            }
            i++
            delay((600 / animationSpeed).toLong())
        }
        
        activeIndex = null
        isPlaying = false
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Hash Table Visualization", fontWeight = FontWeight.Bold, color = DeepNavy) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = DeepNavy)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        bottomBar = {
            NavigationBar(containerColor = Color.White, tonalElevation = 8.dp) {
                val navItems = listOf(
                    "Chat" to Icons.Default.ChatBubbleOutline,
                    "Topics" to Icons.Default.MenuBook,
                    "Visualizer" to Icons.Default.Visibility,
                    "Practice" to Icons.Default.School,
                    "Profile" to Icons.Default.PersonOutline
                )
                navItems.forEachIndexed { index, item ->
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
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Spacer(modifier = Modifier.height(8.dp))
                // Main Visualization Grid
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp)
                        .clip(RoundedCornerShape(16.dp)),
                    color = Color.White,
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFF3F4F6))
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize().padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text("Hash Array (Linear Probing)", fontSize = 12.sp, color = Color.Gray, modifier = Modifier.padding(bottom = 12.dp))
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            table.forEachIndexed { index, value ->
                                val cellColor by animateColorAsState(
                                    targetValue = when {
                                        successIndex == index -> TealGreen
                                        activeIndex == index -> Color(0xFFFBBF24)
                                        probedIndices.contains(index) -> Color(0xFF3B82F6).copy(alpha = 0.15f)
                                        else -> Color(0xFFF3F4F6)
                                    }
                                )
                                
                                val borderColor by animateColorAsState(
                                    targetValue = when {
                                        successIndex == index -> TealGreen
                                        activeIndex == index -> Color(0xFFFBBF24)
                                        else -> Color(0xFFE5E7EB)
                                    }
                                )

                                Column(
                                    modifier = Modifier.weight(1f),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .aspectRatio(0.7f)
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(cellColor)
                                            .border(1.5.dp, borderColor, RoundedCornerShape(8.dp)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = when (value) {
                                                null -> ""
                                                -1 -> "DEL"
                                                else -> value.toString()
                                            },
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (successIndex == index || activeIndex == index) Color.White else DeepNavy
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "[$index]",
                                        fontSize = 10.sp,
                                        color = Color.Gray,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        }
                    }
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    BinarySearchLegendItem(Color(0xFFFBBF24), "Current")
                    BinarySearchLegendItem(Color(0xFF3B82F6).copy(alpha = 0.2f), "Probed")
                    BinarySearchLegendItem(TealGreen, "Success")
                }
            }

            item {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFFEFF6FF),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFDBEAFE))
                ) {
                    Text(
                        text = statusMessage,
                        modifier = Modifier.padding(14.dp),
                        color = Color(0xFF1D4ED8),
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Medium,
                        lineHeight = 20.sp
                    )
                }
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFF3F4F6))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Interactive Controls", fontWeight = FontWeight.Bold, color = DeepNavy, fontSize = 16.sp)
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        OutlinedTextField(
                            value = inputValue,
                            onValueChange = { if (it.length <= 3) inputValue = it },
                            placeholder = { Text("Enter value (0-999)") },
                            modifier = Modifier.fillMaxWidth(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = TealGreen,
                                unfocusedBorderColor = Color(0xFFE5E7EB)
                            )
                        )
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Button(
                                onClick = { 
                                    inputValue.toIntOrNull()?.let { 
                                        scope.launch { insert(it) }
                                        inputValue = ""
                                    }
                                },
                                modifier = Modifier.weight(1f).height(48.dp),
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = TealGreen),
                                enabled = !isPlaying
                            ) {
                                Text("Insert")
                            }

                            Button(
                                onClick = { 
                                    inputValue.toIntOrNull()?.let { 
                                        scope.launch { search(it) }
                                        inputValue = ""
                                    }
                                },
                                modifier = Modifier.weight(1f).height(48.dp),
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3B82F6)),
                                enabled = !isPlaying
                            ) {
                                Text("Search")
                            }

                            Button(
                                onClick = { 
                                    inputValue.toIntOrNull()?.let { 
                                        scope.launch { delete(it) }
                                        inputValue = ""
                                    }
                                },
                                modifier = Modifier.weight(1f).height(48.dp),
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF3B30)),
                                enabled = !isPlaying
                            ) {
                                Text("Delete")
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        Button(
                            onClick = { 
                                table = List(tableSize) { null }
                                statusMessage = "Table cleared"
                                probedIndices = emptySet()
                                successIndex = null
                                activeIndex = null
                            },
                            modifier = Modifier.fillMaxWidth().height(48.dp),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF3F4F6), contentColor = DeepNavy),
                            enabled = !isPlaying
                        ) {
                            Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(Modifier.width(8.dp))
                            Text("Reset Table")
                        }
                        
                        Spacer(modifier = Modifier.height(20.dp))
                        Text("Simulation Speed: ${animationSpeed.toInt()}x", fontSize = 14.sp, color = DeepNavy, fontWeight = FontWeight.Medium)
                        Slider(
                            value = animationSpeed,
                            onValueChange = { animationSpeed = it },
                            valueRange = 1f..5f,
                            steps = 3,
                            colors = SliderDefaults.colors(
                                thumbColor = Color.White,
                                activeTrackColor = Color(0xFF3B82F6),
                                inactiveTrackColor = Color(0xFFE5E7EB)
                            ),
                            modifier = Modifier.padding(horizontal = 4.dp)
                        )
                    }
                }
            }

            item {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    color = Color(0xFFECFDF5),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFD1FAE5))
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Text(
                            text = "Hashing Concept",
                            color = Color(0xFF065F46),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Linear Probing is an open addressing technique. When a collision occurs, the algorithm searches for the next available slot in the array sequentially.",
                            color = Color(0xFF047857),
                            fontSize = 14.sp,
                            lineHeight = 22.sp
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text("Key Logic:", fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = Color(0xFF065F46))
                        val bulletPoints = listOf(
                            "Hash Index = Value % Table Size",
                            "On collision: New Index = (Initial + Offset) % Size",
                            "Lazy Deletion: Slots are marked 'DEL' so the probe chain isn't broken."
                        )
                        bulletPoints.forEach { point ->
                            Row(modifier = Modifier.padding(vertical = 4.dp)) {
                                Text("•", color = Color(0xFF14B8A6), fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(point, fontSize = 13.sp, color = Color(0xFF047857))
                            }
                        }
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
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}
