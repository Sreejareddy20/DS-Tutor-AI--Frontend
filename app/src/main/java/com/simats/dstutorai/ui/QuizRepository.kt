package com.simats.dstutorai.ui

object QuizRepository {
    fun generateQuestions(topic: String, difficulty: String): List<QuizQuestion> {
        return when (topic) {
            "Stack" -> getStackQuestions(difficulty)
            "Array" -> getArrayQuestions(difficulty)
            "Queue" -> getQueueQuestions(difficulty)
            "Linked List" -> getLinkedListQuestions(difficulty)
            "Binary Tree" -> getBinaryTreeQuestions(difficulty)
            "Binary Search Tree" -> getBSTQuestions(difficulty)
            "Heap" -> getHeapQuestions(difficulty)
            "Graph" -> getGraphQuestions(difficulty)
            "Hash Table" -> getHashTableQuestions(difficulty)
            "Sorting" -> getSortingQuestions(difficulty)
            "Searching" -> getSearchingQuestions(difficulty)
            else -> getGenericQuestions(topic, difficulty)
        }
    }

    private fun getStackQuestions(difficulty: String): List<QuizQuestion> {
        return when (difficulty) {
            "Easy" -> listOf(
                QuizQuestion("What does LIFO stand for in Stack?", listOf("Last In First Out", "Last In Fast Out", "Lead In First Out", "Last In First Order"), 0),
                QuizQuestion("Which operation adds an item to the stack?", listOf("Pop", "Peek", "Push", "Shift"), 2),
                QuizQuestion("Where are elements added in a stack?", listOf("Bottom", "Middle", "Top", "Random"), 2),
                QuizQuestion("Which operation removes the top element?", listOf("Push", "Peek", "Pop", "Delete"), 2),
                QuizQuestion("What is 'Peek' operation?", listOf("Removing element", "Viewing top element without removing", "Adding element", "Inverting stack"), 1),
                QuizQuestion("A stack is a _____ data structure.", listOf("Linear", "Non-linear", "Hierarchical", "Graphical"), 0),
                QuizQuestion("Stack underflow occurs when?", listOf("Popping from empty stack", "Pushing to full stack", "Stack is full", "Accessing middle"), 0),
                QuizQuestion("Real-world example of stack?", listOf("Queue at bus stop", "Pile of plates", "Family tree", "Circular road"), 1)
            )
            "Medium" -> listOf(
                QuizQuestion("Which data structure is used for recursion?", listOf("Queue", "Stack", "Linked List", "Tree"), 1),
                QuizQuestion("Time complexity of push operation?", listOf("O(n)", "O(log n)", "O(1)", "O(n^2)"), 2),
                QuizQuestion("Which application uses a stack?", listOf("Undo mechanism", "Print spooling", "Waitlists", "Pathfinding"), 0),
                QuizQuestion("Stacks can be implemented using:", listOf("Arrays only", "Linked Lists only", "Arrays or Linked Lists", "None"), 2),
                QuizQuestion("Converting infix to postfix requires:", listOf("Queue", "Stack", "BST", "Graph"), 1),
                QuizQuestion("Function calls in programming use:", listOf("Call Stack", "Heap", "Queue", "B-Tree"), 0),
                QuizQuestion("Space complexity of stack with N elements?", listOf("O(1)", "O(N)", "O(log N)", "O(N^2)"), 1),
                QuizQuestion("If we push 10, 20, 30 and pop once, top is:", listOf("10", "20", "30", "Empty"), 1)
            )
            "Hard" -> listOf(
                QuizQuestion("Evaluating '5 3 + 8 *' (postfix) gives:", listOf("64", "40", "24", "16"), 0),
                QuizQuestion("Minimum stacks to implement a queue?", listOf("1", "2", "3", "4"), 1),
                QuizQuestion("Checking balanced parentheses uses:", listOf("Queue", "Tree", "Stack", "Hash"), 2),
                QuizQuestion("In recursion, 'Implicit Stack' is managed by:", listOf("Programmer", "OS", "Linker", "Compiler/Runtime"), 3),
                QuizQuestion("Linked list stack: 'Push' should happen at:", listOf("Head", "Tail", "Middle", "Anywhere"), 0),
                QuizQuestion("Amortized time for dynamic array push is:", listOf("O(1)", "O(n)", "O(log n)", "O(1.5)"), 0),
                QuizQuestion("Which uses stack-based memory management?", listOf("JVM", "Static RAM", "External Drive", "Cache"), 0),
                QuizQuestion("Postfix expression 'ABC*+' is equivalent to:", listOf("A+B*C", "A*B+C", "(A+B)*C", "A+B+C"), 0)
            )
            else -> getStackQuestions("Medium")
        }
    }

