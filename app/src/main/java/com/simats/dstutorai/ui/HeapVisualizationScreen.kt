package com.simats.dstutorai.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
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
fun HeapVisualizationScreen(
    onBackClick: () -> Unit,
    onChatClick: () -> Unit,
    onTopicsClick: () -> Unit,
    onPracticeClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    var heap by remember { mutableStateOf(mutableStateListOf(90, 80, 70, 60, 50, 40, 30)) }
    var inputValue by remember { mutableStateOf("") }
    var statusMessage by remember { mutableStateOf("Max Heap initialized") }
    var isPlaying by remember { mutableStateOf(false) }
    var animationSpeed by remember { mutableFloatStateOf(1f) }
    var activeIndices by remember { mutableStateOf(setOf<Int>()) }
    var comparingIndices by remember { mutableStateOf(setOf<Int>()) }
    var swappingIndices by remember { mutableStateOf(setOf<Int>()) }

    val scope = rememberCoroutineScope()

    suspend fun waitIfPaused() {
        while (!isPlaying) {
            delay(200)
        }
    }

    suspend fun heapifyUp(index: Int) {
        var curr = index
        while (curr > 0) {
            val parent = (curr - 1) / 2
            comparingIndices = setOf(curr, parent)
            statusMessage = "Comparing node ${heap[curr]} with parent ${heap[parent]}"
            delay((1000 / animationSpeed).toLong())
            
            if (heap[curr] > heap[parent]) {
                statusMessage = "${heap[curr]} > ${heap[parent]}, swapping..."
                swappingIndices = setOf(curr, parent)
                delay((800 / animationSpeed).toLong())
                
                val temp = heap[curr]
                heap[curr] = heap[parent]
                heap[parent] = temp
                
                swappingIndices = emptySet()
                curr = parent
            } else {
                statusMessage = "${heap[curr]} <= ${heap[parent]}, heap property satisfied"
                break
            }
        }
        comparingIndices = emptySet()
    }

    suspend fun heapifyDown(index: Int) {
        var curr = index
        while (true) {
            val left = 2 * curr + 1
            val right = 2 * curr + 2
            var largest = curr
            
            if (left < heap.size) {
                comparingIndices = setOf(curr, left)
                statusMessage = "Comparing ${heap[curr]} with left child ${heap[left]}"
                delay((1000 / animationSpeed).toLong())
                if (heap[left] > heap[largest]) {
                    largest = left
                }
            }
            
            if (right < heap.size) {
                comparingIndices = setOf(largest, right)
                statusMessage = "Comparing current largest with right child ${heap[right]}"
                delay((1000 / animationSpeed).toLong())
                if (heap[right] > heap[largest]) {
                    largest = right
                }
            }
            
            if (largest != curr) {
                statusMessage = "Swapping ${heap[curr]} with ${heap[largest]}"
                swappingIndices = setOf(curr, largest)
                delay((800 / animationSpeed).toLong())
                
                val temp = heap[curr]
                heap[curr] = heap[largest]
                heap[largest] = temp
                
                swappingIndices = emptySet()
                curr = largest
            } else {
                statusMessage = "Heap property satisfied at index $curr"
                break
            }
        }
        comparingIndices = emptySet()
    }

    fun insertHeap(value: Int) {
        scope.launch {
            isPlaying = true
            statusMessage = "Inserting $value at the end of the heap"
            heap.add(value)
            activeIndices = setOf(heap.size - 1)
            delay((1000 / animationSpeed).toLong())
            heapifyUp(heap.size - 1)
            activeIndices = emptySet()
            statusMessage = "Insertion of $value completed"
            isPlaying = false
        }
    }

    fun extractMax() {
        if (heap.isEmpty()) return
        scope.launch {
            isPlaying = true
            statusMessage = "Extracting Max value ${heap[0]}"
            swappingIndices = setOf(0, heap.size - 1)
            delay((1000 / animationSpeed).toLong())
            
            if (heap.size > 1) {
                heap[0] = heap.removeAt(heap.size - 1)
                swappingIndices = emptySet()
                statusMessage = "Moved last element to root. Heapifying down..."
                heapifyDown(0)
            } else {
                heap.removeAt(0)
                swappingIndices = emptySet()
            }
            statusMessage = "Extraction completed"
            isPlaying = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Heap Visualization", fontWeight = FontWeight.Bold, color = DeepNavy) },
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
                // Visualization Box (Array Representation)
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .clip(RoundedCornerShape(16.dp)),
                    color = Color.White,
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFF3F4F6))
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize().padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text("Array Representation", fontSize = 12.sp, color = Color.Gray, modifier = Modifier.padding(bottom = 8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            heap.forEachIndexed { index, value ->
                                val cellColor by animateColorAsState(
                                    targetValue = when {
                                        swappingIndices.contains(index) -> Color(0xFFFF3B30)
                                        comparingIndices.contains(index) -> Color(0xFFFBBF24)
                                        activeIndices.contains(index) -> TealGreen
                                        else -> Color(0xFFE5E7EB)
                                    }
                                )
                                
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .aspectRatio(1f)
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(cellColor),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text(
                                            text = value.toString(),
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (cellColor != Color(0xFFE5E7EB)) Color.White else DeepNavy
                                        )
                                        Text(
                                            text = index.toString(),
                                            fontSize = 8.sp,
                                            color = if (cellColor != Color(0xFFE5E7EB)) Color.White.copy(alpha = 0.7f) else Color.Gray
                                        )
                                    }
                                }
                            }
                            // Fill empty space if heap is small
                            repeat(maxOf(0, 10 - heap.size)) {
                                Box(modifier = Modifier.weight(1f).aspectRatio(1f))
                            }
                        }
                    }
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    BinarySearchLegendItem(Color(0xFFFBBF24), "Comparing")
                    Spacer(Modifier.width(12.dp))
                    BinarySearchLegendItem(Color(0xFFFF3B30), "Swapping")
                    Spacer(Modifier.width(12.dp))
                    BinarySearchLegendItem(TealGreen, "Active")
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
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            item {
                // Animation Controls
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFF3F4F6))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Heap Operations", fontWeight = FontWeight.Bold, color = DeepNavy, fontSize = 16.sp)
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        OutlinedTextField(
                            value = inputValue,
                            onValueChange = { inputValue = it },
                            placeholder = { Text("Enter value") },
                            modifier = Modifier.fillMaxWidth(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = TealGreen,
                                unfocusedBorderColor = Color(0xFFE5E7EB)
                            )
                        )
                        
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            Button(
                                onClick = { 
                                    if (inputValue.isNotEmpty()) {
                                        insertHeap(inputValue.toInt())
                                        inputValue = ""
                                    }
                                },
                                modifier = Modifier.weight(1f).height(50.dp),
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = TealGreen),
                                enabled = !isPlaying
                            ) {
                                Text("Insert")
                            }

                            Button(
                                onClick = { extractMax() },
                                modifier = Modifier.weight(1f).height(50.dp),
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3B82F6)),
                                enabled = !isPlaying && heap.isNotEmpty()
                            ) {
                                Text("Extract Max")
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        Button(
                            onClick = { 
                                heap.clear()
                                heap.addAll(listOf(90, 80, 70, 60, 50, 40, 30))
                                statusMessage = "Heap reset"
                            },
                            modifier = Modifier.fillMaxWidth().height(50.dp),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE5E7EB), contentColor = DeepNavy),
                            enabled = !isPlaying
                        ) {
                            Icon(Icons.Default.Refresh, contentDescription = null)
                            Spacer(Modifier.width(8.dp))
                            Text("Reset Heap")
                        }
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Animation Speed: ${animationSpeed.toInt()}x", fontSize = 14.sp, color = DeepNavy)
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
                // How Heap Works
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    color = Color(0xFFECFDF5),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFD1FAE5))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "How Binary Heap Works",
                            color = Color(0xFF065F46),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "A Binary Heap is a complete binary tree which satisfies the heap property. In a Max Heap, for any given node I, the value of I is greater than or equal to the values of its children.",
                            color = Color(0xFF047857),
                            fontSize = 14.sp,
                            lineHeight = 20.sp
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text("Operations:", fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = Color(0xFF065F46))
                        val steps = listOf(
                            "Insert: Add element at the end and 'Heapify Up' to restore property.",
                            "Extract Max: Replace root with last element and 'Heapify Down'.",
                            "Heapify Up: Swap child with parent if child > parent.",
                            "Heapify Down: Swap parent with largest child if child > parent."
                        )
                        steps.forEach { step ->
                            Row(modifier = Modifier.padding(vertical = 4.dp)) {
                                Text("•", color = Color(0xFF14B8A6), fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = step,
                                    fontSize = 13.sp,
                                    color = Color(0xFF047857)
                                )
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(12.dp))
                        Surface(
                            color = Color(0xFFD1FAE5),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = "Time Complexity:\nInsert: O(log n)\nExtract Max: O(log n)\nSpace Complexity: O(n)",
                                modifier = Modifier.padding(12.dp),
                                fontSize = 12.sp,
                                color = Color(0xFF065F46),
                                fontWeight = FontWeight.Medium
                            )
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
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}
