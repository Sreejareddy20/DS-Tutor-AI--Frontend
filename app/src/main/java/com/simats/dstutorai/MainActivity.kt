package com.simats.dstutorai

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.simats.dstutorai.ui.*
import com.simats.dstutorai.ui.theme.DSTutorAITheme
import com.simats.dstutorai.api.*
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "DSTutorAI",
                "DS Tutor AI Notifications",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager
            notificationManager?.createNotificationChannel(channel)
        }

        setContent {
            var isDarkMode by remember { mutableStateOf(false) }
            var notificationsEnabled by remember { mutableStateOf(true) }

            DSTutorAITheme(darkTheme = isDarkMode) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    var currentUserId by remember { mutableIntStateOf(1) }
                    var userName by remember { mutableStateOf("Sreeja") }
                    var userEmail by remember { mutableStateOf("muramreddysreeja@gmail.com") }
                    var quizResults by remember { mutableStateOf<List<QuizQuestion>>(emptyList()) }
                    val historyItems = remember { mutableStateListOf<ChatHistoryItem>() }

                    val allTopics = remember {
                        listOf(
                            Topic("Array", "Fixed-size sequential collection", Icons.Default.Inventory2, Color(0xFFE3F2FD), Color(0xFF2196F3), "Linear Data Structures"),
                            Topic("Stack", "LIFO principle structure", Icons.Default.Layers, Color(0xFFF3E5F5), Color(0xFF9C27B0), "Linear Data Structures"),
                            Topic("Queue", "FIFO principle structure", Icons.Default.ListAlt, Color(0xFFE0F2F1), Color(0xFF009688), "Linear Data Structures"),
                            Topic("Linked List", "Dynamic linear structure", Icons.Default.Link, Color(0xFFE8F5E9), Color(0xFF4CAF50), "Linear Data Structures"),
                            Topic("Binary Tree", "Nodes with max 2 children", Icons.Default.AccountTree, Color(0xFFFFFDE7), Color(0xFFFBC02D), "Hierarchical Structures"),
                            Topic("Binary Search Tree", "Sorted binary tree", Icons.Default.AccountTree, Color(0xFFFFFDE7), Color(0xFFF57F17), "Hierarchical Structures"),
                            Topic("Heap", "Complete binary tree", Icons.Default.Storage, Color(0xFFFFF3E0), Color(0xFFEF6C00), "Hierarchical Structures"),
                            Topic("Graph", "Vertices and edges", Icons.Default.Hub, Color(0xFFFCE4EC), Color(0xFFE91E63), "Non-Linear Structures"),
                            Topic("Hash Table", "Key-value mapping", Icons.Default.Tag, Color(0xFFF3E5F5), Color(0xFFE91E63), "Non-Linear Structures"),
                            Topic("Sorting", "Ordering elements", Icons.Default.BarChart, Color(0xFFE0E7FF), Color(0xFF6366F1), "Algorithms"),
                            Topic("Searching", "Finding elements", Icons.Default.Search, Color(0xFFFCE7F3), Color(0xFFEC4899), "Algorithms")
                        )
                    }

                    NavHost(navController, startDestination = "splash") {
                        composable("splash") {
                            AppSplashScreen(onTimeout = {
                                navController.navigate("onboarding1") {
                                    popUpTo("splash") { inclusive = true }
                                }
                            })
                        }

                        composable("onboarding1") { OnboardingScreen1(onSkipClick = { navController.navigate("welcome") }, onNextClick = { navController.navigate("onboarding2") }) }
                        composable("onboarding2") { OnboardingScreen2(onSkipClick = { navController.navigate("welcome") }, onNextClick = { navController.navigate("onboarding3") }) }
                        composable("onboarding3") { OnboardingScreen3(onSkipClick = { navController.navigate("welcome") }, onNextClick = { navController.navigate("onboarding4") }) }
                        composable("onboarding4") { OnboardingScreen4(onSkipClick = { navController.navigate("welcome") }, onNextClick = { navController.navigate("onboarding5") }) }
                        composable("onboarding5") { OnboardingScreen5(onSkipClick = { navController.navigate("welcome") }, onGetStartedClick = {
                            navController.navigate("welcome") {
                                popUpTo("onboarding1") { inclusive = true }
                            }
                        }) }

                        composable("welcome") {
                            WelcomeScreen(
                                onSignInClick = { navController.navigate("sign_in") },
                                onCreateAccountClick = { navController.navigate("register") }
                            )
                        }

                        composable("register") {
                            RegisterScreen(
                                onBackClick = { navController.popBackStack() },
                                onRegisterSuccess = { email: String ->
                                    navController.navigate("verify_account/$email")
                                },
                                onSignInClick = { navController.navigate("sign_in") }
                            )
                        }
                        composable("verify_account/{email}") { backStackEntry ->
                            val email = backStackEntry.arguments?.getString("email") ?: ""
                            VerifyAccountScreen(
                                email = email,
                                onBackClick = { navController.popBackStack() },
                                onVerifySuccess = { navController.navigate("verification_success") },
                                onResendClick = { /* Handle resend */ }
                            )
                        }
                        composable("verification_success") {
                            VerificationSuccessScreen(onContinueClick = {
                                navController.navigate("sign_in") {
                                    popUpTo("welcome") { inclusive = true }
                                }
                            })
                        }

                        composable("sign_in") {
                            val prefs = PreferenceManager(this@MainActivity)
                            SignInScreen(
                                onBackClick = { navController.popBackStack() },
                                onLoginSuccess = { name: String, email: String, id: Int ->
                                    userName = name
                                    userEmail = email
                                    currentUserId = id
                                    
                                    if (prefs.isPremiumUser()) {
                                        navController.navigate("chat") {
                                            popUpTo("welcome") { inclusive = true }
                                        }
                                    } else {
                                        navController.navigate("subscription") {
                                            popUpTo("welcome") { inclusive = true }
                                        }
                                    }
                                },
                                onForgotPasswordClick = { navController.navigate("forgot_password") },
                                onRegisterClick = { navController.navigate("register") }
                            )
                        }

                        composable("subscription") {
                            val prefs = remember { PreferenceManager(this@MainActivity) }
                            val billingManager = remember {
                                BillingManager(
                                    context = this@MainActivity,
                                    activity = this@MainActivity,
                                    onPurchaseSuccess = {
                                        prefs.setPremiumUser(true)
                                        navController.navigate("chat") {
                                            popUpTo("subscription") { inclusive = true }
                                        }
                                    }
                                )
                            }
                            
                            SubscriptionScreen(
                                onStartPremiumClick = { billingManager.startPurchase() },
                                onMaybeLaterClick = {
                                    navController.navigate("chat") {
                                        popUpTo("subscription") { inclusive = true }
                                    }
                                }
                            )
                        }
                        composable("forgot_password") {
                            ForgotPasswordScreen(
                                onBackClick = { navController.popBackStack() },
                                onSendResetLinkClick = { email: String ->
                                    navController.navigate("verify_reset_otp/$email")
                                }
                            )
                        }
                        composable("verify_reset_otp/{email}") { backStackEntry ->
                            val email = backStackEntry.arguments?.getString("email") ?: ""
                            VerifyAccountScreen(
                                email = email,
                                onBackClick = { navController.popBackStack() },
                                onVerifySuccess = { navController.navigate("reset_password/$email") },
                                onResendClick = { /* Handle resend */ }
                            )
                        }
                        composable("reset_password/{email}") { backStackEntry ->
                            val email = backStackEntry.arguments?.getString("email") ?: ""
                            ResetPasswordScreen(
                                email = email,
                                onBackClick = { navController.popBackStack() },
                                onResetSuccess = {
                                    navController.navigate("sign_in") {
                                        popUpTo("welcome") { inclusive = true }
                                    }
                                },
                                onBackToLoginClick = { navController.navigate("sign_in") }
                            )
                        }

                        composable(
                            route = "chat?initialMessage={initialMessage}&initialAnswer={initialAnswer}",
                            arguments = listOf(
                                navArgument("initialMessage") { 
                                    type = NavType.StringType
                                    nullable = true
                                    defaultValue = null
                                },
                                navArgument("initialAnswer") { 
                                    type = NavType.StringType
                                    nullable = true
                                    defaultValue = null
                                }
                            )
                        ) { backStackEntry ->
                            val initialMessage = backStackEntry.arguments?.getString("initialMessage")
                            val initialAnswer = backStackEntry.arguments?.getString("initialAnswer")
                            ChatScreen(
                                userId = currentUserId,
                                initialMessage = initialMessage,
                                initialAnswer = initialAnswer,
                                onProfileClick = { navController.navigate("profile") },
                                onTopicsClick = { navController.navigate("topics") },
                                onVisualizerClick = { navController.navigate("visualizer_detail/all") },
                                onPracticeClick = { navController.navigate("practice") },
                                onHistoryClick = { navController.navigate("history") },
                                onMenuClick = { navController.navigate("history") }
                            )
                        }


                        composable("topics") {
                            TopicsScreen(
                                topics = allTopics,
                                onTopicClick = { topic -> 
                                    navController.navigate("topic_detail/${topic.name}")
                                },
                                onBackClick = { navController.popBackStack() },
                                onChatClick = { navController.navigate("chat") },
                                onVisualizerClick = { navController.navigate("visualizer_detail/all") },
                                onPracticeClick = { navController.navigate("practice") },
                                onProfileClick = { navController.navigate("profile") }
                            )
                        }

                        composable("topic_detail/{topicName}") { backStackEntry ->
                            val topicName = backStackEntry.arguments?.getString("topicName") ?: ""
                            val topic = allTopics.find { it.name == topicName } ?: allTopics[0]
                            TopicDetailScreen(
                                topic = topic,
                                onBackClick = { navController.popBackStack() },
                                onVisualizeClick = { 
                                    val route = "${topicName.lowercase().replace(" ", "_")}_viz"
                                    navController.navigate(route) 
                                },
                                onCodeClick = { navController.navigate("code_generator/${topic.name}") },
                                onGenerateCodeClick = { navController.navigate("code_generator/${topic.name}") },
                                onPracticeQuizClick = { navController.navigate("practice") },
                                onAskAIClick = { 
                                    val encodedQuestion = java.net.URLEncoder.encode("Tell me more about ${topic.name}", "UTF-8")
                                    navController.navigate("chat?initialMessage=$encodedQuestion")
                                },
                                onChatClick = { navController.navigate("chat") },
                                onTopicsClick = { navController.navigate("topics") },
                                onVisualizerClick = { navController.navigate("visualizer_detail/all") },
                                onPracticeClick = { navController.navigate("practice") },
                                onProfileClick = { navController.navigate("profile") }
                            )
                        }

                        composable("visualizer_detail/{topicType}") { backStackEntry ->
                            VisualizerScreen(
                                onBackClick = { navController.popBackStack() },
                                onPlayClick = { title ->
                                    val route = when {
                                        title.contains("Array", ignoreCase = true) -> "array_viz"
                                        title.contains("Stack", ignoreCase = true) -> "stack_viz"
                                        title.contains("Queue", ignoreCase = true) -> "queue_viz"
                                        title.contains("Linked List", ignoreCase = true) -> "linked_list_viz"
                                        title.contains("Binary Tree", ignoreCase = true) -> "binary_tree_viz"
                                        title.contains("BST", ignoreCase = true) -> "bst_viz"
                                        title.contains("Heap", ignoreCase = true) -> "heap_viz"
                                        title.contains("Graph", ignoreCase = true) -> "graph_viz"
                                        title.contains("Hash Table", ignoreCase = true) -> "hash_table_viz"
                                        title.contains("Sorting", ignoreCase = true) -> "sorting_viz"
                                        title.contains("Binary Search", ignoreCase = true) -> "searching_viz"
                                        else -> null
                                    }
                                    route?.let { navController.navigate(it) }
                                },
                                onChatClick = { navController.navigate("chat") },
                                onTopicsClick = { navController.navigate("topics") },
                                onPracticeClick = { navController.navigate("practice") },
                                onProfileClick = { navController.navigate("profile") }
                            )
                        }

                        // Visualization Screens
                        composable("array_viz") { ArrayVisualizationScreen(onBackClick = { navController.popBackStack() }, onChatClick = { navController.navigate("chat") }, onTopicsClick = { navController.navigate("topics") }, onPracticeClick = { navController.navigate("practice") }, onProfileClick = { navController.navigate("profile") }) }
                        composable("stack_viz") { StackVisualizationScreen(onBackClick = { navController.popBackStack() }, onChatClick = { navController.navigate("chat") }, onTopicsClick = { navController.navigate("topics") }, onPracticeClick = { navController.navigate("practice") }, onProfileClick = { navController.navigate("profile") }) }
                        composable("queue_viz") { QueueVisualizationScreen(onBackClick = { navController.popBackStack() }, onChatClick = { navController.navigate("chat") }, onTopicsClick = { navController.navigate("topics") }, onPracticeClick = { navController.navigate("practice") }, onProfileClick = { navController.navigate("profile") }) }
                        composable("linked_list_viz") { LinkedListVisualizationScreen(onBackClick = { navController.popBackStack() }, onChatClick = { navController.navigate("chat") }, onTopicsClick = { navController.navigate("topics") }, onPracticeClick = { navController.navigate("practice") }, onProfileClick = { navController.navigate("profile") }) }
                        composable("binary_tree_viz") { BinaryTreeVisualizationScreen(onBackClick = { navController.popBackStack() }, onChatClick = { navController.navigate("chat") }, onTopicsClick = { navController.navigate("topics") }, onPracticeClick = { navController.navigate("practice") }, onProfileClick = { navController.navigate("profile") }) }
                        composable("bst_viz") { BSTVisualizationScreen(onBackClick = { navController.popBackStack() }, onChatClick = { navController.navigate("chat") }, onTopicsClick = { navController.navigate("topics") }, onPracticeClick = { navController.navigate("practice") }, onProfileClick = { navController.navigate("profile") }) }
                        composable("heap_viz") { HeapVisualizationScreen(onBackClick = { navController.popBackStack() }, onChatClick = { navController.navigate("chat") }, onTopicsClick = { navController.navigate("topics") }, onPracticeClick = { navController.navigate("practice") }, onProfileClick = { navController.navigate("profile") }) }
                        composable("graph_viz") { GraphVisualizationScreen(onBackClick = { navController.popBackStack() }, onChatClick = { navController.navigate("chat") }, onTopicsClick = { navController.navigate("topics") }, onPracticeClick = { navController.navigate("practice") }, onProfileClick = { navController.navigate("profile") }) }
                        composable("hash_table_viz") { HashTableVisualizationScreen(onBackClick = { navController.popBackStack() }, onChatClick = { navController.navigate("chat") }, onTopicsClick = { navController.navigate("topics") }, onPracticeClick = { navController.navigate("practice") }, onProfileClick = { navController.navigate("profile") }) }
                        composable("sorting_viz") { SortingVisualizationScreen(onBackClick = { navController.popBackStack() }, onChatClick = { navController.navigate("chat") }, onTopicsClick = { navController.navigate("topics") }, onPracticeClick = { navController.navigate("practice") }, onProfileClick = { navController.navigate("profile") }) }
                        composable("searching_viz") { BinarySearchVisualizationScreen(onBackClick = { navController.popBackStack() }, onChatClick = { navController.navigate("chat") }, onTopicsClick = { navController.navigate("topics") }, onPracticeClick = { navController.navigate("practice") }, onProfileClick = { navController.navigate("profile") }) }

                        composable("code_generator/{topicName}") { backStackEntry ->
                            val topicName = backStackEntry.arguments?.getString("topicName") ?: "Array"
                            CodeGeneratorScreen(
                                initialTopic = topicName,
                                onBackClick = { navController.popBackStack() },
                                onGenerateClick = { _, _ -> /* Log generation */ }
                            )
                        }

                        composable("practice") {
                            PracticeScreen(
                                onBackClick = { navController.popBackStack() },
                                onChatClick = { navController.navigate("chat") },
                                onTopicsClick = { navController.navigate("topics") },
                                onVisualizerClick = { navController.navigate("visualizer_detail/all") },
                                onProfileClick = { navController.navigate("profile") },
                                onGenerateQuizClick = { topic, difficulty -> 
                                    navController.navigate("quiz/$topic/$difficulty") 
                                }
                            )
                        }

                        composable("quiz/{topic}/{difficulty}") { backStackEntry ->
                            val topic = backStackEntry.arguments?.getString("topic") ?: "Array"
                            val difficulty = backStackEntry.arguments?.getString("difficulty") ?: "Easy"
                            QuizContentScreen(
                                topic = topic,
                                difficulty = difficulty,
                                onBackClick = { navController.popBackStack() },
                                onNewQuizClick = { navController.navigate("practice") },
                                onSubmitClick = { results ->
                                    quizResults = results
                                    navController.navigate("quiz_result")
                                },
                                onChatClick = { navController.navigate("chat") },
                                onTopicsClick = { navController.navigate("topics") },
                                onVisualizerClick = { navController.navigate("visualizer_detail/all") },
                                onProfileClick = { navController.navigate("profile") }
                            )
                        }

                        composable("quiz_result") {
                            QuizResultScreen(
                                questions = quizResults,
                                onBackClick = { navController.popBackStack() },
                                onTryAgainClick = { navController.popBackStack() },
                                onNewQuizClick = { 
                                    navController.navigate("practice") {
                                        popUpTo("practice") { inclusive = true }
                                    }
                                },
                                onChatClick = { navController.navigate("chat") },
                                onTopicsClick = { navController.navigate("topics") },
                                onVisualizerClick = { navController.navigate("visualizer_detail/all") },
                                onProfileClick = { navController.navigate("profile") }
                            )
                        }

                        composable("history") {
                            LaunchedEffect(currentUserId) {
                                try {
                                    val response = RetrofitClient.instance.getChatHistory(currentUserId)
                                    if (response.isSuccessful) {
                                        historyItems.clear()
                                        response.body()?.forEach { chat ->
                                            historyItems.add(
                                                ChatHistoryItem(
                                                    id = chat.id,
                                                    question = chat.question,
                                                    answer = chat.answer,
                                                    time = chat.createdAt ?: "Today"
                                                )
                                            )
                                        }
                                    }
                                } catch (e: Exception) {
                                    android.widget.Toast.makeText(this@MainActivity, "Could not load history", android.widget.Toast.LENGTH_SHORT).show()
                                }
                            }
                            
                            ChatHistoryScreen(
                                historyItems = historyItems,
                                onBackClick = { navController.popBackStack() },
                                onItemClick = { item ->
                                    val encodedQuestion = java.net.URLEncoder.encode(item.question, "UTF-8")
                                    val encodedAnswer = java.net.URLEncoder.encode(item.answer, "UTF-8")
                                    navController.navigate("chat?initialMessage=$encodedQuestion&initialAnswer=$encodedAnswer")
                                },
                                onDeleteItem = { item ->
                                    lifecycleScope.launch {
                                        try {
                                            RetrofitClient.instance.deleteChat(item.id)
                                            historyItems.remove(item)
                                        } catch (e: Exception) {
                                            android.widget.Toast.makeText(this@MainActivity, "Delete failed on server", android.widget.Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                },
                                onNewChatClick = {
                                    navController.navigate("chat") {
                                        popUpTo("welcome") { inclusive = false }
                                    }
                                },
                                onClearAllClick = {
                                    lifecycleScope.launch {
                                        try {
                                            RetrofitClient.instance.clearChatHistory(currentUserId)
                                            historyItems.clear()
                                        } catch (e: Exception) {
                                            android.widget.Toast.makeText(this@MainActivity, "History clear failed", android.widget.Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                }
                            )
                        }

                        composable("profile") {
                            ProfileScreen(
                                userId = currentUserId,
                                userName = userName,
                                userEmail = userEmail,
                                onBackClick = { navController.popBackStack() },
                                onChatClick = { navController.navigate("chat") },
                                onTopicsClick = { navController.navigate("topics") },
                                onVisualizerClick = { navController.navigate("visualizer_detail/all") },
                                onPracticeClick = { navController.navigate("practice") },
                                onEditProfileClick = { navController.navigate("edit_profile") },
                                onSettingsClick = { navController.navigate("settings") },
                                onSignOutClick = {
                                    navController.navigate("welcome") {
                                        popUpTo(0) { inclusive = true }
                                    }
                                }
                            )
                        }

                        composable("edit_profile") {
                            EditProfileScreen(
                                initialName = userName,
                                initialEmail = userEmail,
                                onDismiss = { navController.popBackStack() },
                                onSave = { newName, newEmail ->
                                    lifecycleScope.launch {
                                        try {
                                            val response = RetrofitClient.instance.updateProfile(
                                                UpdateProfileRequest(currentUserId, newName, newEmail)
                                            )
                                            if (response.isSuccessful && response.body()?.message == "Profile updated successfully") {
                                                userName = newName
                                                userEmail = newEmail
                                                navController.popBackStack()
                                            } else {
                                                android.widget.Toast.makeText(this@MainActivity, response.body()?.message ?: "Update failed", android.widget.Toast.LENGTH_SHORT).show()
                                            }
                                        } catch (e: Exception) {
                                            android.widget.Toast.makeText(this@MainActivity, "Server connection failed", android.widget.Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                }
                            )
                        }

                        composable("settings") {
                            SettingsScreen(
                                notificationsEnabled = notificationsEnabled,
                                onNotificationsChange = { notificationsEnabled = it },
                                onBackClick = { navController.popBackStack() },
                                onChatHistoryClick = { navController.navigate("history") },
                                onClearChatHistoryClick = {
                                    lifecycleScope.launch {
                                        try {
                                            RetrofitClient.instance.clearChatHistory(currentUserId)
                                            historyItems.clear()
                                        } catch (e: Exception) {
                                            android.widget.Toast.makeText(this@MainActivity, "History clear failed", android.widget.Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                },
                                onLogoutClick = {
                                    navController.navigate("welcome") {
                                        popUpTo(0) { inclusive = true }
                                    }
                                },
                                onAccountDeleteClick = {
                                    lifecycleScope.launch {
                                        try {
                                            RetrofitClient.instance.deleteAccount(currentUserId)
                                            navController.navigate("welcome") {
                                                popUpTo(0) { inclusive = true }
                                            }
                                        } catch (e: Exception) {
                                            android.widget.Toast.makeText(this@MainActivity, "Delete account failed", android.widget.Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