    private fun getArrayQuestions(difficulty: String): List<QuizQuestion> {
        return when (difficulty) {
            "Easy" -> listOf(
                QuizQuestion("First index of array in most languages?", listOf("-1", "0", "1", "Empty"), 1),
                QuizQuestion("Memory allocation for static array is:", listOf("Contiguous", "Distributed", "Linked", "Dynamic"), 0),
                QuizQuestion("What is the index of last element in array size N?", listOf("N", "N+1", "N-1", "0"), 2),
                QuizQuestion("Can arrays store different data types (in standard C/C++)?", listOf("Yes", "No", "Sometimes", "Only in Java"), 1),
                QuizQuestion("Array is what type of data structure?", listOf("Dynamic", "Linear", "Hierarchical", "Hash-based"), 1),
                QuizQuestion("Complexity to access arr[i] by index?", listOf("O(1)", "O(n)", "O(log n)", "O(n log n)"), 0),
                QuizQuestion("An array of size [3][4] has how many elements?", listOf("7", "12", "3", "4"), 1),
                QuizQuestion("Array overflow happens when?", listOf("Accessing index 0", "Accessing index N", "Removing element", "Adding element"), 1)
            )
            "Medium" -> listOf(
                QuizQuestion("Time complexity of Linear Search in array?", listOf("O(1)", "O(log n)", "O(n)", "O(n^2)"), 2),
                QuizQuestion("Time complexity of Binary Search in sorted array?", listOf("O(1)", "O(n)", "O(log n)", "O(n log n)"), 2),
                QuizQuestion("Inserting at the front of array size N takes:", listOf("O(1)", "O(n)", "O(log n)", "O(n^2)"), 1),
                QuizQuestion("Average time complexity of push in dynamic array?", listOf("O(1)", "O(n)", "O(log n)", "O(1.5)"), 0),
                QuizQuestion("A 2D array is stored in memory as:", listOf("Grid", "Linear sequence", "Tree", "Graph"), 1),
                QuizQuestion("Row-major order indexing [i,j] for M x N array is:", listOf("i*N + j", "i+j", "j*M + i", "i*M + j"), 0),
                QuizQuestion("Rotating array by 1 position (left shift) takes:", listOf("O(1)", "O(n)", "O(log n)", "O(N^2)"), 1),
                QuizQuestion("Which is best for Cache Locality?", listOf("Linked List", "Array", "Graph", "Tree"), 1)
            )
            "Hard" -> listOf(
                QuizQuestion("Boyer-Moore Voting algorithm complexity is:", listOf("O(n log n)", "O(n)", "O(n^2)", "O(1)"), 1),
                QuizQuestion("Finding missing number in 1-N array takes:", listOf("O(log n)", "O(n)", "O(n^2)", "O(1)"), 1),
                QuizQuestion("Kadane's Algorithm is used for:", listOf("Searching", "Sorting", "Max Subarray Sum", "Matrix Mul"), 2),
                QuizQuestion("Prefix Sum array approach reduces complexity to:", listOf("O(1) per query", "O(log n) per query", "O(n) per query", "O(1) space"), 0),
                QuizQuestion("Finding intersection of 2 sorted arrays size N, M takes:", listOf("O(N*M)", "O(N+M)", "O(min(N,M))", "O(log N)"), 1),
                QuizQuestion("Dutch National Flag algorithm sorts in:", listOf("O(n^2)", "O(n log n)", "O(n)", "O(1)"), 2),
                QuizQuestion("Sliding Window technique is usually:", listOf("O(n)", "O(n^2)", "O(2^n)", "O(log n)"), 0),
                QuizQuestion("Sparse Matrix representation usually uses:", listOf("Simple 2D Array", "Compressed Row Storage", "Binary Tree", "Stack"), 1)
            )
            else -> getArrayQuestions("Medium")
        }
    }

    private fun getQueueQuestions(difficulty: String): List<QuizQuestion> {
        return when (difficulty) {
            "Easy" -> listOf(
                QuizQuestion("What does FIFO stand for?", listOf("First In First Out", "Fast In Fast Out", "First In Fast Order", "Fixed In Fixed Out"), 0),
                QuizQuestion("Where does deletion happen in a queue?", listOf("Rear", "Front", "Middle", "Anywhere"), 1),
                QuizQuestion("Where does insertion happen in a queue?", listOf("Front", "Rear", "Top", "Bottom"), 1),
                QuizQuestion("Operation to add element to queue is:", listOf("Push", "Pop", "Enqueue", "Dequeue"), 2),
                QuizQuestion("Operation to remove element from queue is:", listOf("Push", "Pop", "Enqueue", "Dequeue"), 3),
                QuizQuestion("Which describes a queue?", listOf("Linear", "Non-linear", "LIFO", "Hierarchical"), 0),
                QuizQuestion("Queue underflow is:", listOf("Full queue", "Empty queue deletion", "Adding to full", "Wrong index"), 1),
                QuizQuestion("Real-world example of queue?", listOf("Stack of trays", "Waitlist at bank", "Folders on PC", "Undo button"), 1)
            )
            "Medium" -> listOf(
                QuizQuestion("Breadth First Search (BFS) uses:", listOf("Stack", "Queue", "BST", "Graph"), 1),
                QuizQuestion("Which queue allows insertion/deletion at both ends?", listOf("Priority Queue", "Circular Queue", "Deque", "Simple Queue"), 2),
                QuizQuestion("Time complexity of Enqueue in simple queue?", listOf("O(1)", "O(n)", "O(log n)", "O(n^2)"), 0),
                QuizQuestion("Benefit of Circular Queue?", listOf("Less memory", "Faster access", "Avoid memory wastage", "Easier to code"), 2),
                QuizQuestion("In Circular Queue, 'Rear' after N-1 is:", listOf("Overflow", "Underflow", "Index 0", "Index 1"), 2),
                QuizQuestion("Print spooler in OS uses:", listOf("Queue", "Stack", "Tree", "Hash"), 0),
                QuizQuestion("Time complexity of Dequeue in simple queue?", listOf("O(1)", "O(n)", "O(log n)", "O(n^2)"), 0),
                QuizQuestion("Minimum queues to implement a stack?", listOf("1", "2", "3", "4"), 1)
            )
            "Hard" -> listOf(
                QuizQuestion("In Priority Queue (Max Heap), find-max is:", listOf("O(1)", "O(log n)", "O(n)", "O(n log n)"), 0),
                QuizQuestion("In Priority Queue (Max Heap), insertion is:", listOf("O(1)", "O(log n)", "O(n)", "O(n log n)"), 1),
                QuizQuestion("Scheduling tasks with different urgency uses:", listOf("Simple Queue", "Deque", "Priority Queue", "Stack"), 2),
                QuizQuestion("Double Ended Queue (Deque) can act as:", listOf("Stack only", "Queue only", "Both Stack and Queue", "Neither"), 2),
                QuizQuestion("Sliding window maximum uses which data structure?", listOf("Stack", "Deque", "BST", "Linked List"), 1),
                QuizQuestion("Queue implementation: 'Front' points to:", listOf("First element", "Empty space at front", "Last element", "Middle"), 0),
                QuizQuestion("Which queue is used for Job Scheduling in OS?", listOf("LIFO", "FCFS Queue", "Stack", "B-Tree"), 1),
                QuizQuestion("Reversing a queue can be done using:", listOf("Another queue", "A stack", "Both A and B", "None"), 2)
            )
            else -> getQueueQuestions("Medium")
        }
    }

