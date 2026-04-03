package com.simats.dstutorai.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.simats.dstutorai.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopicDetailScreen(
    topic: Topic,
    onBackClick: () -> Unit,
    onVisualizeClick: () -> Unit,
    onCodeClick: () -> Unit,
    onGenerateCodeClick: () -> Unit,
    onPracticeQuizClick: () -> Unit,
    onAskAIClick: () -> Unit,
    onChatClick: () -> Unit,
    onTopicsClick: () -> Unit,
    onVisualizerClick: () -> Unit,
    onPracticeClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Explain", "Operations", "Code", "Uses")

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = topic.name,
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
                    "Chat" to Icons.Default.Chat,
                    "Topics" to Icons.Default.MenuBook,
                    "Visualizer" to Icons.Default.Visibility,
                    "Practice" to Icons.Default.School,
                    "Profile" to Icons.Default.Person
                )
                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        icon = { Icon(item.second, contentDescription = item.first) },
                        label = { Text(item.first, fontSize = 12.sp) },
                        selected = index == 1,
                        onClick = {
                            when (index) {
                                0 -> onChatClick()
                                1 -> onTopicsClick()
                                2 -> onVisualizerClick()
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
        containerColor = Color.White
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Tabs
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = Color.White,
                contentColor = TealGreen,
                divider = { HorizontalDivider(color = Color(0xFFF3F4F6)) },
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                        color = TealGreen
                    )
                }
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = {
                            Text(
                                text = title,
                                fontSize = 14.sp,
                                fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal,
                                color = if (selectedTab == index) DeepNavy else Color.Gray
                            )
                        }
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp)
            ) {
                when (selectedTab) {
                    0 -> ExplainTabContent(
                        topic = topic,
                        onVisualizeClick = onVisualizeClick,
                        onCodeClick = onCodeClick,
                        onPracticeQuizClick = onPracticeQuizClick,
                        onAskAIClick = onAskAIClick
                    )
                    1 -> OperationsTabContent(topicName = topic.name)
                    2 -> CodeTabContent(
                        topicName = topic.name,
                        onGenerateCodeClick = onGenerateCodeClick
                    )
                    3 -> UsesTabContent(topicName = topic.name)
                    else -> {
                        // Placeholder for other tabs
                        Box(modifier = Modifier.fillMaxWidth().padding(top = 40.dp), contentAlignment = Alignment.Center) {
                            Text("Content for ${tabs[selectedTab]} coming soon!", color = Color.Gray)
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(32.dp))
                
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
            }
        }
    }
}

@Composable
fun ExplainTabContent(
    topic: Topic,
    onVisualizeClick: () -> Unit,
    onCodeClick: () -> Unit,
    onPracticeQuizClick: () -> Unit,
    onAskAIClick: () -> Unit
) {
    Column {
        // Main Explanation Card
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            color = Color(0xFFF0F9F6) // Very light teal/green
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(TealGreen),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = topic.icon,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        "What is a ${topic.name}?",
                        color = DeepNavy,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                
                Spacer(modifier = Modifier.height(20.dp))
                
                Text(
                    text = getTopicFullDescription(topic.name),
                    color = SubtextContent,
                    fontSize = 15.sp,
                    lineHeight = 22.sp
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    text = "Example:",
                    color = SubtextContent,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = getTopicExample(topic.name),
                    color = SubtextContent,
                    fontSize = 15.sp,
                    lineHeight = 22.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Action Buttons
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Button(
                onClick = onVisualizeClick,
                modifier = Modifier.weight(1f).height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4285F4))
            ) {
                Icon(Icons.Default.Visibility, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Visualize", fontSize = 16.sp)
            }
            
            Button(
                onClick = onCodeClick,
                modifier = Modifier.weight(1f).height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF139D77))
            ) {
                Icon(Icons.Default.Code, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Code", fontSize = 16.sp)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedButton(
            onClick = onPracticeQuizClick,
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = TealGreen),
            border = androidx.compose.foundation.BorderStroke(1.dp, TealGreen)
        ) {
            Icon(Icons.Default.Lightbulb, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text("Practice Quiz", fontSize = 16.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .clip(RoundedCornerShape(12.dp))
                .border(1.dp, Color(0xFFE5E7EB), RoundedCornerShape(12.dp))
                .background(Color.White)
                .padding(horizontal = 16.dp),
            onClick = onAskAIClick
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(
                    "Ask AI about ${topic.name}",
                    color = DeepNavy,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
fun OperationsTabContent(topicName: String) {
    val operations = getTopicOperations(topicName)
    
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        operations.forEach { operation ->
            OperationCard(operation)
        }
    }
}

@Composable
fun OperationCard(operation: Operation) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFFF8F9FA)),
        color = Color(0xFFF8F9FA)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = operation.name,
                    color = DeepNavy,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                
                Surface(
                    color = Color(0xFFE8F5E9),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        text = operation.complexity,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                        color = Color(0xFF2E7D32),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = operation.description,
                color = SubtextContent,
                fontSize = 14.sp,
                lineHeight = 20.sp
            )
        }
    }
}

