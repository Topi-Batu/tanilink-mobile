package com.topibatu.tanilink.View

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ProductDetailPage(navController: NavController) {
    Scaffold(
        topBar = {
            TopBar(navController)
        },
        bottomBar = {
            BottomBar()
        }
    ) {
        Column(
            modifier = Modifier
                .padding(top = 64.dp, start = 24.dp, end = 24.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
//          Image
            Image(
                painter = rememberAsyncImagePainter("https://assetsio.reedpopcdn.com/hu-tao-genshin.jpg?width=1200&height=1200&fit=crop&quality=100&format=png&enable=upscale&auto=webp"),
                contentDescription = "avatar",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(160.dp)
                    .border(2.dp, Color.Gray)
            )
            Spacer(modifier = Modifier.height(36.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
//                  Nama Toko
                    Text(text = "Shop Name")
//                  Harga
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Rp.12.000", fontWeight = FontWeight.Bold)
                }
                Button(onClick = { /*TODO Belum Diisi*/ }) {
                    Text(text = "Chat")
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            Column (modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.Start){
//          Description
                Text(text = "Description", fontWeight = FontWeight.Bold)
                Text(text = "Lorem Ipsum Lorem Ipsum Lorem Ipsum Lorem Ipsum Lorem Ipsum " +
                        "Lorem Ipsum Lorem Ipsum Lorem Ipsum Lorem Ipsum Lorem Ipsum " +
                        "Lorem Ipsum Lorem Ipsum Lorem Ipsum Lorem Ipsum ")
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
        Icon(
            Icons.Filled.ArrowBack, "Back", modifier = Modifier
                .size(38.dp)
                .clickable {
                    navController.popBackStack("main", false)
                })
        // Name
        Spacer(modifier = Modifier.width(24.dp))
        Text("Product Detail", fontWeight = FontWeight.Bold, fontSize = 24.sp)
    }
}

@Composable
private fun BottomBar() {
    data class NavigationItem(
        val text: String,
        val icon: ImageVector
    )
    var selectedItem by remember { mutableIntStateOf(0) }
    val items = listOf(
        NavigationItem("Home", Icons.Filled.Home),
        NavigationItem("Chat", Icons.Filled.Email),
        NavigationItem("Profile", Icons.Filled.Person)
    )
    NavigationBar {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.text) },
                label = { Text(item.text) },
                selected = selectedItem == index,
                onClick = { selectedItem = index }
            )
        }
    }
}