    private fun getLinkedListQuestions(difficulty: String): List<QuizQuestion> {
        return when (difficulty) {
            "Easy" -> listOf(
                QuizQuestion("What does a node in Basic Linked List contain?", listOf("Data only", "Data & Next Pointer", "Next & Prev Pointers", "Key & Value"), 1),
                QuizQuestion("Last node next pointer points to:", listOf("Head", "Middle", "Null", "Self"), 2),
                QuizQuestion("Memory for Linked List is allocated:", listOf("Contiguously", "Dynamically (non-contiguous)", "Statistically", "Strictly"), 1),
                QuizQuestion("Starting node of Linked List is called:", listOf("Start", "Base", "Head", "Root"), 2),
                QuizQuestion("In Singly Linked List, can you move backwards?", listOf("Yes", "No", "Sometimes", "Only in Java"), 1),
                QuizQuestion("Operation to search takes (Worst Case):", listOf("O(1)", "O(log n)", "O(n)", "O(n log n)"), 2),
                QuizQuestion("Accessing element at index K takes:", listOf("O(1)", "O(K)", "O(log K)", "O(N^2)"), 1),
                QuizQuestion("Benefit over Arrays?", listOf("Fast access", "Dynamic sizing", "Contiguous memory", "Binary search support"), 1)
            )
            "Medium" -> listOf(
                QuizQuestion("Doubly Linked List node contains:", listOf("Data & Next", "Data, Next & Prev", "Data only", "Next & Prev only"), 1),
                QuizQuestion("Operation to insert at beginning is:", listOf("O(1)", "O(n)", "O(log n)", "O(N^2)"), 0),
                QuizQuestion("Operation to delete from end (Singly LL):", listOf("O(1)", "O(n)", "O(log n)", "O(N^2)"), 1),
                QuizQuestion("Circular Linked List: Last node points to:", listOf("Null", "Self", "Head", "Prev"), 2),
                QuizQuestion("Deleting middle element requires:", listOf("Traversal to K-1", "Only K", "Direct access", "Head pointer only"), 0),
                QuizQuestion("Complexity of finding middle element (Slow/Fast)?", listOf("O(n^2)", "O(n log n)", "O(n)", "O(log n)"), 2),
                QuizQuestion("Reversing Singly Linked List requires how many pointers?", listOf("1", "2", "3", "0"), 2),
                QuizQuestion("Merging two sorted Linked Lists takes:", listOf("O(N*M)", "O(N+M)", "O(min(N,M))", "O(1)"), 1)
            )
            "Hard" -> listOf(
                QuizQuestion("Detecting cycle (Floyd's) takes:", listOf("O(1) space", "O(n) space", "O(log n) space", "O(n log n) space"), 0),
                QuizQuestion("Removing duplicates from Unsorted LL (Hash map) takes:", listOf("O(n^2)", "O(n)", "O(n log n)", "O(1)"), 1),
                QuizQuestion("Find intersection of two Linked Lists (length diff):", listOf("O(N+M) time", "O(N*M) time", "O(log N) time", "O(1) time"), 0),
                QuizQuestion("Flattening a multi-level Linked List uses:", listOf("Queue", "Stack/Recursion", "BST", "Array"), 1),
                QuizQuestion("Deleting a node given ONLY its pointer (non-tail):", listOf("Cannot be done", "O(n)", "O(1) (copy next)", "O(log n)"), 2),
                QuizQuestion("Swap every two nodes in LL takes:", listOf("O(n)", "O(n^2)", "O(log n)", "O(1)"), 0),
                QuizQuestion("Which LL is used for LRU Cache implementation?", listOf("Singly", "Doubly + Hash Map", "Circular", "XOR Linked List"), 1),
                QuizQuestion("Memory efficient Doubly LL uses which bitwise op?", listOf("AND", "OR", "XOR", "NOT"), 2)
            )
            else -> getLinkedListQuestions("Medium")
        }
    }

