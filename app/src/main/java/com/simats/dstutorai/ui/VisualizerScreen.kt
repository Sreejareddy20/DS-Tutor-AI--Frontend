package com.simats.dstutorai.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.simats.dstutorai.ui.theme.DeepNavy
import com.simats.dstutorai.ui.theme.FooterText
import com.simats.dstutorai.ui.theme.TealGreen

data class VisualizerItem(
    val title: String,
    val description: String,
    val icon: ImageVector,
    val iconBgColor: Color,
    val iconTintColor: Color
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VisualizerScreen(
    onBackClick: () -> Unit,
    onChatClick: () -> Unit,
    onTopicsClick: () -> Unit,
    onPracticeClick: () -> Unit,
    onProfileClick: () -> Unit,
    onPlayClick: (String) -> Unit
) {
    val visualizerItems = listOf(
        VisualizerItem(
            "Stack Push / Pop Animation",
            "Visualize stack operations step by step",
            Icons.Default.Layers,
            Color(0xFFF3E8FF),
            Color(0xFFA855F7)
        ),
        VisualizerItem(
            "Queue Enqueue / Dequeue",
            "See how queue works in FIFO order",
            Icons.AutoMirrored.Filled.List,
            Color(0xFFE0F2FE),
            Color(0xFF0EA5E9)
        ),
        VisualizerItem(
            "Linked List Insertion",
            "Visualize node insertion and traversal",
            Icons.Default.AccountTree,
            Color(0xFFDCFCE7),
            Color(0xFF22C55E)
        ),
        VisualizerItem(
            "Binary Tree Traversal",
            "Inorder, Preorder and Postorder",
            Icons.Default.Code,
            Color(0xFFFEF9C3),
            Color(0xFFEAB308)
        ),
        VisualizerItem(
            "BST Insert / Delete",
            "Visualize binary search tree operations",
            Icons.Default.Terrain,
            Color(0xFFFFEDD5),
            Color(0xFFF97316)
        ),
        VisualizerItem(
            "Graph BFS and DFS",
            "Breadth First and Depth First Search",
            Icons.Default.Hub,
            Color(0xFFFEE2E2),
            Color(0xFFEF4444)
        ),
        VisualizerItem(
            "Sorting Algorithms",
            "Bubble Sort, Merge Sort, Quick Sort",
            Icons.Default.BarChart,
            Color(0xFFE0E7FF),
            Color(0xFF6366F1)
        ),
        VisualizerItem(
            "Binary Search Visualization",
            "Understand how binary search works",
            Icons.Default.Search,
            Color(0xFFFCE7F3),
            Color(0xFFEC4899)
        ),
        VisualizerItem(
            "Heap Operations",
            "Min Heap and Max Heap visualization",
            Icons.Default.Storage,
            Color(0xFFFFEDD5),
            Color(0xFFF97316)
        ),
        VisualizerItem(
            "Hash Table Operations",
            "Visualize hashing and collision handling",
            Icons.Default.GridOn,
            Color(0xFFF5F3FF),
            Color(0xFF8B5CF6)
        )
    )

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(horizontal = 4.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = DeepNavy
                    )
                }
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Algorithm Visualizer",
                        color = DeepNavy,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Interactive visualizations",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                }
                // Spacer to balance the back button
                Box(modifier = Modifier.size(48.dp))
            }
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
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(visualizerItems) { item ->
                VisualizerCard(item, onPlayClick)
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Powered by SIMATS",
                        color = FooterText,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun VisualizerCard(item: VisualizerItem, onPlayClick: (String) -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = Color.White
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(item.iconBgColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = item.icon,
                    contentDescription = null,
                    tint = item.iconTintColor,
                    modifier = Modifier.size(28.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.title,
                    color = DeepNavy,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = item.description,
                    color = Color.Gray,
                    fontSize = 13.sp,
                    lineHeight = 18.sp
                )
            }

            IconButton(
                onClick = { onPlayClick(item.title) },
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.PlayCircle,
                    contentDescription = "Play",
                    tint = TealGreen,
                    modifier = Modifier.size(36.dp)
                )
            }
        }
    }
}
