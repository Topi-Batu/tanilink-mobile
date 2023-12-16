@file:OptIn(ExperimentalMaterial3Api::class)

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.topibatu.tanilink.View.components.TopBar

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun CategoryListPage(navController: NavController){
    Scaffold (
        topBar = {
            TopBar(navController, "Category List")
        },
    ){
        LazyColumn(modifier = Modifier.padding(top = 62.dp)) {
            items(10) {
                Row(
                    modifier = Modifier
                        .padding(top = 10.dp, start = 24.dp, end = 24.dp)
                        .clickable {
                            navController.navigate("product_detail")
                        }
                ) {
                    Image(
                        painter = rememberAsyncImagePainter("https://assetsio.reedpopcdn.com/hu-tao-genshin.jpg?width=1200&height=1200&fit=crop&quality=100&format=png&enable=upscale&auto=webp"),
                        contentDescription = "avatar",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(160.dp)
                            .clip(RoundedCornerShape(18.dp))
                            .border(2.dp, Color.Gray, RoundedCornerShape(18.dp))
                    )
                    Spacer(modifier = Modifier.width(48.dp))
                    Column(modifier = Modifier.height(160.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                        Text(text = "Product Name")
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = "Rp.12.000", fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = { /*TODO*/ }) {
                            Text(text = "Buy")
                        }
                    }
                }
            }
        }
    }
}


