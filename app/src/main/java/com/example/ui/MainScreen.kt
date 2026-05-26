package com.example.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun MainScreen() {
    var selectedTab by remember { mutableStateOf(0) }
    var showAIAdvice by remember { mutableStateOf(true) }
    var showEditProfile by remember { mutableStateOf(false) }

    if (showEditProfile) {
        EditProfileScreen(onBack = { showEditProfile = false })
        return
    }
    
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onBackground
            ) {
                val items = listOf("الرئيسية" to Icons.Default.Home, "الدردشات" to Icons.Default.Chat, "سِراج" to Icons.Default.AutoAwesome, "ريلز" to Icons.Default.PlayCircle, "حسابي" to Icons.Default.AccountCircle)
                items.forEachIndexed { index, pair ->
                    NavigationBarItem(
                        icon = { Icon(pair.second, contentDescription = pair.first) },
                        label = { Text(pair.first) },
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.background,
                            selectedTextColor = MaterialTheme.colorScheme.onBackground,
                            indicatorColor = MaterialTheme.colorScheme.onBackground,
                            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                        ),
                        modifier = Modifier.testTag("nav_item_$index")
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
            when (selectedTab) {
                0 -> SocialFeedScreen(showAIAdvice = showAIAdvice)
                1 -> ChatListScreen()
                2 -> AIChatScreen()
                3 -> ReelsScreen()
                4 -> ProfileScreen(
                    onEditProfile = { showEditProfile = true },
                    showAIAdvice = showAIAdvice,
                    onToggleAIAdvice = { showAIAdvice = it }
                )
            }
        }
    }
}

@Composable
fun SocialFeedScreen(showAIAdvice: Boolean = true) {
    var posts = remember { mutableStateOf(emptyList<String>()) }
    // Simulated fetch
    LaunchedEffect(Unit) {
        delay(1500)
        posts.value = List(3) { "تصميم هادئ وعصري، يعكس الرقي والبساطة في جميع التفاصيل. 🌌✨ (منشور ${it + 1})" }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 24.dp, bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        if (showAIAdvice) {
            item {
                MinimalCard(modifier = Modifier.fillMaxWidth()) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            "💡 نصيحة سِراج لك اليوم:",
                            color = MaterialTheme.colorScheme.onBackground,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "النجاح ليس نتيجة الصدفة، بل هو مجموع التفاصيل الصغيرة التي تهتم بها يومياً.",
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                }
            }
        }
        
        item {
            Text(
                "استكشف الفضاء", 
                fontSize = 28.sp, 
                fontWeight = FontWeight.Bold, 
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
        
        if (posts.value.isEmpty()) {
            item {
                Box(modifier = Modifier.fillMaxWidth().padding(48.dp), contentAlignment = Alignment.Center) {
                    Text("لا توجد منشورات حالياً", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 18.sp)
                }
            }
        } else {
            items(posts.value.size) { index ->
                MinimalCard(
                    modifier = Modifier.fillMaxWidth().testTag("feed_card_$index")
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier.size(48.dp).clip(CircleShape).background(MaterialTheme.colorScheme.primary)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text("أحمد ناصر", color = MaterialTheme.colorScheme.onBackground, fontWeight = FontWeight.Bold)
                                Text("قبل ${index + 2} دقيقة", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp)
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            posts.value[index],
                            color = MaterialTheme.colorScheme.onBackground,
                            lineHeight = 24.sp
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                            Text("❤️ إعجاب", color = MaterialTheme.colorScheme.onSurfaceVariant, fontWeight = FontWeight.Bold)
                            Text("💬 تعليق", color = MaterialTheme.colorScheme.onSurfaceVariant, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}
