package com.topibatu.tanilink.View

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ChatPage(navController: NavController) {
//    data class Message(val sender: String, val content: String, val timestamp: Long)

    Scaffold(
        topBar = {
            TopBar(navController)
        },
        bottomBar = {
            BottomBar()
        }
    ) {
        LazyColumn(
            modifier = Modifier.padding(top = 80.dp, bottom = 80.dp)
        ) {
            items(10) {
                MessageBubble("hello", "test", true)
                MessageBubble("hello", "test", false)
            }
        }
    }
}

@Composable
private fun TopBar(navController: NavController) {
    Row(
        modifier = Modifier
            .padding(all = 16.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // back Icon
        Icon(Icons.Filled.ArrowBack, "Back", modifier = Modifier.size(38.dp).clickable {
            navController.popBackStack("main", false)
        })

        // Images
        Spacer(modifier = Modifier.width(12.dp))
        Image(
            painter = rememberAsyncImagePainter("https://assetsio.reedpopcdn.com/hu-tao-genshin.jpg?width=1200&height=1200&fit=crop&quality=100&format=png&enable=upscale&auto=webp"),
            contentDescription = "avatar",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(52.dp)
                .clip(CircleShape)
                .border(2.dp, Color.Gray, CircleShape)
        )

        // Name
        Spacer(modifier = Modifier.width(16.dp))
        Text("Hu Tao", fontWeight = FontWeight.Bold, fontSize = 24.sp)
    }
}

@Composable
private fun BottomBar() {
    Row(
        modifier = Modifier
            .padding(all = 16.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        OutlinedTextField(
            value = "Pesan",
            onValueChange = {},
            shape = RoundedCornerShape(32.dp),
            modifier = Modifier.width(LocalConfiguration.current.screenWidthDp.dp*0.75f)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Icon(imageVector = Icons.Filled.Send, contentDescription = "Send Message", modifier = Modifier.size(38.dp).clickable {  })
    }
}


@Composable
fun MessageBubble(
    sender: String,
    content: String,
    isCurrentUser: Boolean
) {
    val bubbleColor = if (isCurrentUser) Color.Cyan else Color.LightGray
    val textColor = if (isCurrentUser) Color.Black else Color.DarkGray

    Surface(
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(bubbleColor),
        tonalElevation = 4.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = if (isCurrentUser) Arrangement.Bottom else Arrangement.Top,
            horizontalAlignment = if (isCurrentUser) Alignment.End else Alignment.Start
        ) {
            if (!isCurrentUser) {
                Text(
                    text = sender,
                    style = TextStyle(color = textColor, fontWeight = FontWeight.Bold)
                )
            }
            Text(text = content, color = textColor)
        }
    }
}