    private fun getBinaryTreeQuestions(difficulty: String): List<QuizQuestion> {
        return when (difficulty) {
            "Easy" -> listOf(
                QuizQuestion("A node can have maximum _____ children in Binary Tree.", listOf("1", "2", "Unlimited", "3"), 1),
                QuizQuestion("A node with no children is called:", listOf("Root", "Branch", "Leaf", "Stem"), 2),
                QuizQuestion("Top-most node of a tree is called:", listOf("Head", "Top", "Root", "Leader"), 2),
                QuizQuestion("Traversing Root-Left-Right is called:", listOf("Inorder", "Preorder", "Postorder", "Level-order"), 1),
                QuizQuestion("Traversing Left-Root-Right is called:", listOf("Inorder", "Preorder", "Postorder", "Level-order"), 0),
                QuizQuestion("Traversing Left-Right-Root is called:", listOf("Inorder", "Preorder", "Postorder", "Level-order"), 2),
                QuizQuestion("Maximum nodes at level 'L' (Root = 0) is:", listOf("L^2", "2^L", "L", "2L"), 1),
                QuizQuestion("What defines a tree?", listOf("Cycle-less connected graph", "Cyclic graph", "Path with no edges", "List of pointers"), 0)
            )
            "Medium" -> listOf(
                QuizQuestion("Height of Full Binary Tree with N nodes?", listOf("O(n)", "O(log n)", "O(n log n)", "O(n^2)"), 1),
                QuizQuestion("Level-order traversal uses which structure?", listOf("Stack", "Queue", "Priority Queue", "BST"), 1),
                QuizQuestion("Height-balanced tree is also known as:", listOf("Skewed Tree", "B-Tree", "AVL Tree", "Perfect Tree"), 2),
                QuizQuestion("Preorder: 1 2 4 5 3. Root is:", listOf("3", "5", "1", "4"), 2),
                QuizQuestion("Total nodes in Perfect Binary Tree height H (Root=1)?", listOf("2^H - 1", "2^H", "H^2", "2H"), 0),
                QuizQuestion("Tree traversal using recursion uses:", listOf("Implicit Stack", "Queue", "Heap", "External Array"), 0),
                QuizQuestion("Internal nodes in Binary Tree with L leaves (Full)?", listOf("L", "L-1", "L+1", "2L"), 1),
                QuizQuestion("Expression Tree: Leaves are always:", listOf("Operators", "Operands", "Roots", "Null"), 1)
            )
            "Hard" -> listOf(
                QuizQuestion("Complexity of finding Diameter of Binary Tree?", listOf("O(n^2)", "O(n)", "O(log n)", "O(n log n)"), 1),
                QuizQuestion("Constructing tree from Inorder & Preorder takes:", listOf("O(n)", "O(n log n)", "O(n^2)", "O(log n)"), 0),
                QuizQuestion("Smallest tree with height H (Root=1)?", listOf("H nodes", "2^H-1 nodes", "Log H nodes", "2H nodes"), 0),
                QuizQuestion("Morris Traversal helps traverse in:", listOf("O(n) time, O(1) space", "O(log n) time", "O(n) space", "O(n^2) time"), 0),
                QuizQuestion("Lowest Common Ancestor (LCA) in Binary Tree takes:", listOf("O(1)", "O(log n)", "O(n)", "O(H)"), 2),
                QuizQuestion("Serialized form of BinTree usually uses:", listOf("Inorder only", "Level order with Nulls", "Preorder only", "Postorder only"), 1),
                QuizQuestion("Threaded Binary Tree uses NULL pointers for:", listOf("Extra data", "Traversing without recursion", "Cycle detection", "Balanced height"), 1),
                QuizQuestion("Maximum edges in tree with N nodes?", listOf("N", "N-1", "N+1", "N^2"), 1)
            )
            else -> getBinaryTreeQuestions("Medium")
        }
    }

