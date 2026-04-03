package com.simats.dstutorai.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Canvas
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.simats.dstutorai.ui.theme.DeepNavy
import com.simats.dstutorai.ui.theme.TealGreen
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class GraphNode(
    val id: Int,
    val label: String,
    val position: Offset
)

data class GraphEdge(
    val from: Int,
    val to: Int
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GraphVisualizationScreen(
    onBackClick: () -> Unit,
    onChatClick: () -> Unit,
    onTopicsClick: () -> Unit,
    onPracticeClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    val nodes = remember {
        listOf(
            GraphNode(0, "A", Offset(150f, 50f)),
            GraphNode(1, "B", Offset(80f, 130f)),
            GraphNode(2, "C", Offset(220f, 130f)),
            GraphNode(3, "D", Offset(80f, 210f)),
            GraphNode(4, "E", Offset(150f, 210f)),
            GraphNode(5, "F", Offset(220f, 210f))
        )
    }

    val edges = remember {
        listOf(
            GraphEdge(0, 1), GraphEdge(0, 2),
            GraphEdge(1, 3), GraphEdge(1, 4),
            GraphEdge(2, 4), GraphEdge(2, 5),
            GraphEdge(3, 4), GraphEdge(4, 5)
        )
    }

    var visitedNodes by remember { mutableStateOf(setOf<Int>()) }
    var currentNodeId by remember { mutableStateOf<Int?>(null) }
    var queueOrStack by remember { mutableStateOf(listOf<Int>()) }
    var statusMessage by remember { mutableStateOf("Select BFS or DFS to start") }
    var isPlaying by remember { mutableStateOf(false) }
    var animationSpeed by remember { mutableFloatStateOf(1f) }
    var algorithmType by remember { mutableStateOf("BFS") }
    var traversalActive by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()

    fun resetGraph() {
        visitedNodes = emptySet()
        currentNodeId = null
        queueOrStack = emptyList()
        statusMessage = "Graph reset"
        isPlaying = false
        traversalActive = false
    }

    LaunchedEffect(traversalActive) {
        if (traversalActive) {
            val visited = mutableSetOf<Int>()
            if (algorithmType == "BFS") {
                val queue = mutableListOf(0)
                statusMessage = "Starting BFS from node A"
                
                while (queue.isNotEmpty() && traversalActive) {
                    while (!isPlaying && traversalActive) delay(200)
                    if (!traversalActive) break
                    
                    val curr = queue.removeAt(0)
                    queueOrStack = queue.toList()
                    currentNodeId = curr
                    
                    if (curr !in visited) {
                        statusMessage = "Visiting node ${nodes[curr].label}"
                        visited.add(curr)
                        visitedNodes = visited.toSet()
                        delay((1500 / animationSpeed).toLong())
                        
                        val neighbors = edges.filter { it.from == curr }.map { it.to } +
                                        edges.filter { it.to == curr }.map { it.from }
                        
                        for (neighbor in neighbors.distinct()) {
                            if (!traversalActive) break
                            if (neighbor !in visited && neighbor !in queue) {
                                while (!isPlaying && traversalActive) delay(200)
                                if (!traversalActive) break
                                statusMessage = "Adding neighbor ${nodes[neighbor].label} to queue"
                                queue.add(neighbor)
                                queueOrStack = queue.toList()
                                delay((800 / animationSpeed).toLong())
                            }
                        }
                    }
                }
            } else {
                val stack = mutableListOf(0)
                statusMessage = "Starting DFS from node A"
                
                while (stack.isNotEmpty() && traversalActive) {
                    while (!isPlaying && traversalActive) delay(200)
                    if (!traversalActive) break
                    
                    val curr = stack.removeAt(stack.size - 1)
                    queueOrStack = stack.toList()
                    currentNodeId = curr
                    
                    if (curr !in visited) {
                        statusMessage = "Visiting node ${nodes[curr].label}"
                        visited.add(curr)
                        visitedNodes = visited.toSet()
                        delay((1500 / animationSpeed).toLong())
                        
                        val neighbors = edges.filter { it.from == curr }.map { it.to } +
                                        edges.filter { it.to == curr }.map { it.from }
                        
                        for (neighbor in neighbors.distinct().reversed()) {
                            if (!traversalActive) break
                            if (neighbor !in visited && neighbor !in stack) {
                                while (!isPlaying && traversalActive) delay(200)
                                if (!traversalActive) break
                                statusMessage = "Pushing neighbor ${nodes[neighbor].label} to stack"
                                stack.add(neighbor)
                                queueOrStack = stack.toList()
                                delay((800 / animationSpeed).toLong())
                            }
                        }
                    }
                }
            }
            if (traversalActive) {
                currentNodeId = null
                statusMessage = "$algorithmType traversal complete"
                isPlaying = false
                traversalActive = false
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Graph Operations", fontWeight = FontWeight.Bold, color = DeepNavy) },
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
                    modifier = Modifier.fillMaxWidth().height(300.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        GraphVisualizationArea(nodes, edges, visitedNodes, currentNodeId)
                    }
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    LegendItem(Color(0xFF3B82F6), "Current")
                    Spacer(Modifier.width(24.dp))
                    LegendItem(TealGreen, "Visited")
                    Spacer(Modifier.width(24.dp))
                    LegendItem(TealGreen, "Unvisited", isOutline = true)
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
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            FilterChip(
                                selected = algorithmType == "BFS",
                                onClick = { 
                                    algorithmType = "BFS"
                                    resetGraph() 
                                },
                                label = { Text("BFS (Queue)") },
                                modifier = Modifier.weight(1f)
                            )
                            FilterChip(
                                selected = algorithmType == "DFS",
                                onClick = { 
                                    algorithmType = "DFS"
                                    resetGraph() 
                                },
                                label = { Text("DFS (Stack)") },
                                modifier = Modifier.weight(1f)
                            )
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
                                    if (!traversalActive) {
                                        traversalActive = true
                                        isPlaying = true
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
                                onClick = { resetGraph() },
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
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            if (algorithmType == "BFS") "Queue (FIFO)" else "Stack (LIFO)",
                            fontWeight = FontWeight.Bold,
                            color = DeepNavy
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            if (queueOrStack.isEmpty()) {
                                Text("Empty", color = Color.Gray, fontSize = 14.sp)
                            } else {
                                queueOrStack.forEach { nodeId ->
                                    val label = nodes[nodeId].label
                                    Box(
                                        modifier = Modifier
                                            .size(32.dp)
                                            .clip(CircleShape)
                                            .background(Color(0xFFEFF6FF))
                                            .border(1.dp, Color(0xFF3B82F6), CircleShape),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(label, color = Color(0xFF3B82F6), fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
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
                        Text(
                            if (algorithmType == "BFS") "Breadth First Search" else "Depth First Search",
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF92400E),
                            fontSize = 16.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            if (algorithmType == "BFS") 
                                "BFS starts at A and explores neighbors at the current depth before moving to the next level. It uses a Queue."
                            else 
                                "DFS starts at A and explores as far as possible along each branch before backtracking. It uses a Stack.",
                            fontSize = 14.sp,
                            color = Color(0xFFB45309),
                            lineHeight = 20.sp
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun LegendItem(color: Color, label: String, isOutline: Boolean = false) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(12.dp)
                .clip(CircleShape)
                .then(
                    if (isOutline) Modifier.border(2.dp, color, CircleShape)
                    else Modifier.background(color)
                )
        )
        Spacer(Modifier.width(8.dp))
        Text(label, fontSize = 13.sp, color = Color.Gray, fontWeight = FontWeight.Medium)
    }
}

@Composable
fun GraphVisualizationArea(
    nodes: List<GraphNode>,
    edges: List<GraphEdge>,
    visitedNodes: Set<Int>,
    currentNodeId: Int?
) {
    val density = LocalDensity.current
    
    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val width = constraints.maxWidth.toFloat()
        val height = constraints.maxHeight.toFloat()
        
        val xOffset = width / 2 - 150f
        val yOffset = height / 2 - 130f

        Canvas(modifier = Modifier.fillMaxSize()) {
            edges.forEach { edge ->
                val fromNode = nodes[edge.from]
                val toNode = nodes[edge.to]
                
                drawLine(
                    color = Color(0xFFE5E7EB),
                    start = Offset(fromNode.position.x + xOffset, fromNode.position.y + yOffset),
                    end = Offset(toNode.position.x + xOffset, toNode.position.y + yOffset),
                    strokeWidth = 2f
                )
            }
        }

        nodes.forEach { node ->
            val isVisited = visitedNodes.contains(node.id)
            val isCurrent = currentNodeId == node.id
            
            val bgColor by animateColorAsState(
                targetValue = when {
                    isCurrent -> Color(0xFF3B82F6)
                    isVisited -> TealGreen
                    else -> Color.White
                }
            )
            
            val textColor = if (isVisited || isCurrent) Color.White else TealGreen
            val borderColor = if (isVisited || isCurrent) Color.Transparent else TealGreen

            val xPosDp = with(density) { (node.position.x + xOffset).toDp() }
            val yPosDp = with(density) { (node.position.y + yOffset).toDp() }

            Box(
                modifier = Modifier
                    .offset(x = xPosDp - 20.dp, y = yPosDp - 20.dp)
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(bgColor)
                    .border(if (isVisited || isCurrent) 0.dp else 2.dp, borderColor, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(node.label, color = textColor, fontWeight = FontWeight.Bold)
            }
        }
    }
}
