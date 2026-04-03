package com.simats.dstutorai.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.simats.dstutorai.ui.theme.DeepNavy
import com.simats.dstutorai.ui.theme.FooterText
import com.simats.dstutorai.ui.theme.TealGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CodeGeneratorScreen(
    initialTopic: String = "Stack",
    onBackClick: () -> Unit,
    onGenerateClick: (String, String) -> Unit
) {
    var selectedTopic by remember { mutableStateOf(initialTopic) }
    var selectedLanguage by remember { mutableStateOf("Python") }
    
    var topicExpanded by remember { mutableStateOf(false) }
    var languageExpanded by remember { mutableStateOf(false) }
    
    var generatedCode by remember { mutableStateOf<String?>(null) }
    var isCopied by remember { mutableStateOf(false) }
    val clipboardManager = LocalClipboardManager.current

    val topics = listOf(
        "Array", "Stack", "Queue", "Linked List", 
        "Binary Tree", "Binary Search Tree", "Heap", 
        "Graph", "Hash Table", "Sorting", "Searching"
    )
    val languages = listOf("Python", "Java")

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Code Generator",
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
        containerColor = Color.White
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(24.dp)
        ) {
            Text(
                "Select Data Structure / Algorithm",
                color = DeepNavy,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(8.dp))
            
            Box {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .border(1.dp, Color(0xFFE5E7EB), RoundedCornerShape(12.dp))
                        .clickable { topicExpanded = true },
                    color = Color(0xFFF9FAFB)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = selectedTopic, color = DeepNavy)
                        Icon(Icons.Default.ArrowDropDown, contentDescription = null, tint = Color.Gray)
                    }
                }
                DropdownMenu(
                    expanded = topicExpanded,
                    onDismissRequest = { topicExpanded = false },
                    modifier = Modifier.fillMaxWidth(0.85f).background(Color.White)
                ) {
                    topics.forEach { topic ->
                        DropdownMenuItem(
                            text = { Text(topic) },
                            onClick = {
                                selectedTopic = topic
                                topicExpanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                "Select Programming Language",
                color = DeepNavy,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(8.dp))
            
            Box {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .border(1.dp, Color(0xFFE5E7EB), RoundedCornerShape(12.dp))
                        .clickable { languageExpanded = true },
                    color = Color(0xFFF9FAFB)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = selectedLanguage, color = DeepNavy)
                        Icon(Icons.Default.ArrowDropDown, contentDescription = null, tint = Color.Gray)
                    }
                }
                DropdownMenu(
                    expanded = languageExpanded,
                    onDismissRequest = { languageExpanded = false },
                    modifier = Modifier.fillMaxWidth(0.85f).background(Color.White)
                ) {
                    languages.forEach { language ->
                        DropdownMenuItem(
                            text = { Text(language) },
                            onClick = {
                                selectedLanguage = language
                                languageExpanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = { 
                    generatedCode = getGeneratedCode(selectedTopic, selectedLanguage)
                    isCopied = false
                    onGenerateClick(selectedTopic, selectedLanguage)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = TealGreen)
            ) {
                Text("Generate Code", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }

            if (generatedCode != null) {
                Spacer(modifier = Modifier.height(32.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Generated Code",
                        color = DeepNavy,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Row(
                        modifier = Modifier.clickable {
                            clipboardManager.setText(AnnotatedString(generatedCode!!))
                            isCopied = true
                        },
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = if (isCopied) Icons.Default.Check else Icons.Default.ContentCopy,
                            contentDescription = "Copy",
                            tint = DeepNavy,
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = if (isCopied) "Copied" else "Copy",
                            color = DeepNavy,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp)),
                    color = Color(0xFF0F172A) // Dark slate background for code
                ) {
                    Text(
                        text = generatedCode!!,
                        modifier = Modifier.padding(20.dp),
                        color = Color.White,
                        fontSize = 14.sp,
                        fontFamily = FontFamily.Monospace,
                        lineHeight = 22.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

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

fun getGeneratedCode(topic: String, language: String): String {
    return when (language) {
        "Python" -> getPythonCode(topic)
        "Java" -> getJavaCode(topic)
        else -> "# No code available for $topic"
    }
}

private fun getPythonCode(topic: String): String {
    return when (topic) {
        "Array" -> """
# Array (List) Implementation in Python
my_array = [10, 20, 30, 40, 50]

# Accessing elements
print(f"First element: {my_array[0]}")

# Adding elements
my_array.append(60)
my_array.insert(1, 15) # Insert at index 1

# Removing elements
my_array.pop() # Remove last
my_array.remove(20) # Remove by value
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
        return None
            
    def is_empty(self):
        return len(self.items) == 0
        """.trimIndent()
        "Queue" -> """
from collections import deque

class Queue:
    def __init__(self):
        self.items = deque()
        
    def enqueue(self, item):
        self.items.append(item)
        
    def dequeue(self):
        if not self.is_empty():
            return self.items.popleft()
        return None
        """.trimIndent()
        "Linked List" -> """
class Node:
    def __init__(self, data):
        self.data = data
        self.next = None

class LinkedList:
    def __init__(self):
        self.head = None
        
    def insert(self, data):
        new_node = Node(data)
        if not self.head:
            self.head = new_node
            return
        last = self.head
        while last.next:
            last = last.next
        last.next = new_node
        """.trimIndent()
        "Binary Tree" -> """
class Node:
    def __init__(self, key):
        self.left = None
        self.right = None
        self.val = key

def inorder(root):
    if root:
        inorder(root.left)
        print(root.val, end=" ")
        inorder(root.right)
        """.trimIndent()
        "Binary Search Tree" -> """
class Node:
    def __init__(self, key):
        self.left = self.right = None
        self.val = key

def insert(root, key):
    if root is None: return Node(key)
    if key < root.val: root.left = insert(root.left, key)
    else: root.right = insert(root.right, key)
    return root
        """.trimIndent()
        "Heap" -> """
import heapq

# Min Heap implementation
heap = []
heapq.heappush(heap, 10)
heapq.heappush(heap, 5)
heapq.heappush(heap, 15)

print(heapq.heappop(heap)) # 5
        """.trimIndent()
        "Graph" -> """
class Graph:
    def __init__(self):
        self.adj = {}
        
    def add_edge(self, u, v):
        if u not in self.adj: self.adj[u] = []
        self.adj[u].append(v)
        """.trimIndent()
        "Hash Table" -> """
# Python Dictionary as Hash Table
hash_map = {}
hash_map["key1"] = "value1"
print(hash_map.get("key1"))
        """.trimIndent()
        "Sorting" -> """
# Bubble Sort in Python
def bubble_sort(arr):
    n = len(arr)
    for i in range(n):
        for j in range(0, n-i-1):
            if arr[j] > arr[j+1]:
                arr[j], arr[j+1] = arr[j+1], arr[j]
    return arr
        """.trimIndent()
        "Searching" -> """
# Binary Search in Python
def binary_search(arr, x):
    low, high = 0, len(arr) - 1
    while low <= high:
        mid = (low + high) // 2
        if arr[mid] < x: low = mid + 1
        elif arr[mid] > x: high = mid - 1
        else: return mid
    return -1
        """.trimIndent()
        else -> "# Code for $topic"
    }
}

private fun getJavaCode(topic: String): String {
    return when (topic) {
        "Array" -> """
public class Main {
    public static void main(String[] args) {
        int[] arr = {10, 20, 30, 40, 50};
        System.out.println(arr[0]);
    }
}
        """.trimIndent()
        "Stack" -> """
import java.util.Stack;

public class Main {
    public static void main(String[] args) {
        Stack<Integer> s = new Stack<>();
        s.push(10);
        System.out.println(s.pop());
    }
}
        """.trimIndent()
        "Queue" -> """
import java.util.LinkedList;
import java.util.Queue;

public class Main {
    public static void main(String[] args) {
        Queue<Integer> q = new LinkedList<>();
        q.add(10);
        System.out.println(q.remove());
    }
}
        """.trimIndent()
        "Linked List" -> """
class Node {
    int data; Node next;
    Node(int d) { data = d; next = null; }
}

public class LinkedList {
    Node head;
    void add(int d) { /* Implementation */ }
}
        """.trimIndent()
        "Binary Tree" -> """
class Node {
    int key; Node left, right;
    Node(int item) { key = item; }
}

public class BinaryTree {
    Node root;
    void traverse(Node n) { /* Implementation */ }
}
        """.trimIndent()
        "Binary Search Tree" -> """
class Node {
    int val; Node left, right;
    Node(int v) { val = v; }
}

public class BST {
    Node insert(Node root, int val) {
        if (root == null) return new Node(val);
        if (val < root.val) root.left = insert(root.left, val);
        else root.right = insert(root.right, val);
        return root;
    }
}
        """.trimIndent()
        "Heap" -> """
import java.util.PriorityQueue;

public class Main {
    public static void main(String[] args) {
        PriorityQueue<Integer> minHeap = new PriorityQueue<>();
        minHeap.add(10);
        minHeap.poll();
    }
}
        """.trimIndent()
        "Graph" -> """
import java.util.*;

public class Graph {
    private Map<Integer, List<Integer>> adj = new HashMap<>();
    void addEdge(int u, int v) {
        adj.computeIfAbsent(u, k -> new ArrayList<>()).add(v);
    }
}
        """.trimIndent()
        "Hash Table" -> """
import java.util.HashMap;

public class Main {
    public static void main(String[] args) {
        HashMap<String, Integer> map = new HashMap<>();
        map.put("One", 1);
    }
}
        """.trimIndent()
        "Sorting" -> """
// Bubble Sort Java
public class Sorting {
    void bubbleSort(int[] arr) {
        int n = arr.length;
        for (int i = 0; i < n-1; i++)
            for (int j = 0; j < n-i-1; j++)
                if (arr[j] > arr[j+1]) {
                    int temp = arr[j];
                    arr[j] = arr[j+1];
                    arr[j+1] = temp;
                }
    }
}
        """.trimIndent()
        "Searching" -> """
// Binary Search Java
public class Searching {
    int binarySearch(int[] arr, int x) {
        int l = 0, r = arr.length - 1;
        while (l <= r) {
            int m = l + (r - l) / 2;
            if (arr[m] == x) return m;
            if (arr[m] < x) l = m + 1;
            else r = m - 1;
        }
        return -1;
    }
}
        """.trimIndent()
        else -> "// Java code for $topic"
    }
}


