package com.simats.dstutorai.ui

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.simats.dstutorai.ui.theme.DeepNavy
import com.simats.dstutorai.ui.theme.FooterText
import com.simats.dstutorai.ui.theme.TealGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizContentScreen(
    topic: String,
    difficulty: String,
    onBackClick: () -> Unit,
    onNewQuizClick: () -> Unit,
    onSubmitClick: (List<QuizQuestion>) -> Unit,
    onChatClick: () -> Unit,
    onTopicsClick: () -> Unit,
    onVisualizerClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    // State to hold the questions and their selected answers
    val questions = remember(topic, difficulty) { 
        mutableStateListOf<QuizQuestion>().apply { 
            addAll(generateQuestions(topic, difficulty)) 
        } 
    }
    
    // Calculate answered count dynamically
    val answeredCount = questions.count { it.selectedIndex != -1 }
    val totalQuestions = questions.size
    val isAllAnswered = answeredCount == totalQuestions && totalQuestions > 0

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Quiz Generator",
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
                        selected = index == 3,
                        onClick = {
                            when (index) {
                                0 -> onChatClick()
                                1 -> onTopicsClick()
                                2 -> onVisualizerClick()
                                3 -> { /* Already here */ }
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
                .verticalScroll(rememberScrollState())
                .padding(20.dp)
        ) {
            // Status Bar
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp)),
                color = Color(0xFFEFF6FF)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = "Topic: $topic | Difficulty: $difficulty",
                        color = Color(0xFF1E40AF),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Total Questions: $totalQuestions | Answered: $answeredCount/$totalQuestions",
                        color = Color(0xFF2563EB),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (questions.isEmpty()) {
                Box(modifier = Modifier.fillMaxWidth().height(200.dp), contentAlignment = Alignment.Center) {
                    Text("No questions available for this topic/difficulty yet.", color = Color.Gray)
                }
            } else {
                // Questions List
                questions.forEachIndexed { index, question ->
                    QuestionItem(
                        number = index + 1,
                        question = question,
                        onOptionSelected = { optionIndex: Int ->
                            // Update the question state
                            questions[index] = questions[index].copy(selectedIndex = optionIndex)
                        },
                        onClearAnswer = {
                            questions[index] = questions[index].copy(selectedIndex = -1)
                        }
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedButton(
                    onClick = onNewQuizClick,
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp),
                    shape = RoundedCornerShape(8.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE5E7EB)),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = DeepNavy)
                ) {
                    Text("New Quiz", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }

                Button(
                    onClick = { onSubmitClick(questions.toList()) },
                    enabled = isAllAnswered,
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = TealGreen,
                        disabledContainerColor = Color(0xFF86D2C1).copy(alpha = 0.6f)
                    )
                ) {
                    Text(
                        "Submit Answers", 
                        fontSize = 16.sp, 
                        fontWeight = FontWeight.Bold, 
                        color = if (isAllAnswered) Color.White else Color.White.copy(alpha = 0.8f)
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

@Composable
fun QuestionItem(
    number: Int,
    question: QuizQuestion,
    onOptionSelected: (Int) -> Unit,
    onClearAnswer: () -> Unit
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Text(
                text = "$number. ${question.text}",
                color = DeepNavy,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 22.sp,
                modifier = Modifier.weight(1f)
            )
            
            if (question.selectedIndex != -1) {
                Text(
                    text = "Clear",
                    color = Color(0xFFEF4444),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .clickable { onClearAnswer() }
                        .padding(start = 8.dp)
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Color(0xFFF3F4F6), RoundedCornerShape(12.dp)),
            shape = RoundedCornerShape(12.dp),
            color = Color.White
        ) {
            Column(modifier = Modifier.padding(vertical = 8.dp)) {
                question.options.forEachIndexed { index, option ->
                    val isSelected = index == question.selectedIndex
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onOptionSelected(index) }
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (isSelected) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = TealGreen,
                                modifier = Modifier.size(20.dp)
                            )
                        } else {
                            Box(
                                modifier = Modifier
                                    .size(20.dp)
                                    .border(1.5.dp, Color(0xFFE5E7EB), CircleShape)
                            )
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text = option,
                            color = if (isSelected) DeepNavy else Color(0xFF4B5563),
                            fontSize = 15.sp,
                            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                        )
                    }
                    if (index < question.options.size - 1) {
                        HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), color = Color(0xFFF3F4F6))
                    }
                }
            }
        }
    }
}

fun generateQuestions(topic: String, difficulty: String): List<QuizQuestion> {
    return QuizRepository.generateQuestions(topic, difficulty)
}
