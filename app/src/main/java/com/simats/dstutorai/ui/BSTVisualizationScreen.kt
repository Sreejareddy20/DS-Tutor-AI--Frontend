package com.simats.dstutorai.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.simats.dstutorai.ui.theme.DeepNavy
import com.simats.dstutorai.ui.theme.TealGreen
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class BSTNode(
    var value: Int,
    var left: BSTNode? = null,
    var right: BSTNode? = null
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BSTVisualizationScreen(
    onBackClick: () -> Unit,
    onChatClick: () -> Unit,
    onTopicsClick: () -> Unit,
    onPracticeClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    var root by remember { mutableStateOf<BSTNode?>(
        BSTNode(10).apply {
            left = BSTNode(5).apply {
                left = BSTNode(3)
                right = BSTNode(7)
            }
            right = BSTNode(15)
        }
    ) }
    
    var inputValue by remember { mutableStateOf("") }
    var currentNodeValue by remember { mutableStateOf<Int?>(null) }
    var statusMessage by remember { mutableStateOf("Ready to perform operations") }
    var animationSpeed by remember { mutableFloatStateOf(1f) }
    var isPlaying by remember { mutableStateOf(false) }
    var isOperationActive by remember { mutableStateOf(false) }
    
    val scope = rememberCoroutineScope()
    val density = LocalDensity.current

    suspend fun waitIfPaused() {
        while (!isPlaying && isOperationActive) {
            delay(200)
        }
    }

    suspend fun performInsert(value: Int) {
        isOperationActive = true
        statusMessage = "Inserting $value: Comparing with root..."
        
        if (root == null) {
            root = BSTNode(value)
            statusMessage = "Inserted $value as new root"
        } else {
            var curr = root
            while (curr != null) {
                waitIfPaused()
                if (!isOperationActive) break
                
                currentNodeValue = curr!!.value
                delay((1200 / animationSpeed).toLong())
                
                val currentVal = curr!!.value
                if (value < currentVal) {
                    statusMessage = "$value < $currentVal, moving to left child"
                    if (curr!!.left == null) {
                        waitIfPaused()
                        delay((800 / animationSpeed).toLong())
                        curr!!.left = BSTNode(value)
                        statusMessage = "Inserted $value to the left of $currentVal"
                        break
                    }
                    curr = curr!!.left
                } else if (value > currentVal) {
                    statusMessage = "$value > $currentVal, moving to right child"
                    if (curr!!.right == null) {
                        waitIfPaused()
                        delay((800 / animationSpeed).toLong())
                        curr!!.right = BSTNode(value)
                        statusMessage = "Inserted $value to the right of $currentVal"
                        break
                    }
                    curr = curr!!.right
                } else {
                    statusMessage = "Value $value already exists in the tree"
                    break
                }
            }
        }
        
        delay(1000)
        currentNodeValue = null
        isOperationActive = false
        isPlaying = false
    }

    suspend fun performDelete(value: Int) {
        isOperationActive = true
        statusMessage = "Searching for $value to delete..."
        
        fun minValueNode(node: BSTNode): BSTNode {
            var curr = node
            while (curr.left != null) {
                curr = curr.left!!
            }
            return curr
        }

        suspend fun deleteRecursive(node: BSTNode?, key: Int): BSTNode? {
            if (node == null) return null
            
            waitIfPaused()
            if (!isOperationActive) return node
            
            currentNodeValue = node.value
            delay((1000 / animationSpeed).toLong())
            
            return when {
                key < node.value -> {
                    statusMessage = "$key < ${node.value}, searching left"
                    node.left = deleteRecursive(node.left, key)
                    node
                }
                key > node.value -> {
                    statusMessage = "$key > ${node.value}, searching right"
                    node.right = deleteRecursive(node.right, key)
                    node
                }
                else -> {
                    statusMessage = "Found $key! Deleting node..."
                    delay(500)
                    if (node.left == null) return node.right
                    if (node.right == null) return node.left
                    
                    statusMessage = "Node has two children. Finding successor..."
                    val temp = minValueNode(node.right!!)
                    node.value = temp.value
                    statusMessage = "Replaced with successor ${temp.value}. Deleting successor..."
                    delay(800)
                    node.right = deleteRecursive(node.right, temp.value)
                    node
                }
            }
        }
        
        val newRoot = deleteRecursive(root, value)
        root = null // force refresh
        root = newRoot
        
        delay(1000)
        currentNodeValue = null
        statusMessage = "Deletion of $value complete"
        isOperationActive = false
        isPlaying = false
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("BST Operations", fontWeight = FontWeight.Bold, color = DeepNavy) },
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
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(8.dp))
                Card(
                    modifier = Modifier.fillMaxWidth().height(280.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        BSTVisualizationArea(root, currentNodeValue)
                    }
                }
            }

            item {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = Color(0xFFFFFBEB),
                    shape = RoundedCornerShape(8.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFEF3C7))
                ) {
                    Text(
                        text = "BST Property: Left subtree < Node < Right subtree",
                        modifier = Modifier.padding(12.dp),
                        color = Color(0xFF92400E),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center
                    )
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
                        Text("Animation Controls", fontWeight = FontWeight.Bold, color = DeepNavy)
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            Button(
                                onClick = { 
                                    if (!isOperationActive) {
                                        isPlaying = true
                                        scope.launch {
                                            statusMessage = "Auto-Play: Inserting random values..."
                                            val demoValues = List(3) { (1..30).random() }
                                            for (v in demoValues) {
                                                if (!isOperationActive && !isPlaying) break
                                                performInsert(v)
                                                delay(500)
                                            }
                                            statusMessage = "Auto-Play demo complete"
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
                                onClick = { 
                                    statusMessage = "Step forward triggered"
                                },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3B82F6)),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Icon(Icons.Default.SkipNext, null)
                                Spacer(Modifier.width(4.dp))
                                Text("Step")
                            }
                            Button(
                                onClick = { 
                                    isOperationActive = false
                                    isPlaying = false
                                    root = BSTNode(10).apply {
                                        left = BSTNode(5).apply {
                                            left = BSTNode(3)
                                            right = BSTNode(7)
                                        }
                                        right = BSTNode(15)
                                    }
                                    currentNodeValue = null
                                    statusMessage = "Tree Reset"
                                },
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
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Slow", fontSize = 12.sp, color = Color.Gray)
                            Text("Fast", fontSize = 12.sp, color = Color.Gray)
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
                        Text("BST Operations", fontWeight = FontWeight.Bold, color = DeepNavy)
                        Spacer(modifier = Modifier.height(12.dp))
                        OutlinedTextField(
                            value = inputValue,
                            onValueChange = { inputValue = it },
                            placeholder = { Text("Enter value") },
                            modifier = Modifier.fillMaxWidth(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFFE5E7EB),
                                unfocusedBorderColor = Color(0xFFE5E7EB)
                            )
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            Button(
                                onClick = {
                                    inputValue.toIntOrNull()?.let { 
                                        isPlaying = true
                                        scope.launch { performInsert(it) } 
                                    }
                                    inputValue = ""
                                },
                                modifier = Modifier.weight(1f).height(50.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF10B981)),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text("Insert")
                            }
                            Button(
                                onClick = {
                                    inputValue.toIntOrNull()?.let { 
                                        isPlaying = true
                                        scope.launch { performDelete(it) } 
                                    }
                                    inputValue = ""
                                },
                                modifier = Modifier.weight(1f).height(50.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF4444)),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text("Delete")
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
                        Text("Binary Search Tree", fontWeight = FontWeight.Bold, color = Color(0xFF92400E), fontSize = 16.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "A Binary Search Tree (BST) is a binary tree where for each node, all values in the left subtree are smaller and all values in the right subtree are larger. This property enables efficient searching, insertion, and deletion.",
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
                        BSTComplexityRow("Search (Average)", "O(log n)", Color(0xFFF0FDF4), Color(0xFF166534))
                        Spacer(modifier = Modifier.height(8.dp))
                        BSTComplexityRow("Insert (Average)", "O(log n)", Color(0xFFF0FDF4), Color(0xFF166534))
                        Spacer(modifier = Modifier.height(8.dp))
                        BSTComplexityRow("Delete (Average)", "O(log n)", Color(0xFFF0FDF4), Color(0xFF166534))
                        Spacer(modifier = Modifier.height(8.dp))
                        BSTComplexityRow("Worst Case (Skewed)", "O(n)", Color(0xFFFEF2F2), Color(0xFF991B1B))
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun BSTComplexityRow(label: String, complexity: String, bgColor: Color, textColor: Color) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        color = bgColor
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(label, color = textColor, fontSize = 14.sp, fontWeight = FontWeight.Medium)
            Text(complexity, color = textColor, fontSize = 14.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun BSTVisualizationArea(root: BSTNode?, currentNodeValue: Int?) {
    val density = LocalDensity.current
    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val width = constraints.maxWidth.toFloat()
        val centerX = width / 2
        val startY = with(density) { 40.dp.toPx() }
        val hSpacing = width / 4
        val vSpacing = with(density) { 60.dp.toPx() }

        Canvas(modifier = Modifier.fillMaxSize()) {
            drawBSTLines(root, Offset(centerX, startY), hSpacing, vSpacing)
        }

        Box(modifier = Modifier.fillMaxSize()) {
            DrawBSTNodesRecursive(root, Offset(centerX, startY), hSpacing, vSpacing, currentNodeValue, density)
        }
    }
}

fun androidx.compose.ui.graphics.drawscope.DrawScope.drawBSTLines(
    node: BSTNode?, pos: Offset, hSpace: Float, vSpace: Float
) {
    if (node == null) return
    node.left?.let {
        val leftPos = Offset(pos.x - hSpace, pos.y + vSpace)
        drawLine(Color(0xFFE5E7EB), pos, leftPos, strokeWidth = 1.5f)
        drawBSTLines(it, leftPos, hSpace / 1.8f, vSpace)
    }
    node.right?.let {
        val rightPos = Offset(pos.x + hSpace, pos.y + vSpace)
        drawLine(Color(0xFFE5E7EB), pos, rightPos, strokeWidth = 1.5f)
        drawBSTLines(it, rightPos, hSpace / 1.8f, vSpace)
    }
}

@Composable
fun BoxScope.DrawBSTNodesRecursive(
    node: BSTNode?, pos: Offset, hSpace: Float, vSpace: Float,
    currentNodeValue: Int?, density: androidx.compose.ui.unit.Density
) {
    if (node == null) return
    
    val isCurrent = currentNodeValue == node.value
    
    // Highlight current node in yellow during animation
    val bgColor by animateColorAsState(targetValue = if (isCurrent) Color(0xFF3B82F6) else Color(0xFF10B981))

    val xPosDp = with(density) { pos.x.toDp() }
    val yPosDp = with(density) { pos.y.toDp() }

    Box(
        modifier = Modifier
            .offset(x = xPosDp - 18.dp, y = yPosDp - 18.dp)
            .size(36.dp)
            .clip(CircleShape)
            .background(bgColor)
            .border(2.dp, if (isCurrent) Color.White else Color.Transparent, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Text(node.value.toString(), color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
    }

    DrawBSTNodesRecursive(node.left, Offset(pos.x - hSpace, pos.y + vSpace), hSpace / 1.8f, vSpace, currentNodeValue, density)
    DrawBSTNodesRecursive(node.right, Offset(pos.x + hSpace, pos.y + vSpace), hSpace / 1.8f, vSpace, currentNodeValue, density)
}