    private fun getBSTQuestions(difficulty: String): List<QuizQuestion> {
        return when (difficulty) {
            "Easy" -> listOf(
                QuizQuestion("In BST, left child is strictly _____ than parent.", listOf("Smaller", "Larger", "Equal", "Twice"), 0),
                QuizQuestion("In BST, right child is strictly _____ than parent.", listOf("Smaller", "Larger", "Equal", "Twice"), 1),
                QuizQuestion("Best case complexity of searching in BST?", listOf("O(1)", "O(log n)", "O(n)", "O(n log n)"), 1),
                QuizQuestion("Inorder traversal of BST gives:", listOf("Random order", "Sorted order", "Reverse order", "Level order"), 1),
                QuizQuestion("Smallest element in BST is at:", listOf("Root", "Rightmost leaf", "Leftmost leaf", "Anywhere"), 2),
                QuizQuestion("Maximum element in BST is at:", listOf("Root", "Rightmost leaf", "Leftmost leaf", "Anywhere"), 1),
                QuizQuestion("Is Root always the median in BST?", listOf("Yes", "No", "Only if balanced", "Only in AVL"), 1),
                QuizQuestion("Average time for insertion in BST?", listOf("O(1)", "O(log n)", "O(n)", "O(n^2)"), 1)
            )
            "Medium" -> listOf(
                QuizQuestion("Worst case search time in BST (skewed)?", listOf("O(log n)", "O(n)", "O(1)", "O(n^2)"), 1),
                QuizQuestion("Deletion category: node has 2 children. Replace with:", listOf("Left child", "Inorder Successor", "Leaf", "Parent"), 1),
                QuizQuestion("Search logic: if Target < Current, go:", listOf("Right", "Left", "Up", "Stay"), 1),
                QuizQuestion("Balanced BST (AVL/Red-Black) worst case search:", listOf("O(1)", "O(log n)", "O(n)", "O(n log n)"), 1),
                QuizQuestion("Minimum height of BST with N nodes?", listOf("N", "N-1", "Log2 N", "Sqrt N"), 2),
                QuizQuestion("Validating if tree is BST requires checking:", listOf("Parent only", "Root only", "Range [min, max]", "Left child only"), 2),
                QuizQuestion("Floor of key X in BST is:", listOf("Largest value <= X", "Smallest value >= X", "Parent of X", "Null"), 0),
                QuizQuestion("Ceil of key X in BST is:", listOf("Largest value <= X", "Smallest value >= X", "Parent of X", "Null"), 1)
            )
            "Hard" -> listOf(
                QuizQuestion("Kth smallest element in BST (Rank)?", listOf("O(N) traversal", "O(H) with augmented sizes", "O(1)", "O(log K)"), 1),
                QuizQuestion("Constructing BST from sorted array size N takes:", listOf("O(N log N)", "O(N)", "O(N^2)", "O(1)"), 1),
                QuizQuestion("Complexity of finding LCA in BST?", listOf("O(log N) or O(H)", "O(N)", "O(1)", "O(H^2)"), 0),
                QuizQuestion("Self-balancing property is NOT in:", listOf("AVL", "Red-Black", "Splay", "Simple BST"), 3),
                QuizQuestion("If we insert 1,2,3,4,5 into BST, it becomes:", listOf("Balanced", "Left skewed", "Right skewed", "Full Tree"), 2),
                QuizQuestion("Total ways to form BST from N keys (Catalan)?", listOf("N!", "2^N", "(1/(n+1))*(2nCn)", "N^N"), 2),
                QuizQuestion("Deletion of root in 1-node BST leaves:", listOf("Leaf", "Null", "Root", "Empty stack"), 1),
                QuizQuestion("Search complexity in Splay Tree (Amortized)?", listOf("O(log n)", "O(1)", "O(n)", "O(n log n)"), 0)
            )
            else -> getBSTQuestions("Medium")
        }
    }

    private fun getHeapQuestions(difficulty: String): List<QuizQuestion> {
        return when (difficulty) {
            "Easy" -> listOf(
                QuizQuestion("A Heap is typically implemented using:", listOf("Stack", "Queue", "Array", "Graph"), 2),
                QuizQuestion("Max-Heap: Root is _____ element.", listOf("Smallest", "Median", "Largest", "Random"), 2),
                QuizQuestion("Min-Heap: Root is _____ element.", listOf("Smallest", "Median", "Largest", "Random"), 0),
                QuizQuestion("Heap is a complete binary tree. (True/False)", listOf("True", "False"), 0),
                QuizQuestion("Complexity to find Min in Min-Heap?", listOf("O(1)", "O(log n)", "O(n)", "O(n log n)"), 0),
                QuizQuestion("Child of parent at index 'i' (0-indexed) are:", listOf("i+1, i+2", "2i, 2i+1", "2i+1, 2i+2", "i/2"), 2),
                QuizQuestion("Parent of child at index 'i' (0-indexed) is:", listOf("2i", "i*2", "(i-1)/2", "i-1"), 2),
                QuizQuestion("Binary Heap can have max _____ children per node.", listOf("Unlimited", "2", "3", "1"), 1)
            )
            "Medium" -> listOf(
                QuizQuestion("Time to insert an element into Binary Heap?", listOf("O(1)", "O(log n)", "O(n)", "O(n log n)"), 1),
                QuizQuestion("Time to delete root (extract-min/max)?", listOf("O(1)", "O(log n)", "O(n)", "O(n log n)"), 1),
                QuizQuestion("Heapify operation for one node takes:", listOf("O(1)", "O(log n)", "O(n)", "O(n^2)"), 1),
                QuizQuestion("Building a heap from array size N takes:", listOf("O(log n)", "O(N log N)", "O(N)", "O(1)"), 2),
                QuizQuestion("Priority Queue is often implemented as:", listOf("Stack", "Queue", "Binary Heap", "Hash Map"), 2),
                QuizQuestion("Heapsort time complexity (Worst Case)?", listOf("O(n^2)", "O(n log n)", "O(n)", "O(log n)"), 1),
                QuizQuestion("In Max-Heap, every parent is _____ than children.", listOf("Smaller", "Larger or Equal", "Equal", "Perfect"), 1),
                QuizQuestion("Adding to Heap: new element starts at:", listOf("Root", "Middle", "Next available leaf", "Null"), 2)
            )
            "Hard" -> listOf(
                QuizQuestion("Complexity of finding Kth largest using Max-Heap?", listOf("O(N + K log N)", "O(K log N)", "O(N log K)", "O(N^2)"), 0),
                QuizQuestion("Decrease-key in Binary Heap takes:", listOf("O(1)", "O(log n)", "O(n)", "O(1.5)"), 1),
                QuizQuestion("Decrease-key in Fibonacci Heap takes:", listOf("O(og n)", "O(1) amortized", "O(n)", "O(log log n)"), 1),
                QuizQuestion("Merging two Binary Heaps takes:", listOf("O(1)", "O(log n)", "O(N+M)", "O(N*M)"), 2),
                QuizQuestion("Heapsort is an 'In-place' sort. (True/False)", listOf("True", "False"), 0),
                QuizQuestion("Is Heapsort 'Stable'?", listOf("Yes", "No"), 1),
                QuizQuestion("Priority queue 'peek' takes:", listOf("O(1)", "O(log n)", "O(n)"), 0),
                QuizQuestion("Space complexity of Heap size N?", listOf("O(1)", "O(N)", "O(log N)"), 1)
            )
            else -> getHeapQuestions("Medium")
        }
    }

