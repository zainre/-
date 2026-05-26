package com.example.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query
import com.squareup.moshi.Moshi
import com.squareup.moshi.JsonClass
import com.example.BuildConfig

// Models
data class Message(val text: String, val isUser: Boolean)

// Moshi Models for Gemini
@JsonClass(generateAdapter = true)
data class GenerateContentRequest(val contents: List<Content>)

@JsonClass(generateAdapter = true)
data class Content(val parts: List<Part>)

@JsonClass(generateAdapter = true)
data class Part(val text: String)

@JsonClass(generateAdapter = true)
data class GenerateContentResponse(val candidates: List<Candidate>?)

@JsonClass(generateAdapter = true)
data class Candidate(val content: Content?)

interface GeminiApiService {
    @POST("v1beta/models/gemini-3.5-flash:generateContent")
    suspend fun generateContent(
        @Query("key") apiKey: String,
        @Body request: GenerateContentRequest
    ): GenerateContentResponse
}

object RetrofitClient {
    private const val BASE_URL = "https://generativelanguage.googleapis.com/"
    
    val service: GeminiApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(GeminiApiService::class.java)
    }
}

class AIChatViewModel : ViewModel() {
    private val _messages = MutableStateFlow<List<Message>>(
        listOf(Message("مرحباً بك! أنا مساعدك الذكي 'سِراج'. مبتكر هذا التطبيق هو 'زين العابدين (zain)'. كيف يمكنني مساعدتك في تطبيق فضاء اليوم؟", false))
    )
    val messages: StateFlow<List<Message>> = _messages.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun sendMessage(text: String) {
        if (text.isBlank()) return
        
        val userMsg = Message(text, true)
        _messages.value = _messages.value + userMsg
        _isLoading.value = true

        viewModelScope.launch {
            val responseText = try {
                val apiKey = BuildConfig.GEMINI_API_KEY
                
                if (apiKey == "MY_GEMINI_API_KEY" || apiKey.isBlank()) {
                    "لتفعيل الذكاء الاصطناعي الخاص بي، يُرجى التوجه إلى قسم الأسرار (Secrets) وإضافة مفتاح GEMINI_API_KEY صحيح."
                } else {
                    val request = GenerateContentRequest(
                        contents = listOf(Content(parts = listOf(Part(text = "System Prompt: You are a smart assistant named 'Siraj' (سِراج). The creator of this app is 'Zain al-Abidin (zain)'. Answer in a highly professional and refined Arabic tone. The user says: $text"))))
                    )
                    val response = RetrofitClient.service.generateContent(apiKey, request)
                    response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text ?: "لم يتمكن الذكاء الاصطناعي من الإجابة."
                }
            } catch (e: Exception) {
                "حدث خطأ في الاتصال بالخادم: ${e.message}"
            }
            
            _messages.value = _messages.value + Message(responseText, false)
            _isLoading.value = false
        }
    }
}

@Composable
fun AIChatScreen(viewModel: AIChatViewModel = viewModel()) {
    val messages by viewModel.messages.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    var currentInput by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize()) {
        MinimalCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                "المساعد سِراج (Siraj AI)", 
                fontSize = 20.sp, 
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
        LazyColumn(
            modifier = Modifier.weight(1f).padding(horizontal = 16.dp),
            contentPadding = PaddingValues(bottom = 16.dp),
            reverseLayout = false
        ) {
            items(messages) { msg ->
                val alignment = if (msg.isUser) Alignment.End else Alignment.Start
                val containerColor = if (msg.isUser) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface
                val textColor = if (msg.isUser) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onBackground
                
                Box(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    contentAlignment = if (msg.isUser) Alignment.CenterEnd else Alignment.CenterStart
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.85f)
                            .clip(RoundedCornerShape(16.dp, 16.dp, if (msg.isUser) 4.dp else 16.dp, if (msg.isUser) 16.dp else 4.dp))
                            .background(containerColor)
                            .padding(14.dp)
                    ) {
                        Text(msg.text, color = textColor, lineHeight = 24.sp)
                    }
                }
            }
            if (isLoading) {
                item {
                    Box(modifier = Modifier.fillMaxWidth().padding(16.dp), contentAlignment = Alignment.CenterStart) {
                        CircularProgressIndicator(modifier = Modifier.size(24.dp), color = MaterialTheme.colorScheme.primary)
                    }
                }
            }
        }
        MinimalCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = currentInput,
                    onValueChange = { currentInput = it },
                    placeholder = { Text("اكتب رسالتك لأسأله...", color = MaterialTheme.colorScheme.onSurfaceVariant) },
                    modifier = Modifier.weight(1f).testTag("chat_input"),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedContainerColor = Color.Transparent
                    )
                )
                IconButton(
                    onClick = { 
                        viewModel.sendMessage(currentInput)
                        currentInput = ""
                    },
                    modifier = Modifier
                        .testTag("send_button")
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary),
                    enabled = currentInput.isNotBlank() && !isLoading
                ) {
                    Icon(
                        Icons.AutoMirrored.Filled.Send, 
                        contentDescription = "إرسال", 
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }
    }
}