@Composable
fun CodeTabContent(topicName: String, onGenerateCodeClick: () -> Unit) {
    val code = getTopicCode(topicName)
    
    Column {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp)),
            color = Color(0xFF0F172A) // Dark slate background for code
        ) {
            Text(
                text = code,
                modifier = Modifier.padding(20.dp),
                color = Color.White,
                fontSize = 14.sp,
                fontFamily = FontFamily.Monospace,
                lineHeight = 22.sp
            )
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Button(
            onClick = onGenerateCodeClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF139D77))
        ) {
            Text("Generate Code in Other Languages", fontSize = 16.sp)
        }
    }
}

@Composable
fun UsesTabContent(topicName: String) {
    val uses = getTopicUses(topicName)
    
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        uses.forEachIndexed { index, use ->
            UseCard(index + 1, use)
        }
    }
}

@Composable
fun UseCard(number: Int, use: String) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp)),
        color = Color(0xFFF3F7FA) // Light blue/gray background
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(32.dp),
                shape = CircleShape,
                color = TealGreen
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = number.toString(),
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Text(
                text = use,
                color = DeepNavy,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

fun getTopicUses(topicName: String): List<String> {
    return when (topicName) {
        "Stack" -> listOf(
            "Function call management",
            "Expression evaluation",
            "Backtracking algorithms",
            "Browser history",
            "Undo/Redo functionality"
        )
        "Array" -> listOf(
            "Storing multiple items of same type",
            "Base for other data structures",
            "CPU scheduling",
            "Image processing (pixels)",
            "Lookup tables"
        )
        "Queue" -> listOf(
            "Task scheduling in OS",
            "Message buffering",
            "Breadth First Search (BFS)",
            "Printer spooling",
            "Handling interrupts"
        )
        "Linked List" -> listOf(
            "Dynamic memory allocation",
            "Implementing Stacks and Queues",
            "Symbol table management",
            "Undo functionality",
            "Image viewer (Prev/Next)"
        )
        else -> listOf(
            "Algorithm implementation",
            "Data organization",
            "Resource management",
            "Search and Sort operations",
            "Memory optimization"
        )
    }
}

fun getTopicCode(topicName: String): String {
    return when (topicName) {
        "Array" -> """
# Python Array implementation (using list)
arr = [10, 20, 30, 40, 50]

# Accessing elements
print(arr[0])  # Output: 10

# Inserting element
arr.append(60)

# Removing element
arr.pop(2)

# Finding length
length = len(arr)
        """.trimIndent()
        "Stack" -> """
class Stack:
    def __init__(self):
        self.items = []
        
    def push(self, item):
        self.items.append(item)
        
    def pop(self):
        if not self.is_empty():
            return self.items.pop()
            
    def peek(self):
        if not self.is_empty():
            return self.items[-1]
            
    def is_empty(self):
        return len(self.items) == 0
        """.trimIndent()
        "Queue" -> """
class Queue:
    def __init__(self):
        self.items = []
        
    def enqueue(self, item):
        self.items.append(item)
        
    def dequeue(self):
        if not self.is_empty():
            return self.items.pop(0)
            
    def is_empty(self):
        return len(self.items) == 0
        """.trimIndent()
        "Linked List" -> """
class Node:
    def __init__(self, data):
        self.data = data
        self.next = None

class LinkedList:
    def __init__(self):
        self.head = None
        
    def append(self, data):
        new_node = Node(data)
        if not self.head:
            self.head = new_node
            return
        last = self.head
        while last.next:
            last = last.next
        last.next = new_node
        """.trimIndent()
        "Binary Tree", "Binary Search Tree" -> """
class Node:
    def __init__(self, key):
        self.left = None
        self.right = None
        self.val = key

def insert(root, key):
    if root is None:
        return Node(key)
    else:
        if root.val < key:
            root.right = insert(root.right, key)
        else:
            root.left = insert(root.left, key)
    return root
        """.trimIndent()
        "Heap" -> """
import heapq

# Initialize a min-heap
min_heap = []

# Push elements
heapq.heappush(min_heap, 10)
heapq.heappush(min_heap, 1)

# Pop smallest element
smallest = heapq.heappop(min_heap)
        """.trimIndent()
        "Graph" -> """
class Graph:
    def __init__(self):
        self.adj_list = {}

    def add_edge(self, u, v):
        if u not in self.adj_list:
            self.adj_list[u] = []
        self.adj_list[u].append(v)
        """.trimIndent()
        "Hash Table" -> """
# Python's built-in dictionary is a Hash Table
hash_table = {}

# Insert key-value pair
hash_table["name"] = "Alice"
hash_table["age"] = 25

# Access by key
print(hash_table["name"])
        """.trimIndent()
        else -> """
# Implementation for $topicName
# Coming soon in next update!
        """.trimIndent()
    }
}

data class Operation(
    val name: String,
    val description: String,
    val complexity: String
)

fun getTopicOperations(topicName: String): List<Operation> {
    return when (topicName) {
        "Stack" -> listOf(
            Operation("Push", "Add an element to the top", "O(1)"),
            Operation("Pop", "Remove the top element", "O(1)"),
            Operation("Peek/Top", "View the top element without removing it", "O(1)"),
            Operation("isEmpty", "Check if stack is empty", "O(1)")
        )
        "Queue" -> listOf(
            Operation("Enqueue", "Add an element to the end", "O(1)"),
            Operation("Dequeue", "Remove an element from the front", "O(1)"),
            Operation("Front", "Get the front item from queue", "O(1)"),
            Operation("Rear", "Get the last item from queue", "O(1)")
        )
        "Array" -> listOf(
            Operation("Access", "Access an element at index", "O(1)"),
            Operation("Search", "Search for an element", "O(n)"),
            Operation("Insertion", "Insert an element", "O(n)"),
            Operation("Deletion", "Delete an element", "O(n)")
        )
        else -> listOf(
            Operation("Insertion", "Add new data to the structure", "Depends"),
            Operation("Deletion", "Remove data from the structure", "Depends"),
            Operation("Traversal", "Visit all elements in order", "O(n)")
        )
    }
}

// Helper functions for content
fun getTopicFullDescription(topicName: String): String {
    return when (topicName) {
        "Stack" -> "Stack is a linear data structure that follows the Last In First Out (LIFO) principle. This means the last element added to the stack will be the first one to be removed."
        "Array" -> "An Array is a collection of items stored at contiguous memory locations. The idea is to store multiple items of the same type together."
        "Queue" -> "A Queue is a linear structure which follows a particular order in which the operations are performed. The order is First In First Out (FIFO)."
        else -> "$topicName is a fundamental data structure in computer science. It allows for efficient organization and manipulation of data."
    }
}

fun getTopicExample(topicName: String): String {
    return when (topicName) {
        "Stack" -> "Think of a stack of plates - you add plates on top and remove them from the top."
        "Array" -> "Think of a bookshelf where each book has a fixed position or index."
        "Queue" -> "Think of a line of people waiting for a ticket - the person who arrives first is served first."
        else -> "Commonly used in various algorithms and real-world software applications."
    }
}
