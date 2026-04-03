package com.simats.dstutorai.api

import com.google.gson.annotations.SerializedName

// Generic Message Response
data class MessageResponse(
    @SerializedName("message") val message: String,
    @SerializedName("otp") val otp: String? = null,
    @SerializedName("name") val name: String? = null,
    @SerializedName("user_id") val userId: Int? = null
)

// UI Chat Message model
data class ChatMessage(
    val text: String,
    val isUser: Boolean,
    val fileName: String? = null
)

// Auth Requests
data class RegisterRequest(
    @SerializedName("name") val name: String,
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String
)

data class LoginRequest(
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String
)

data class VerifyOtpRequest(
    @SerializedName("email") val email: String,
    @SerializedName("otp") val otp: String
)

data class ForgotPasswordRequest(
    @SerializedName("email") val email: String
)

data class ResetPasswordRequest(
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String // This is 'new_password' in python but mapped to 'password' in json
)

// Chat
data class ChatRequest(
    @SerializedName("user_id") val userId: Int,
    @SerializedName("question") val question: String
)

data class ChatResponse(
    @SerializedName("question") val question: String,
    @SerializedName("answer") val answer: String,
    @SerializedName("created_at") val createdAt: String? = null
)

data class ChatHistory(
    @SerializedName("id") val id: Int,
    @SerializedName("question") val question: String,
    @SerializedName("answer") val answer: String,
    @SerializedName("created_at") val createdAt: String? = null
)

// Profile
data class UserProfile(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("email") val email: String
)

data class UpdateProfileRequest(
    @SerializedName("user_id") val userId: Int,
    @SerializedName("name") val name: String,
    @SerializedName("email") val email: String
)
