package com.simats.dstutorai.ui

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.speech.RecognizerIntent
import android.speech.tts.TextToSpeech
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.simats.dstutorai.ui.theme.DeepNavy
import com.simats.dstutorai.ui.theme.TealGreen
import com.simats.dstutorai.api.ChatMessage
import com.simats.dstutorai.api.ChatRequest
import com.simats.dstutorai.api.RetrofitClient
import kotlinx.coroutines.launch
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    userId: Int,
    initialMessage: String? = null,
    initialAnswer: String? = null,
    onTopicsClick: () -> Unit,
    onVisualizerClick: () -> Unit,
    onPracticeClick: () -> Unit,
    onHistoryClick: () -> Unit,
    onMenuClick: () -> Unit,
    onProfileClick: () -> Unit,
    onSuggestionClick: (String) -> Unit = {}
) {
    var messageText by remember { mutableStateOf("") }
    var isTyping by remember { mutableStateOf(false) }
    
    val messages = remember {
        mutableStateListOf(
            ChatMessage("Welcome! I'm your Data Structures assistant. Ask me anything about DS topics like Arrays, Stacks, Queues, Trees, or Graphs!", false)
        )
    }
    
    val coroutineScope = rememberCoroutineScope()
    val listState = rememberLazyListState()
    val context = LocalContext.current
    
    var tts by remember { mutableStateOf<TextToSpeech?>(null) }
    DisposableEffect(Unit) {
        val textToSpeech = TextToSpeech(context) { status -> }
        tts = textToSpeech
        onDispose {
            textToSpeech.stop()
            textToSpeech.shutdown()
        }
    }

    fun speak(text: String) {
        tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
    }

    val handleSend: (String) -> Unit = { text ->
        if (text.isNotBlank()) {
            messages.add(ChatMessage(text, true))
            isTyping = true
            
            coroutineScope.launch {
                try {
                    val response = RetrofitClient.instance.chat(ChatRequest(userId, text))
                    isTyping = false
                    if (response.isSuccessful) {
                        val answer = response.body()?.answer ?: "I'm having trouble connecting right now."
                        messages.add(ChatMessage(answer, false))
                        speak(answer)
                    } else {
                        messages.add(ChatMessage("Sorry, the AI is overwhelmed. Please check your API key / quota.", false))
                    }
                } catch (e: Exception) {
                    isTyping = false
                    messages.add(ChatMessage("Connection error: AI took too long to respond. Please check your history in a moment.", false))
                }
            }
        }
    }

    LaunchedEffect(initialMessage, initialAnswer) {
        val decodedMessage = initialMessage?.let { java.net.URLDecoder.decode(it, "UTF-8") }
        val decodedAnswer = initialAnswer?.let { java.net.URLDecoder.decode(it, "UTF-8") }
        
        if (decodedMessage != null && messages.none { it.text == decodedMessage }) {
            messages.add(ChatMessage(decodedMessage, true))
            if (decodedAnswer != null) {
                messages.add(ChatMessage(decodedAnswer, false))
            } else {
                handleSend(decodedMessage)
            }
        }
    }

    val speechLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            val spokenText = data?.get(0) ?: ""
            if (spokenText.isNotBlank()) {
                handleSend(spokenText)
            }
        }
    }


    LaunchedEffect(messages.size, isTyping) {
        if (messages.isNotEmpty() || isTyping) {
            listState.animateScrollToItem(if (isTyping) messages.size else messages.size - 1)
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("DS Tutor AI", color = DeepNavy, fontWeight = FontWeight.Bold, fontSize = 20.sp) },
                navigationIcon = {
                    IconButton(onClick = onMenuClick) { Icon(Icons.Default.Menu, contentDescription = "History", tint = DeepNavy) }
                },
                actions = {
                    IconButton(onClick = onProfileClick) { Icon(Icons.Outlined.Person, contentDescription = "Profile", tint = DeepNavy) }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White)
            )
        },
        bottomBar = {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextField(
                        value = messageText,
                        onValueChange = { messageText = it },
                        modifier = Modifier.weight(1f).heightIn(min = 52.dp).clip(RoundedCornerShape(12.dp)),
                        placeholder = { Text("Ask about data structures...", color = Color.Gray, fontSize = 14.sp) },
                        trailingIcon = {
                                IconButton(onClick = { 
                                    val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                                        putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                                        putExtra(RecognizerIntent.EXTRA_PROMPT, "Ask about Data Structures...")
                                    }
                                    try { speechLauncher.launch(intent) } catch (e: Exception) {}
                                }) {
                                    Icon(Icons.Default.Mic, contentDescription = "Mic", tint = Color.Gray)
                                }
                        },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color(0xFFF3F4F6),
                            unfocusedContainerColor = Color(0xFFF3F4F6),
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        )
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    FloatingActionButton(
                        onClick = {
                            if (messageText.isNotBlank()) {
                                handleSend(messageText)
                                messageText = ""
                            }
                        },
                        containerColor = TealGreen,
                        contentColor = Color.White,
                        shape = CircleShape,
                        modifier = Modifier.size(48.dp)
                    ) {
                        Icon(Icons.Default.Send, contentDescription = "Send")
                    }
                }
                
                NavigationBar(containerColor = Color.White, tonalElevation = 8.dp) {
                    val navItems = listOf(
                        "Chat" to Icons.Default.Chat,
                        "Topics" to Icons.Default.MenuBook,
                        "Visualizer" to Icons.Default.Visibility,
                        "Practice" to Icons.Default.School,
                        "Profile" to Icons.Default.Person
                    )
                    navItems.forEachIndexed { index, item ->
                        NavigationBarItem(
                            icon = { Icon(item.second, contentDescription = item.first) },
                            label = { Text(item.first, fontSize = 12.sp) },
                            selected = index == 0,
                            onClick = { 
                                when (index) {
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
            }
        },
        containerColor = Color.White
    ) { innerPadding ->
        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize().padding(innerPadding).padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            items(messages) { message ->
                ChatBubble(message)
            }
            if (isTyping) {
                item {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp, bottomEnd = 16.dp))
                            .background(Color(0xFFF3F4F6))
                            .padding(12.dp)
                    ) {
                        Text("Thinking...", color = Color.Gray, fontSize = 14.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun ChatBubble(message: ChatMessage) {
    val isUser = message.isUser
    val parts = message.text.split("```")
    
    Column(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        horizontalAlignment = if (isUser) Alignment.End else Alignment.Start
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = 320.dp)
                .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp, 
                    bottomStart = if (isUser) 16.dp else 0.dp, 
                    bottomEnd = if (isUser) 0.dp else 16.dp))
                .background(if (isUser) TealGreen.copy(alpha = 0.1f) else Color(0xFFF3F4F6))
                .padding(12.dp)
        ) {
            parts.forEachIndexed { index, part ->
                if (index % 2 == 1) { 
                    val lines = part.trim().split("\n")
                    val language = if (lines.firstOrNull()?.contains(" ") == false) lines.first() else "CODE"
                    val codeContent = if (lines.size > 1 && !lines.first().contains(" ")) lines.drop(1).joinToString("\n") else part
                    CodeBlock(codeContent.trim(), language) 
                } else if (part.isNotBlank()) {
                    Text(text = part.trim(), color = DeepNavy, fontSize = 15.sp, lineHeight = 22.sp)
                }
            }
        }
    }
}

@Composable
fun CodeBlock(code: String, language: String) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp).clip(RoundedCornerShape(8.dp)).background(Color(0xFF1E1E1E)).padding(12.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text(text = language.uppercase(Locale.ROOT), color = Color.Gray, fontSize = 10.sp, fontWeight = FontWeight.Bold)
            Icon(imageVector = Icons.Default.ContentCopy, contentDescription = "Copy", tint = Color.Gray, modifier = Modifier.size(16.dp))
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = code, color = Color(0xFF9CDCFE), fontSize = 13.sp, fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace, lineHeight = 20.sp)
    }
}
