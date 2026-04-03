package com.simats.dstutorai.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {

    @POST("register")
    suspend fun register(
        @Body registerRequest: RegisterRequest
    ): Response<MessageResponse>


    @POST("login")
    suspend fun login(
        @Body loginRequest: LoginRequest
    ): Response<MessageResponse>


    @POST("verify_otp")
    suspend fun verifyOtp(
        @Body verifyOtpRequest: VerifyOtpRequest
    ): Response<MessageResponse>


    @POST("forgot_password")
    suspend fun forgotPassword(
        @Body forgotPasswordRequest: ForgotPasswordRequest
    ): Response<MessageResponse>


    @POST("verify_reset_otp")
    suspend fun verifyResetOtp(
        @Body verifyOtpRequest: VerifyOtpRequest
    ): Response<MessageResponse>


    @POST("reset_password")
    suspend fun resetPassword(
        @Body resetPasswordRequest: ResetPasswordRequest
    ): Response<MessageResponse>


    @POST("chat")
    suspend fun chat(
        @Body chatRequest: ChatRequest
    ): Response<ChatResponse>


    // NEW API FOR CHAT HISTORY
    @GET("chat_history/{user_id}")
    suspend fun getChatHistory(
        @Path("user_id") userId: Int
    ): Response<List<ChatHistory>>


    @GET("profile/{user_id}")
    suspend fun getProfile(
        @Path("user_id") userId: Int
    ): Response<UserProfile>


    @POST("update_profile")
    suspend fun updateProfile(
        @Body updateProfileRequest: UpdateProfileRequest
    ): Response<MessageResponse>


    // NEW: DELETE CHAT HISTORY
    @retrofit2.http.DELETE("clear_chat_history/{user_id}")
    suspend fun clearChatHistory(
        @Path("user_id") userId: Int
    ): Response<MessageResponse>


    @retrofit2.http.DELETE("delete_chat/{chat_id}")
    suspend fun deleteChat(
        @Path("chat_id") chatId: Int
    ): Response<MessageResponse>


    @retrofit2.http.DELETE("delete_account/{user_id}")
    suspend fun deleteAccount(
        @Path("user_id") userId: Int
    ): Response<MessageResponse>

}
