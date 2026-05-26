package com.example.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.automirrored.filled.Chat

import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState

@Composable
fun ReelsScreen() {
    val pagerState = rememberPagerState(pageCount = { 10 })
    
    VerticalPager(
        state = pagerState,
        modifier = Modifier.fillMaxSize().background(Color.Black)
    ) { page ->
        Box(modifier = Modifier.fillMaxSize()) {
            // Video placeholder
            Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.surfaceVariant))

            // Right side buttons
            Column(
                modifier = Modifier.align(Alignment.BottomEnd).padding(16.dp).padding(bottom = 64.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.FavoriteBorder, contentDescription = "Like", tint = Color.White, modifier = Modifier.size(32.dp))
                    Text("12k", color = Color.White, fontSize = 12.sp)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.AutoMirrored.Filled.Chat, contentDescription = "Comment", tint = Color.White, modifier = Modifier.size(32.dp))
                    Text("450", color = Color.White, fontSize = 12.sp)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.Send, contentDescription = "Share", tint = Color.White, modifier = Modifier.size(32.dp))
                    Text("Share", color = Color.White, fontSize = 12.sp)
                }
                Icon(Icons.Default.MoreVert, contentDescription = "More", tint = Color.White, modifier = Modifier.size(32.dp))
            }

            // Bottom left info
            Column(
                modifier = Modifier.align(Alignment.BottomStart).padding(16.dp).padding(bottom = 64.dp).fillMaxWidth(0.7f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(40.dp).clip(CircleShape).background(Color.Gray))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("zain_al_abidin", color = Color.White, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {},
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent, contentColor = Color.White),
                        border = BorderStroke(1.dp, Color.White),
                        modifier = Modifier.height(28.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp)
                    ) {
                        Text("متابعة", fontSize = 12.sp)
                    }
                }
                Text("نصنع المستحيل ونبني المستقبل ${page + 1}. ✨", color = Color.White, fontSize = 14.sp)
                Row(
                    verticalAlignment = Alignment.CenterVertically, 
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(Color.Black.copy(alpha = 0.5f))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Icon(Icons.Default.AutoAwesome, contentDescription = "AI Summarize", tint = Color.Yellow, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("✨ تلخيص الفيديو بواسطة سِراج", color = Color.White, fontSize = 12.sp)
                }
            }
        }
    }
}