    private fun getGraphQuestions(difficulty: String): List<QuizQuestion> {
        return when (difficulty) {
            "Easy" -> listOf(
                QuizQuestion("Graph consists of Vertices and _____.", listOf("Leaves", "Branches", "Edges", "Roots"), 2),
                QuizQuestion("BFS stands for:", listOf("Better First Search", "Breadth First Search", "Base First Search", "Binary First Search"), 1),
                QuizQuestion("DFS stands for:", listOf("Depth First Search", "Double First Search", "Direct First Search", "Detailed First Search"), 0),
                QuizQuestion("Graph with directions on edges is called:", listOf("Undirected", "Tree", "Directed Graph (Digraph)", "Loop"), 2),
                QuizQuestion("An edge connecting a node to itself is:", listOf("Path", "Cycle", "Self-loop", "Bridge"), 2),
                QuizQuestion("Adjacency Matrix uses _____ of memory (N nodes).", listOf("O(N)", "O(N^2)", "O(N+E)", "O(E)"), 1),
                QuizQuestion("Tree is a graph without _____.", listOf("Nodes", "Edges", "Cycles", "Roots"), 2),
                QuizQuestion("BFS uses which data structure?", listOf("Stack", "Queue", "BST", "Heap"), 1)
            )
            "Medium" -> listOf(
                QuizQuestion("DFS uses which data structure?", listOf("Stack", "Queue", "BST", "List"), 0),
                QuizQuestion("Connectivity: Adjacency List uses _____ memory.", listOf("O(N^2)", "O(V+E)", "O(E^2)", "O(1)"), 1),
                QuizQuestion("Detecting cycle in Directed Graph uses:", listOf("BFS", "DFS with recursion stack", "Heap", "Sorted array"), 1),
                QuizQuestion("Shortest path in unweighted graph is found by:", listOf("DFS", "Dijkstra", "BFS", "Prim"), 2),
                QuizQuestion("A 'Complete Graph' with N nodes has _____ edges.", listOf("N", "N-1", "N(N-1)/2", "N^2"), 2),
                QuizQuestion("A graph's degree of a node is:", listOf("Total nodes", "Total edges connected", "Weight of node", "Level"), 1),
                QuizQuestion("Topological Sort is for which graph type?", listOf("Undirected", "Tree", "DAG (Directed Acyclic Graph)", "Cyclic Graph"), 2),
                QuizQuestion("Dijkstra's Algorithm finds:", listOf("MST", "Shortest path from source", "Cycles", "Max flow"), 1)
            )
            "Hard" -> listOf(
                QuizQuestion("Dijkstra's with Binary Heap takes:", listOf("O(E log V)", "O(V^2)", "O(E+V)", "O(E*V)"), 0),
                QuizQuestion("Bellman-Ford is used for graphs with:", listOf("Only positive weights", "Negative weights", "Cycles only", "Multiple sources"), 1),
                QuizQuestion("Prim's and Kruskal's find:", listOf("Shortest path", "Max Flow", "Minimum Spanning Tree (MST)", "Hamiltonian Path"), 2),
                QuizQuestion("Time to find all cycles in graph (Worst Case)?", listOf("Polynomial", "Exponential", "Linear", "Logarithmic"), 1),
                QuizQuestion("Strongly Connected Components are found by:", listOf("Tarjan's or Kosaraju's", "Dijkstra", "BFS", "Binary Search"), 0),
                QuizQuestion("Minimum edges to connect N nodes?", listOf("N", "N-1", "N(N-1)/2", "1"), 1),
                QuizQuestion("Handshaking Lemma: Sum of degrees =", listOf("V", "E", "2E", "E^2"), 2),
                QuizQuestion("Network Flow problems use:", listOf("BFS/DFS only", "Ford-Fulkerson", "Kruskal", "A*"), 1)
            )
            else -> getGraphQuestions("Medium")
        }
    }

