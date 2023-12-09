package com.topibatu.tanilink.View

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ProfilePage(navController: NavController) {
    Scaffold(
        topBar = {
            TopBar(navController)
        },
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 86.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = rememberAsyncImagePainter("https://assetsio.reedpopcdn.com/hu-tao-genshin.jpg?width=1200&height=1200&fit=crop&quality=100&format=png&enable=upscale&auto=webp"),
                contentDescription = "avatar",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(160.dp)
                    .clip(CircleShape)
                    .border(2.dp, Color.Gray, CircleShape)
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = "",
                onValueChange = {},
                label = { Text("Email") },
                visualTransformation = VisualTransformation.None,
                readOnly = true,
                shape = RoundedCornerShape(32.dp),
                modifier = Modifier.width(LocalConfiguration.current.screenWidthDp.dp * 0.75f)
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = "Name",
                onValueChange = {},
                readOnly = true,
                shape = RoundedCornerShape(32.dp),
                modifier = Modifier.width(LocalConfiguration.current.screenWidthDp.dp * 0.75f)
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = "Email",
                onValueChange = {},
                readOnly = true,
                shape = RoundedCornerShape(32.dp),
                modifier = Modifier.width(LocalConfiguration.current.screenWidthDp.dp * 0.75f)
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = "Telp. Number",
                onValueChange = {},
                readOnly = true,
                shape = RoundedCornerShape(32.dp),
                modifier = Modifier.width(LocalConfiguration.current.screenWidthDp.dp * 0.75f)
            )
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
        Icon(Icons.Filled.ArrowBack, "Back", modifier = Modifier
            .size(38.dp)
            .clickable {
                navController.popBackStack("main", false)
            })
        // Name
        Spacer(modifier = Modifier.width(24.dp))
        Text("Profile", fontWeight = FontWeight.Bold, fontSize = 24.sp)
    }
}

