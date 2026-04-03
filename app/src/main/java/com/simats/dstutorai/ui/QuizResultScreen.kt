package com.simats.dstutorai.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.simats.dstutorai.ui.theme.DeepNavy
import com.simats.dstutorai.ui.theme.FooterText
import com.simats.dstutorai.ui.theme.TealGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizResultScreen(
    questions: List<QuizQuestion>,
    onBackClick: () -> Unit,
    onTryAgainClick: () -> Unit,
    onNewQuizClick: () -> Unit,
    onChatClick: () -> Unit,
    onTopicsClick: () -> Unit,
    onVisualizerClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    val correctCount = questions.count { it.selectedIndex == it.correctIndex }
    val totalQuestions = questions.size
    val percentage = if (totalQuestions > 0) (correctCount.toFloat() / totalQuestions * 100).toInt() else 0

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Quiz Generator",
                        color = DeepNavy,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = DeepNavy)
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
                .padding(horizontal = 20.dp, vertical = 10.dp)
        ) {
            // Result Header Card
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp)),
                color = Color(0xFFEFF6FF)
            ) {
                Column(
                    modifier = Modifier.padding(vertical = 30.dp, horizontal = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.EmojiEvents,
                        contentDescription = null,
                        tint = Color.Red,
                        modifier = Modifier.size(65.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = if (percentage >= 70) "Well Done! 🎓" else "Keep Practicing! 💪",
                        color = DeepNavy,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "$correctCount/$totalQuestions",
                        color = DeepNavy,
                        fontSize = 44.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "$percentage% Correct",
                        color = if (percentage >= 70) TealGreen else Color.Red,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            Text(
                text = "Review Answers",
                color = DeepNavy,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Answers Review List
            questions.forEachIndexed { index, question ->
                ReviewItem(index + 1, question)
                Spacer(modifier = Modifier.height(16.dp))
            }

            Spacer(modifier = Modifier.height(24.dp))

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
                    onClick = onTryAgainClick,
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = TealGreen)
                ) {
                    Text("Try Again", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
fun ReviewItem(number: Int, question: QuizQuestion) {
    val isCorrect = question.selectedIndex == question.correctIndex
    val backgroundColor = if (isCorrect) Color(0xFFF0FDF4) else Color(0xFFFEF2F2)
    val borderColor = if (isCorrect) Color(0xFFDCFCE7) else Color(0xFFFEE2E2)
    val iconColor = if (isCorrect) Color(0xFF22C55E) else Color(0xFFEF4444)

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, borderColor, RoundedCornerShape(12.dp)),
        shape = RoundedCornerShape(12.dp),
        color = backgroundColor
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.Top) {
                Icon(
                    imageVector = if (isCorrect) Icons.Default.CheckCircle else Icons.Default.Cancel,
                    contentDescription = null,
                    tint = iconColor,
                    modifier = Modifier.size(20.dp).padding(top = 2.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "$number. ${question.text}",
                    color = DeepNavy,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 20.sp,
                    modifier = Modifier.weight(1f)
                )
            }
            
            Spacer(modifier = Modifier.height(10.dp))
            
            Column(modifier = Modifier.padding(start = 32.dp)) {
                Row {
                    Text(text = "Your answer: ", color = Color.Gray, fontSize = 14.sp)
                    Text(
                        text = if (question.selectedIndex != -1) question.options[question.selectedIndex] else "None",
                        color = if (isCorrect) Color(0xFF22C55E) else Color(0xFFEF4444),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                if (!isCorrect) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Row {
                        Text(text = "Correct answer: ", color = Color.Gray, fontSize = 14.sp)
                        Text(
                            text = question.options[question.correctIndex],
                            color = Color(0xFF22C55E),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
