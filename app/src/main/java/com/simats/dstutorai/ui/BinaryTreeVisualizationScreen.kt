package com.simats.dstutorai.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.simats.dstutorai.ui.theme.DeepNavy
import com.simats.dstutorai.ui.theme.TealGreen
import kotlinx.coroutines.delay

enum class TraversalType {
    Inorder, Preorder, Postorder
}

data class TreeVisualNode(
    val value: Int,
    val left: TreeVisualNode? = null,
    val right: TreeVisualNode? = null,
    val id: Int
)

data class TraversalStep(
    val visitedNodes: Set<Int>,
    val currentNodeId: Int?,
    val traversalOrder: List<Int>,
    val statusMessage: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BinaryTreeVisualizationScreen(
    onBackClick: () -> Unit,
    onChatClick: () -> Unit,
    onTopicsClick: () -> Unit,
    onPracticeClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    val tree = remember {
        TreeVisualNode(
            10,
            TreeVisualNode(5, TreeVisualNode(3, id = 3), TreeVisualNode(7, id = 7), id = 5),
            TreeVisualNode(15, TreeVisualNode(12, id = 12), TreeVisualNode(20, id = 20), id = 15),
            id = 10
        )
    }

    var traversalType by remember { mutableStateOf(TraversalType.Inorder) }
    var isPlaying by remember { mutableStateOf(false) }
    var animationSpeed by remember { mutableFloatStateOf(1f) }
    
    val traversalSteps = remember { mutableStateListOf<TraversalStep>() }
    var currentStepIndex by remember { mutableIntStateOf(0) }

    fun generateSteps() {
        traversalSteps.clear()
        val steps = mutableListOf<TraversalStep>()
        val visited = mutableSetOf<Int>()
        val order = mutableListOf<Int>()

        // Initial state
        steps.add(TraversalStep(emptySet(), null, emptyList(), "Select Traversal Type and press Play"))

        val methodDesc = when (traversalType) {
            TraversalType.Inorder -> "(Left → Root → Right)"
            TraversalType.Preorder -> "(Root → Left → Right)"
            TraversalType.Postorder -> "(Left → Right → Root)"
        }
        
        steps.add(TraversalStep(emptySet(), null, emptyList(), "Starting ${traversalType.name} Traversal $methodDesc..."))

        fun traverse(node: TreeVisualNode?) {
            if (node == null) return

            // PRE-ORDER: Root -> Left -> Right
            if (traversalType == TraversalType.Preorder) {
                steps.add(TraversalStep(visited.toSet(), node.id, order.toList(), "Visiting Node ${node.value} (Root)"))
                visited.add(node.id)
                order.add(node.value)
                steps.add(TraversalStep(visited.toSet(), node.id, order.toList(), "Node ${node.value} added to Traversal Order"))
            }

            traverse(node.left)

            // IN-ORDER: Left -> Root -> Right
            if (traversalType == TraversalType.Inorder) {
                steps.add(TraversalStep(visited.toSet(), node.id, order.toList(), "Visiting Node ${node.value}"))
                visited.add(node.id)
                order.add(node.value)
                steps.add(TraversalStep(visited.toSet(), node.id, order.toList(), "Node ${node.value} added to Traversal Order"))
            }

            traverse(node.right)

            // POST-ORDER: Left -> Right -> Root
            if (traversalType == TraversalType.Postorder) {
                steps.add(TraversalStep(visited.toSet(), node.id, order.toList(), "Visiting Node ${node.value}"))
                visited.add(node.id)
                order.add(node.value)
                steps.add(TraversalStep(visited.toSet(), node.id, order.toList(), "Node ${node.value} added to Traversal Order"))
            }
        }

        traverse(tree)
        // Final complete step
        steps.add(TraversalStep(visited.toSet(), null, order.toList(), "${traversalType.name} traversal complete!"))
        
        traversalSteps.addAll(steps)
        currentStepIndex = 0
    }

    // Regenerate steps when traversal type changes
    LaunchedEffect(traversalType) {
        generateSteps()
    }

    // Auto-play control
    LaunchedEffect(isPlaying, currentStepIndex) {
        if (isPlaying && currentStepIndex < traversalSteps.size - 1) {
            delay((1000 / animationSpeed).toLong())
            currentStepIndex++
        } else if (currentStepIndex >= traversalSteps.size - 1) {
            isPlaying = false
        }
    }

    val currentStep = if (currentStepIndex >= 0 && currentStepIndex < traversalSteps.size) {
        traversalSteps[currentStepIndex]
    } else {
        TraversalStep(emptySet(), null, emptyList(), "Ready")
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Binary Tree Traversal", fontWeight = FontWeight.Bold, color = DeepNavy) },
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
                // Tree Card
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(260.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        TreeVisualizationArea(tree, currentStep.currentNodeId, currentStep.visitedNodes)
                    }
                }
            }

            item {
                // Legend
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    VisualLegendItem(Color(0xFF3B82F6), "Current")
                    Spacer(modifier = Modifier.width(20.dp))
                    VisualLegendItem(TealGreen, "Visited")
                    Spacer(modifier = Modifier.width(20.dp))
                    VisualLegendItem(TealGreen, "Unvisited", isOutline = true)
                }
            }

            if (currentStep.traversalOrder.isNotEmpty()) {
                item {
                    // Traversal Order Box
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Brush.horizontalGradient(listOf(Color(0xFF0EA5E9), Color(0xFF3B82F6), TealGreen)),
                                shape = RoundedCornerShape(12.dp)
                            )
                            .padding(16.dp)
                    ) {
                        Column {
                            Text("Traversal Order:", color = Color.White.copy(alpha = 0.9f), fontSize = 14.sp, fontWeight = FontWeight.Medium)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                currentStep.traversalOrder.joinToString(" → "),
                                color = Color.White,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            item {
                // Status Information
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = Color(0xFFEFF6FF),
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFBFDBFE))
                ) {
                    Text(
                        text = currentStep.statusMessage,
                        modifier = Modifier.padding(14.dp),
                        color = Color(0xFF1E40AF),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            item {
                // Traversal Type Selector
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Select Traversal Type", fontWeight = FontWeight.Bold, color = DeepNavy, fontSize = 16.sp)
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            TraversalType.entries.forEach { type ->
                                FilterChip(
                                    selected = traversalType == type,
                                    onClick = { traversalType = type },
                                    label = { Text(type.name) },
                                    modifier = Modifier.weight(1f),
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = TealGreen,
                                        selectedLabelColor = Color.White
                                    )
                                )
                            }
                        }
                    }
                }
            }

            item {
                // Control Panel
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Animation Controls", fontWeight = FontWeight.Bold, color = DeepNavy)
                            
                            Row {
                                IconButton(onClick = { 
                                    if (currentStepIndex > 0) currentStepIndex--
                                    isPlaying = false
                                }) {
                                    Icon(Icons.Default.SkipPrevious, contentDescription = "Previous")
                                }
                                
                                FilledIconButton(
                                    onClick = { isPlaying = !isPlaying },
                                    colors = IconButtonDefaults.filledIconButtonColors(containerColor = TealGreen)
                                ) {
                                    Icon(if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow, contentDescription = "Play/Pause")
                                }
                                
                                IconButton(onClick = { 
                                    if (currentStepIndex < traversalSteps.size - 1) currentStepIndex++
                                    isPlaying = false
                                }) {
                                    Icon(Icons.Default.SkipNext, contentDescription = "Next")
                                }

                                IconButton(onClick = { 
                                    currentStepIndex = 0
                                    isPlaying = false
                                }) {
                                    Icon(Icons.Default.Refresh, contentDescription = "Reset")
                                }
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Speed: ${"%.1f".format(animationSpeed)}x", fontSize = 13.sp, color = Color.Gray)
                        Slider(
                            value = animationSpeed,
                            onValueChange = { animationSpeed = it },
                            valueRange = 0.5f..3f,
                            colors = SliderDefaults.colors(thumbColor = TealGreen, activeTrackColor = TealGreen)
                        )
                    }
                }
            }

            item {
                // Algorithm Info
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("About ${traversalType.name} Traversal", fontWeight = FontWeight.Bold, color = DeepNavy, fontSize = 17.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        val description = when (traversalType) {
                            TraversalType.Inorder -> "In-order traversal visits the left subtree, then the root, then the right subtree. For binary search trees, it visits nodes in ascending order."
                            TraversalType.Preorder -> "Pre-order traversal visits the root node first, then recursively visits the left subtree and then the right subtree. Used to create a copy of the tree."
                            TraversalType.Postorder -> "Post-order traversal visits the left subtree first, then the right subtree, and finally the root node. Used to delete the tree or evaluate postfix expressions."
                        }
                        
                        Text(description, fontSize = 14.sp, color = Color.Gray, lineHeight = 20.sp)
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Complexity Analysis", fontWeight = FontWeight.Bold, color = DeepNavy, fontSize = 15.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        ComplexityDisplayBox("Time Complexity", "O(N)", Color(0xFFF0F9FF), Color(0xFF0369A1))
                        Spacer(modifier = Modifier.height(8.dp))
                        ComplexityDisplayBox("Space Complexity", "O(H)", Color(0xFFFDF2F8), Color(0xFF9D174D))
                        
                        Text(
                            "* N is number of nodes, H is height of the tree",
                            fontSize = 11.sp,
                            color = Color.Gray,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun TreeVisualizationArea(root: TreeVisualNode, currentNodeId: Int?, visitedNodes: Set<Int>) {
    val density = LocalDensity.current
    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val width = constraints.maxWidth.toFloat()
        val centerX = width / 2
        val startY = with(density) { 40.dp.toPx() }
        val hSpacing = width / 4
        val vSpacing = with(density) { 60.dp.toPx() }

        Canvas(modifier = Modifier.fillMaxSize()) {
            drawTreeLines(root, Offset(centerX, startY), hSpacing, vSpacing)
        }

        DrawNodesRecursive(root, Offset(centerX, startY), hSpacing, vSpacing, currentNodeId, visitedNodes, density)
    }
}

fun androidx.compose.ui.graphics.drawscope.DrawScope.drawTreeLines(
    node: TreeVisualNode, pos: Offset, hSpace: Float, vSpace: Float
) {
    node.left?.let {
        val leftPos = Offset(pos.x - hSpace, pos.y + vSpace)
        drawLine(Color(0xFFE5E7EB), pos, leftPos, strokeWidth = 1.5f)
        drawTreeLines(it, leftPos, hSpace / 1.8f, vSpace)
    }
    node.right?.let {
        val rightPos = Offset(pos.x + hSpace, pos.y + vSpace)
        drawLine(Color(0xFFE5E7EB), pos, rightPos, strokeWidth = 1.5f)
        drawTreeLines(it, rightPos, hSpace / 1.8f, vSpace)
    }
}

@Composable
fun BoxScope.DrawNodesRecursive(
    node: TreeVisualNode, pos: Offset, hSpace: Float, vSpace: Float,
    currentNodeId: Int?, visitedNodes: Set<Int>, density: androidx.compose.ui.unit.Density
) {
    val isCurrent = currentNodeId == node.id
    val isVisited = visitedNodes.contains(node.id)
    
    val bgColor by animateColorAsState(targetValue = when {
        isCurrent -> Color(0xFF3B82F6)
        isVisited -> TealGreen
        else -> Color.White
    }, animationSpec = tween(400))

    val textColor by animateColorAsState(targetValue = if (isCurrent || isVisited) Color.White else TealGreen)
    val borderColor by animateColorAsState(targetValue = if (isVisited || isCurrent) Color.Transparent else TealGreen)

    val xPosDp = with(density) { pos.x.toDp() }
    val yPosDp = with(density) { pos.y.toDp() }

    Box(
        modifier = Modifier
            .offset(x = xPosDp - 20.dp, y = yPosDp - 20.dp)
            .size(40.dp)
            .clip(CircleShape)
            .background(bgColor)
            .border(2.dp, borderColor, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Text(node.value.toString(), color = textColor, fontWeight = FontWeight.Bold, fontSize = 15.sp)
    }

    node.left?.let { DrawNodesRecursive(it, Offset(pos.x - hSpace, pos.y + vSpace), hSpace / 1.8f, vSpace, currentNodeId, visitedNodes, density) }
    node.right?.let { DrawNodesRecursive(it, Offset(pos.x + hSpace, pos.y + vSpace), hSpace / 1.8f, vSpace, currentNodeId, visitedNodes, density) }
}

@Composable
private fun VisualLegendItem(color: Color, text: String, isOutline: Boolean = false) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(modifier = Modifier.size(14.dp).clip(CircleShape).then(
            if (isOutline) Modifier.border(2.dp, color, CircleShape) else Modifier.background(color)
        ))
        Spacer(modifier = Modifier.width(8.dp))
        Text(text, fontSize = 13.sp, color = Color.Gray, fontWeight = FontWeight.Medium)
    }
}

@Composable
private fun ComplexityDisplayBox(label: String, value: String, bgColor: Color, textColor: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(bgColor, RoundedCornerShape(8.dp))
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, color = DeepNavy.copy(alpha = 0.7f), fontSize = 14.sp, fontWeight = FontWeight.Medium)
        Text(value, color = textColor, fontWeight = FontWeight.Bold, fontSize = 15.sp)
    }
}