    private fun getHashTableQuestions(difficulty: String): List<QuizQuestion> {
        return when (difficulty) {
            "Easy" -> listOf(
                QuizQuestion("Hash Table maps Keys to _____.", listOf("Indices (Buckets)", "Roots", "Nodes", "Edges"), 0),
                QuizQuestion("Mapping function is called:", listOf("Hash Function", "Map Function", "Key Function", "Index Function"), 0),
                QuizQuestion("Two keys mapping to same index is a:", listOf("Connection", "Collision", "Union", "Error"), 1),
                QuizQuestion("Advantage of Hash Table?", listOf("Sorted order", "O(1) average access", "Memory efficiency", "Hierarchical data"), 1),
                QuizQuestion("Collision handling with lists is called:", listOf("Open Addressing", "Chaining", "Linear Probing", "Rehashing"), 1),
                QuizQuestion("Worst case search complexity in Hash Table?", listOf("O(1)", "O(log n)", "O(n)", "O(n^2)"), 2),
                QuizQuestion("Load factor is defined as:", listOf("Keys / Buckets", "Empty / Total", "Deleted / New", "Buckets / Keys"), 0),
                QuizQuestion("Dictionary in Python is essentially a:", listOf("List", "Tree", "Hash Table", "Stack"), 2)
            )
            "Medium" -> listOf(
                QuizQuestion("Collision handling: check next available slot:", listOf("Chaining", "Linear Probing", "Double Hashing", "BST"), 1),
                QuizQuestion("Good Hash Function should provide:", listOf("Sequential indices", "Uniform distribution", "Constant index", "Minimum nulls"), 1),
                QuizQuestion("Rehashing happens when:", listOf("Table is empty", "Collision occurs", "Load factor exceeds limit", "Searching takes too long"), 2),
                QuizQuestion("Double Hashing uses _____ hash functions.", listOf("1", "2", "3", "0"), 1),
                QuizQuestion("Quadratic Probing avoids:", listOf("Primary Clustering", "Secondary Clustering", "Collisions", "Overflow"), 0),
                QuizQuestion("Space complexity of Hash Table size M with N keys?", listOf("O(N)", "O(M)", "O(N+M)", "O(N*M)"), 2),
                QuizQuestion("Perfect Hashing results in how many collisions?", listOf("0", "1", "Log N", "N"), 0),
                QuizQuestion("Deleting from Open Addressing requires:", listOf("Freeing slot", "Marking as 'Deleted/Tombstone'", "Shifting array", "Zeroing"), 1)
            )
            "Hard" -> listOf(
                QuizQuestion("Expected time for N insertions in Hash Table?", listOf("O(1)", "O(N)", "O(N^2)", "O(log N)"), 1),
                QuizQuestion("Birthday Paradox helps estimate probability of:", listOf("Table full", "Collision", "Wrong key", "Memory error"), 1),
                QuizQuestion("Universal Hashing uses a:", listOf("Large table", "Randomly picked hash function", "Prime table size", "Fixed index"), 1),
                QuizQuestion("Cuckoo Hashing uses _____ tables.", listOf("1", "2", "Unlimited", "3"), 1),
                QuizQuestion("Hashing strings: Polynomial Rolling Hash is used for:", listOf("Searching substrings", "Sorting strings", "Concatenation", "Lowercasing"), 0),
                QuizQuestion("Consistent Hashing is mainly used in:", listOf("Single PC", "Distributed Systems/Caching", "Compilers", "Databases"), 1),
                QuizQuestion("Amortized time for Resizing Hash Table?", listOf("O(1)", "O(N)", "O(log N)", "O(1.5)"), 0),
                QuizQuestion("Bloom Filter property:", listOf("No false positives", "No false negatives", "Checks if K in S exactly", "Uses 1 hash function"), 1)
            )
            else -> getHashTableQuestions("Medium")
        }
    }

    private fun getSortingQuestions(difficulty: String): List<QuizQuestion> {
        return when (difficulty) {
            "Easy" -> listOf(
                QuizQuestion("Which sorting algorithm compares adjacent elements and swaps?", listOf("Selection Sort", "Bubble Sort", "Merge Sort", "Quick Sort"), 1),
                QuizQuestion("Best case complexity of Bubble Sort (Optimized)?", listOf("O(n^2)", "O(n log n)", "O(n)", "O(1)"), 2),
                QuizQuestion("Algorithm: 'Find smallest, move to front' is:", listOf("Insertion Sort", "Selection Sort", "Bubble Sort", "Quick Sort"), 1),
                QuizQuestion("Insertion Sort is efficient for _____ arrays.", listOf("Large", "Random", "Small or nearly sorted", "Reversed"), 2),
                QuizQuestion("Which uses Divide and Conquer?", listOf("Bubble Sort", "Insertion Sort", "Merge Sort", "Selection Sort"), 2),
                QuizQuestion("Time complexity of Selection Sort always?", listOf("O(n log n)", "O(n)", "O(n^2)", "O(log n)"), 2),
                QuizQuestion("Algorithm using 'Pivot' is:", listOf("Merge Sort", "Quick Sort", "Radix Sort", "Shell Sort"), 1),
                QuizQuestion("Stable sort: equal elements keep their:", listOf("Value", "Original relative order", "Pointer", "Memory"), 1)
            )
            "Medium" -> listOf(
                QuizQuestion("Worst case of Quick Sort happens with:", listOf("Sorted array", "Random array", "Distinct elements", "Balanced pivot"), 0),
                QuizQuestion("Worst case complexity of Merge Sort?", listOf("O(n^2)", "O(n log n)", "O(n)", "O(log n)"), 1),
                QuizQuestion("Quick Sort average case complexity?", listOf("O(n log n)", "O(n^2)", "O(n)", "O(1)"), 0),
                QuizQuestion("Merge Sort space complexity?", listOf("O(1)", "O(n)", "O(log n)", "O(N^2)"), 1),
                QuizQuestion("Heap Sort time complexity?", listOf("O(n^2)", "O(n log n)", "O(n)", "O(n log k)"), 1),
                QuizQuestion("Which is 'In-place' sorting?", listOf("Merge Sort", "Quick Sort", "Both A and B", "Neither"), 1),
                QuizQuestion("Insertion Sort worst case complexity?", listOf("O(n)", "O(n log n)", "O(n^2)", "O(1)"), 2),
                QuizQuestion("Sorting N elements using comparisons must take at least:", listOf("O(n)", "O(n log n)", "O(log n)", "O(n^2)"), 1)
            )
            "Hard" -> listOf(
                QuizQuestion("Worst case of Quick Sort with Median-of-Three pivot?", listOf("O(n^2)", "O(n log n)", "O(n)", "O(n^3)"), 0),
                QuizQuestion("Sorting algorithm used in Java's Arrays.sort (Objects)?", listOf("Merge Sort (TimSort)", "Quick Sort", "Heap Sort", "Insertion Sort"), 0),
                QuizQuestion("Counting Sort is an example of _____ sort.", listOf("Comparison", "Non-comparison", "In-place", "Unstable"), 1),
                QuizQuestion("Complexity of Counting Sort with range K?", listOf("O(n log n)", "O(n+k)", "O(K^2)", "O(n*k)"), 1),
                QuizQuestion("Radix Sort uses which internal stable sort?", listOf("Bubble Sort", "Counting Sort", "Merge Sort", "Quick Sort"), 1),
                QuizQuestion("External Sorting is used for:", listOf("Small datasets", "Data larger than RAM", "Linked Lists", "Graphs"), 1),
                QuizQuestion("A 'Stable' version of Quick Sort is:", listOf("Default Quicksort", "Only with extra space", "Selection Sort", "Not possible"), 1),
                QuizQuestion("Bucket Sort is best when data is distributed:", listOf("Uniformly", "Exponentially", "Randomly", "Not distributed"), 0)
            )
            else -> getSortingQuestions("Medium")
        }
    }

