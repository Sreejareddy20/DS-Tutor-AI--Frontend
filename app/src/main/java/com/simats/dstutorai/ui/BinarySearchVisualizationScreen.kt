package com.simats.dstutorai.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
fun BinarySearchVisualizationScreen(
    onBackClick: () -> Unit,
    onChatClick: () -> Unit,
    onTopicsClick: () -> Unit,
    onPracticeClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    val array = remember { listOf(10, 20, 30, 40, 50, 60, 70, 80, 90, 100) }
    var target by remember { mutableIntStateOf(70) }
    
    var low by remember { mutableIntStateOf(0) }
    var high by remember { mutableIntStateOf(array.size - 1) }
    var mid by remember { mutableStateOf<Int?>(null) }
    
    var isPlaying by remember { mutableStateOf(false) }
    var isSearchingActive by remember { mutableStateOf(false) }
    var animationSpeed by remember { mutableFloatStateOf(1f) }
    var statusMessage by remember { mutableStateOf("Ready to search. Target is $target") }
    var foundIndex by remember { mutableStateOf<Int?>(null) }

    val scope = rememberCoroutineScope()

    fun resetSearch() {
        low = 0
        high = array.size - 1
        mid = null
        foundIndex = null
        isPlaying = false
        isSearchingActive = false
        statusMessage = "Search reset. Target is $target"
    }

    suspend fun waitIfPaused() {
        while (!isPlaying && isSearchingActive) {
            delay(200)
        }
    }

    suspend fun runBinarySearch() {
        isSearchingActive = true
        statusMessage = "Starting binary search for $target"
        
        var l = 0
        var r = array.size - 1
        
        while (l <= r && isSearchingActive) {
            waitIfPaused()
            if (!isSearchingActive) break
            
            low = l
            high = r
            val m = l + (r - l) / 2
            mid = m
            
            statusMessage = "Step: Low=$l, High=$r. Mid is index $m (value ${array[m]})"
            delay((1500 / animationSpeed).toLong())
            
            waitIfPaused()
            if (array[m] == target) {
                statusMessage = "Found $target at index $m!"
                foundIndex = m
                isSearchingActive = false
                isPlaying = false
                break
            } else if (array[m] < target) {
                statusMessage = "${array[m]} < $target. Searching in the right half."
                l = m + 1
            } else {
                statusMessage = "${array[m]} > $target. Searching in the left half."
                r = m - 1
            }
            delay((1000 / animationSpeed).toLong())
        }
        
        if (foundIndex == null && !isSearchingActive && l > r) {
            statusMessage = "Target $target not found in the array."
            isPlaying = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Binary Search Visualization", fontWeight = FontWeight.Bold, color = DeepNavy) },
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
                // Visualization Box
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(16.dp)),
                    color = Color.White,
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFF3F4F6))
                ) {
                    Box(modifier = Modifier.fillMaxSize().padding(12.dp), contentAlignment = Alignment.Center) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            array.forEachIndexed { index, value ->
                                val isMid = mid == index
                                val isRange = index in low..high
                                val isFound = foundIndex == index
                                
                                val cellColor by animateColorAsState(
                                    targetValue = when {
                                        isFound -> TealGreen
                                        isMid -> Color(0xFFFBBF24)
                                        isRange -> Color(0xFF3B82F6).copy(alpha = 0.2f)
                                        else -> Color(0xFFE5E7EB)
                                    }
                                )
                                
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .aspectRatio(0.8f)
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(cellColor)
                                        .border(
                                            width = if (isMid || isFound) 2.dp else 1.dp,
                                            color = if (isMid) Color(0xFFFBBF24) else if (isFound) TealGreen else Color.Transparent,
                                            shape = RoundedCornerShape(4.dp)
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text(
                                            text = value.toString(),
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (isFound || isMid) Color.White else DeepNavy
                                        )
                                        Text(
                                            text = index.toString(),
                                            fontSize = 8.sp,
                                            color = if (isFound || isMid) Color.White.copy(alpha = 0.7f) else Color.Gray
                                        )
                                    }
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
                    BinarySearchLegendItem(Color(0xFFFBBF24), "Mid")
                    Spacer(Modifier.width(12.dp))
                    BinarySearchLegendItem(Color(0xFF3B82F6).copy(alpha = 0.2f), "Range")
                    Spacer(Modifier.width(12.dp))
                    BinarySearchLegendItem(TealGreen, "Found")
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
                        Text("Animation Controls", fontWeight = FontWeight.Bold, color = DeepNavy, fontSize = 16.sp)
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            Button(
                                onClick = { 
                                    if (!isSearchingActive) {
                                        isPlaying = true
                                        scope.launch { runBinarySearch() }
                                    } else {
                                        isPlaying = !isPlaying
                                    }
                                },
                                modifier = Modifier.weight(1f).height(50.dp),
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = TealGreen)
                            ) {
                                Icon(
                                    if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                    contentDescription = null
                                )
                                Spacer(Modifier.width(8.dp))
                                Text(if (!isSearchingActive) "Start" else if (isPlaying) "Pause" else "Resume")
                            }

                            Button(
                                onClick = { resetSearch() },
                                modifier = Modifier.weight(1f).height(50.dp),
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE5E7EB), contentColor = DeepNavy)
                            ) {
                                Icon(Icons.Default.Refresh, contentDescription = null)
                                Spacer(Modifier.width(8.dp))
                                Text("Reset")
                            }
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
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Slow", fontSize = 12.sp, color = Color.Gray)
                            Text("Fast", fontSize = 12.sp, color = Color.Gray)
                        }
                    }
                }
            }
            
            item {
                // Search Target
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFF3F4F6))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Search Target", fontWeight = FontWeight.Bold, color = DeepNavy, fontSize = 16.sp)
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            listOf(20, 45, 70, 90).forEach { value ->
                                FilterChip(
                                    selected = target == value,
                                    onClick = { 
                                        target = value
                                        resetSearch()
                                    },
                                    label = { Text(value.toString()) },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = TealGreen.copy(alpha = 0.1f),
                                        selectedLabelColor = TealGreen
                                    )
                                )
                            }
                        }
                    }
                }
            }

            item {
                // How Binary Search Works
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    color = Color(0xFFECFDF5),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFD1FAE5))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "How Binary Search Works",
                            color = Color(0xFF065F46),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Binary search is an efficient algorithm for finding an item from a sorted list of items. It works by repeatedly dividing in half the portion of the list that could contain the item, until you've narrowed down the possible locations to just one.",
                            color = Color(0xFF047857),
                            fontSize = 14.sp,
                            lineHeight = 20.sp
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text("Steps:", fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = Color(0xFF065F46))
                        val steps = listOf(
                            "1. Compare the target value with the middle element of the array.",
                            "2. If the target matches the middle element, the search is complete.",
                            "3. If the target is less than the middle, continue search in the left half.",
                            "4. If the target is greater than the middle, continue search in the right half.",
                            "5. Repeat until the target is found or the search space is empty."
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
                                text = "Time Complexity: O(log n)\nSpace Complexity: O(1)",
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

@Composable
fun BinarySearchLegendItem(color: Color, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(12.dp)
                .clip(CircleShape)
                .background(color)
        )
        Spacer(Modifier.width(4.dp))
        Text(label, fontSize = 12.sp, color = Color.Gray)
    }
}
