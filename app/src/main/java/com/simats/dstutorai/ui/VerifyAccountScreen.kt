package com.simats.dstutorai.ui

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.simats.dstutorai.api.RetrofitClient
import com.simats.dstutorai.api.VerifyOtpRequest
import com.simats.dstutorai.ui.theme.DeepNavy
import com.simats.dstutorai.ui.theme.FooterText
import com.simats.dstutorai.ui.theme.SubtextContent
import com.simats.dstutorai.ui.theme.TealGreen
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VerifyAccountScreen(
    email: String,
    onBackClick: () -> Unit,
    onVerifySuccess: () -> Unit,
    onResendClick: () -> Unit
) {
    val otpValues = remember { mutableStateListOf("", "", "", "", "", "") }
    var timeLeft by remember { mutableIntStateOf(60) }
    var isLoading by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = timeLeft) {
        if (timeLeft > 0) {
            delay(1000L)
            timeLeft--
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = DeepNavy
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        },
        containerColor = Color.White
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(100.dp))

                // Title
                Text(
                    text = "Verify OTP",
                    color = DeepNavy,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Subtitle
                Text(
                    text = "Enter the 6 digit OTP sent to $email",
                    color = SubtextContent,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(48.dp))

                // OTP Input Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    repeat(6) { index ->
                        TextField(
                            value = otpValues[index],
                            onValueChange = {
                                if (it.length <= 1) {
                                    otpValues[index] = it
                                }
                            },
                            modifier = Modifier
                                .size(50.dp)
                                .clip(RoundedCornerShape(8.dp)),
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color(0xFFF3F4F6),
                                unfocusedContainerColor = Color(0xFFF3F4F6),
                                disabledContainerColor = Color(0xFFF3F4F6),
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                            ),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            textStyle = LocalTextStyle.current.copy(
                                textAlign = TextAlign.Center,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = DeepNavy
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(48.dp))

                // Verify OTP Button
                Button(
                    onClick = {
                        val otp = otpValues.joinToString("")
                        if (otp.length < 6) {
                            Toast.makeText(context, "Please enter 6-digit OTP", Toast.LENGTH_SHORT).show()
                            return@Button
                        }

                        isLoading = true
                        scope.launch {
                            try {
                                val request = VerifyOtpRequest(email, otp)
                                val response = RetrofitClient.instance.verifyOtp(request)
                                if (response.isSuccessful) {
                                    val body = response.body()
                                    if (body?.message == "OTP Verified") {
                                        Toast.makeText(context, "Account verified successfully", Toast.LENGTH_SHORT).show()
                                        onVerifySuccess()
                                    } else {
                                        Toast.makeText(context, body?.message ?: "Invalid OTP", Toast.LENGTH_SHORT).show()
                                    }
                                } else {
                                    Toast.makeText(context, "Verification failed", Toast.LENGTH_SHORT).show()
                                }
                            } catch (e: Exception) {
                                Toast.makeText(context, "Connection error: ${e.message}", Toast.LENGTH_SHORT).show()
                            } finally {
                                isLoading = false
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = TealGreen,
                        contentColor = Color.White
                    ),
                    enabled = !isLoading
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                    } else {
                        Text(
                            text = "Verify OTP",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Resend OTP Timer
                if (timeLeft > 0) {
                    Text(
                        text = "Resend OTP in ${timeLeft}s",
                        color = SubtextContent,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                } else {
                    Text(
                        text = "Resend OTP",
                        color = DeepNavy,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.clickable { 
                            timeLeft = 60
                            onResendClick() 
                        }
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                // Footer
                Text(
                    text = "Powered by SIMATS",
                    color = FooterText,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(bottom = 24.dp)
                )
            }
        }
    }
}