    private fun getSearchingQuestions(difficulty: String): List<QuizQuestion> {
        return when (difficulty) {
            "Easy" -> listOf(
                QuizQuestion("Linear Search worst case complexity?", listOf("O(1)", "O(n)", "O(log n)", "O(n^2)"), 1),
                QuizQuestion("Binary Search requires the array to be:", listOf("Empty", "Sorted", "Large", "Unsorted"), 1),
                QuizQuestion("Searching for 5 in [1, 2, 5, 8, 10] (Binary Search):", listOf("Found in 3 steps", "Not found", "Found in 1 step", "Error"), 2),
                QuizQuestion("Complexity of finding an element in unsorted array?", listOf("O(n)", "O(1)", "O(log n)", "O(n log n)"), 0),
                QuizQuestion("Does Binary Search work on Linked Lists efficiently?", listOf("Yes", "No (cannot index O(1))", "Sometimes", "Only in Java"), 1),
                QuizQuestion("Worst case of Binary Search?", listOf("O(log n)", "O(n)", "O(1)", "O(n^2)"), 0),
                QuizQuestion("Searching 'Head' node in Linked List is:", listOf("O(1)", "O(n)", "O(log n)"), 0),
                QuizQuestion("Recursive Binary Search uses extra space for:", listOf("Data", "Pointers", "Recursion Stack", "Nothing"), 2)
            )
            "Medium" -> listOf(
                QuizQuestion("Binary Search middle index calculation usually set as:", listOf("(L+R)/2", "L + (R-L)/2 (avoids overflow)", "R/2", "L+R"), 1),
                QuizQuestion("Ternary Search splits searching range into _____ parts.", listOf("2", "3", "4", "Unlimited"), 1),
                QuizQuestion("Complexity of Ternary Search?", listOf("O(log2 n)", "O(log3 n)", "O(n)", "O(n log n)"), 1),
                QuizQuestion("Exponential Search is good for _____ arrays.", listOf("Small", "Unbounded or very large", "Circular", "Empty"), 1),
                QuizQuestion("Jump Search: the optimal jump step is set as:", listOf("SQRT(N)", "N/2", "Log N", "10"), 0),
                QuizQuestion("Index of element not in array using BS returns:", listOf("0", "N", "-1 or insertion point", "Error"), 2),
                QuizQuestion("Searching in a 2D sorted matrix (step-wise) takes:", listOf("O(N*M)", "O(N+M)", "O(log N*M)", "O(1)"), 1),
                QuizQuestion("Interpolation Search is best for data distributed:", listOf("Uniformly", "Randomly", "Sortedly", "Unsortedly"), 0)
            )
            "Hard" -> listOf(
                QuizQuestion("Complexity of Interpolation Search on uniform data?", listOf("O(log n)", "O(log log n)", "O(1)", "O(n)"), 1),
                QuizQuestion("Searching in rotated sorted array takes:", listOf("O(n)", "O(log n)", "O(n^2)", "O(1)"), 1),
                QuizQuestion("Find first and last occurrence of X in O(log n) using:", listOf("Linear Search", "Binary Search twice", "Hashing", "Merge Sort"), 1),
                QuizQuestion("Finding peak element in unsorted array using BS takes:", listOf("O(n)", "O(log n)", "O(1)", "O(n log n)"), 1),
                QuizQuestion("Searching in Infinite Stream uses:", listOf("Linear Search", "Exponential Search", "Binary Search", "All of these"), 1),
                QuizQuestion("Binary Search on nearly sorted array (shifted by 1):", listOf("O(n)", "O(log n)", "O(1)"), 1),
                QuizQuestion("Kth smallest element in unsorted array (QuickSelect):", listOf("O(n^2)", "O(n log n)", "O(n) average", "O(1)"), 2),
                QuizQuestion("Space complexity of Iterative Binary Search?", listOf("O(1)", "O(log n)", "O(n)", "O(N^2)"), 0)
            )
            else -> getSearchingQuestions("Medium")
        }
    }

    private fun getGenericQuestions(topic: String, difficulty: String): List<QuizQuestion> {
        return List(8) { index ->
            QuizQuestion(
                "$topic Question ${index + 1} ($difficulty)",
                listOf("Option A", "Option B", "Option C", "Option D"),
                0
            )
        }
    }
}
