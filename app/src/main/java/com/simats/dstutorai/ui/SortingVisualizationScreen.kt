package com.simats.dstutorai.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.simats.dstutorai.ui.theme.DeepNavy
import com.simats.dstutorai.ui.theme.TealGreen
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SortingVisualizationScreen(
    onBackClick: () -> Unit,
    onChatClick: () -> Unit,
    onTopicsClick: () -> Unit,
    onPracticeClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    var array by remember { mutableStateOf(listOf(50, 30, 80, 10, 60, 40, 90, 20)) }
    var sortingIndices by remember { mutableStateOf(setOf<Int>()) }
    var currentPivotIndex by remember { mutableStateOf<Int?>(null) }
    var sortedIndices by remember { mutableStateOf(setOf<Int>()) }
    
    var isPlaying by remember { mutableStateOf(false) }
    var isSortingActive by remember { mutableStateOf(false) }
    var animationSpeed by remember { mutableFloatStateOf(1f) }
    var algorithmType by remember { mutableStateOf("Bubble Sort") }
    var statusMessage by remember { mutableStateOf("Select algorithm and press Play") }

    val scope = rememberCoroutineScope()

    fun resetArray() {
        array = listOf(50, 30, 80, 10, 60, 40, 90, 20)
        sortingIndices = emptySet()
        currentPivotIndex = null
        sortedIndices = emptySet()
        isPlaying = false
        isSortingActive = false
        statusMessage = "Array reset"
    }

    suspend fun waitIfPaused() {
        while (!isPlaying && isSortingActive) {
            delay(200)
        }
    }

    suspend fun bubbleSort() {
        isSortingActive = true
        val n = array.size
        val arr = array.toMutableList()
        statusMessage = "Starting Bubble Sort"
        
        for (i in 0 until n - 1) {
            for (j in 0 until n - i - 1) {
                if (!isSortingActive) return
                waitIfPaused()
                
                sortingIndices = setOf(j, j + 1)
                statusMessage = "Comparing ${arr[j]} and ${arr[j+1]}"
                delay((1000 / animationSpeed).toLong())
                
                if (arr[j] > arr[j + 1]) {
                    statusMessage = "Swapping ${arr[j]} and ${arr[j+1]}"
                    val temp = arr[j]
                    arr[j] = arr[j + 1]
                    arr[j + 1] = temp
                    array = arr.toList()
                    delay((800 / animationSpeed).toLong())
                }
            }
            sortedIndices = sortedIndices + (n - i - 1)
        }
        sortedIndices = sortedIndices + 0
        sortingIndices = emptySet()
        statusMessage = "Bubble Sort complete"
        isPlaying = false
        isSortingActive = false
    }

    suspend fun selectionSort() {
        isSortingActive = true
        val n = array.size
        val arr = array.toMutableList()
        statusMessage = "Starting Selection Sort"

        for (i in 0 until n - 1) {
            var minIdx = i
            currentPivotIndex = i
            for (j in i + 1 until n) {
                if (!isSortingActive) return
                waitIfPaused()
                
                sortingIndices = setOf(j, minIdx)
                statusMessage = "Searching min: ${arr[j]} vs current min ${arr[minIdx]}"
                delay((800 / animationSpeed).toLong())
                
                if (arr[j] < arr[minIdx]) {
                    minIdx = j
                }
            }
            statusMessage = "Found min ${arr[minIdx]}. Swapping with ${arr[i]}"
            val temp = arr[minIdx]
            arr[minIdx] = arr[i]
            arr[i] = temp
            array = arr.toList()
            sortedIndices = sortedIndices + i
            delay((1000 / animationSpeed).toLong())
        }
        sortedIndices = sortedIndices + (n - 1)
        sortingIndices = emptySet()
        currentPivotIndex = null
        statusMessage = "Selection Sort complete"
        isPlaying = false
        isSortingActive = false
    }

    suspend fun insertionSort() {
        isSortingActive = true
        val n = array.size
        val arr = array.toMutableList()
        statusMessage = "Starting Insertion Sort"
        sortedIndices = setOf(0)

        for (i in 1 until n) {
            val key = arr[i]
            var j = i - 1
            currentPivotIndex = i
            statusMessage = "Picking element $key"
            delay((1000 / animationSpeed).toLong())

            while (j >= 0 && arr[j] > key) {
                if (!isSortingActive) return
                waitIfPaused()
                
                sortingIndices = setOf(j, j + 1)
                statusMessage = "Shifting ${arr[j]} to the right"
                arr[j + 1] = arr[j]
                j--
                array = arr.toList()
                delay((800 / animationSpeed).toLong())
            }
            arr[j + 1] = key
            array = arr.toList()
            sortedIndices = sortedIndices + (0..i).toSet()
            statusMessage = "Inserted $key at position ${j + 1}"
            delay((800 / animationSpeed).toLong())
        }
        sortingIndices = emptySet()
        currentPivotIndex = null
        statusMessage = "Insertion Sort complete"
        isPlaying = false
        isSortingActive = false
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Sorting Visualizer", fontWeight = FontWeight.Bold, color = DeepNavy) },
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
                    "Topics" to Icons.AutoMirrored.Filled.MenuBook,
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
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(8.dp))
                Card(
                    modifier = Modifier.fillMaxWidth().height(250.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Box(modifier = Modifier.fillMaxSize().padding(16.dp), contentAlignment = Alignment.BottomCenter) {
                        Row(
                            modifier = Modifier.fillMaxSize(),
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalAlignment = Alignment.Bottom
                        ) {
                            array.forEachIndexed { index, value ->
                                val isComparing = sortingIndices.contains(index)
                                val isSorted = sortedIndices.contains(index)
                                val isPivot = currentPivotIndex == index
                                
                                val barColor by animateColorAsState(
                                    targetValue = when {
                                        isComparing -> Color(0xFFFBBF24) // Yellow
                                        isPivot -> Color(0xFF3B82F6) // Blue
                                        isSorted -> TealGreen // Teal
                                        else -> Color(0xFFE5E7EB) // Light Gray
                                    },
                                    animationSpec = tween(300)
                                )
                                
                                val barHeight by animateDpAsState(
                                    targetValue = (value * 1.8).dp,
                                    animationSpec = tween(300)
                                )

                                Column(
                                    modifier = Modifier.weight(1f),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(barHeight)
                                            .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
                                            .background(barColor)
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(value.toString(), fontSize = 10.sp, fontWeight = FontWeight.Bold, color = DeepNavy)
                                }
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
                    SortingLegendItem(Color(0xFFFBBF24), "Comparing")
                    Spacer(Modifier.width(16.dp))
                    SortingLegendItem(TealGreen, "Sorted")
                    Spacer(Modifier.width(16.dp))
                    SortingLegendItem(Color(0xFFE5E7EB), "Unsorted")
                }
            }

            item {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = Color(0xFFEFF6FF),
                    shape = RoundedCornerShape(8.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFDBEAFE))
                ) {
                    Text(
                        text = statusMessage,
                        modifier = Modifier.padding(12.dp),
                        color = Color(0xFF2563EB),
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Algorithm Selection", fontWeight = FontWeight.Bold, color = DeepNavy)
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            listOf("Bubble", "Selection", "Insertion").forEach { name ->
                                val type = "$name Sort"
                                FilterChip(
                                    selected = algorithmType == type,
                                    onClick = { 
                                        algorithmType = type
                                        resetArray()
                                    },
                                    label = { Text(name) },
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }
                }
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Animation Controls", fontWeight = FontWeight.Bold, color = DeepNavy)
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            Button(
                                onClick = { 
                                    if (!isSortingActive) {
                                        isPlaying = true
                                        scope.launch {
                                            when (algorithmType) {
                                                "Bubble Sort" -> bubbleSort()
                                                "Selection Sort" -> selectionSort()
                                                "Insertion Sort" -> insertionSort()
                                            }
                                        }
                                    } else {
                                        isPlaying = !isPlaying
                                    }
                                },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF10B981)),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Icon(if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow, null)
                                Spacer(Modifier.width(4.dp))
                                Text(if (isPlaying) "Pause" else "Play")
                            }
                            Button(
                                onClick = { resetArray() },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE5E7EB), contentColor = DeepNavy),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Icon(Icons.Default.Refresh, null)
                                Spacer(Modifier.width(4.dp))
                                Text("Reset")
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Animation Speed: ${animationSpeed.toInt()}x", fontSize = 13.sp, color = Color.Gray)
                        Slider(
                            value = animationSpeed,
                            onValueChange = { animationSpeed = it },
                            valueRange = 1f..5f,
                            colors = SliderDefaults.colors(thumbColor = TealGreen, activeTrackColor = TealGreen)
                        )
                    }
                }
            }

            item {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = Color(0xFFFFFBEB),
                    shape = RoundedCornerShape(16.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFEF3C7))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(algorithmType, fontWeight = FontWeight.Bold, color = Color(0xFF92400E), fontSize = 16.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = when (algorithmType) {
                                "Bubble Sort" -> "Bubble Sort repeatedly steps through the list, compares adjacent elements and swaps them if they are in the wrong order."
                                "Selection Sort" -> "Selection Sort repeatedly finds the minimum element from the unsorted part and puts it at the beginning."
                                "Insertion Sort" -> "Insertion Sort builds the final sorted array one item at a time. It is much less efficient on large lists than more advanced algorithms."
                                else -> ""
                            },
                            fontSize = 14.sp,
                            color = Color(0xFFB45309),
                            lineHeight = 20.sp
                        )
                    }
                }
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Time Complexity", fontWeight = FontWeight.Bold, color = DeepNavy, fontSize = 16.sp)
                        Spacer(modifier = Modifier.height(12.dp))
                        SortingComplexityRow("Best Case", if (algorithmType == "Bubble Sort") "O(n)" else if (algorithmType == "Insertion Sort") "O(n)" else "O(n²)")
                        Spacer(modifier = Modifier.height(8.dp))
                        SortingComplexityRow("Average Case", "O(n²)")
                        Spacer(modifier = Modifier.height(8.dp))
                        SortingComplexityRow("Worst Case", "O(n²)")
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun SortingLegendItem(color: Color, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(modifier = Modifier.size(12.dp).clip(CircleShape).background(color))
        Spacer(Modifier.width(8.dp))
        Text(label, fontSize = 13.sp, color = Color.Gray, fontWeight = FontWeight.Medium)
    }
}

@Composable
fun SortingComplexityRow(label: String, complexity: String) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        color = Color(0xFFF0FDF4)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(label, color = Color(0xFF166534), fontSize = 14.sp, fontWeight = FontWeight.Medium)
            Text(complexity, color = Color(0xFF166534), fontSize = 14.sp, fontWeight = FontWeight.Bold)
        }
    }
}
