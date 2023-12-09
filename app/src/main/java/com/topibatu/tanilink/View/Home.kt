package com.topibatu.tanilink.View

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.material3.IconButton as IconButton

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePage(navController: NavController) {
    // TODO: FIX SCROLL, ENHANCE BOTTOM NAV, SEPARATE TOP AND BOTTOM NAV INTO DIFFERENT FILES

    Scaffold(
        topBar = {
            TopBar()
        },
        bottomBar = {
            BottomBar()
        }
    ) {
        val images = listOf(
            "https://firebasestorage.googleapis.com/v0/b/topibatu-2a076.appspot.com/o/assets%2Fbanner1.png?alt=media&token=985a3e0b-8773-4c84-afae-bcd7c01e695d",
            "https://firebasestorage.googleapis.com/v0/b/topibatu-2a076.appspot.com/o/assets%2Fbanner2.png?alt=media&token=7dfaf44f-225b-41c0-9c46-aa3f267d561d",
            "https://firebasestorage.googleapis.com/v0/b/topibatu-2a076.appspot.com/o/assets%2Fbanner3.png?alt=media&token=70e0d942-b816-45ab-9f72-21417d0b9829",
            "https://firebasestorage.googleapis.com/v0/b/topibatu-2a076.appspot.com/o/assets%2Fbanner3.png?alt=media&token=70e0d942-b816-45ab-9f72-21417d0b9829",
        )

        Column(
            modifier = Modifier
                .padding(
                    start = 8.dp,
                    end = 8.dp,
                    top = 75.dp,
                )
                .fillMaxWidth()
        ) {
            // Title
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Welcome to TaniLink, [NAME]",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )

            // Banner
            Spacer(modifier = Modifier.height(6.dp))
            CarouselSlider(images)

            // Special Product
            Spacer(modifier = Modifier.height(18.dp))
            Text(
                text = "Special Product",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 80.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp)
            ) {
                itemsIndexed(images) { index, imageUrl, ->
                    ProductCard(imageUrl = imageUrl)
                    Spacer(modifier = Modifier.width(18.dp))
                }
            }

            // Category
            Spacer(modifier = Modifier.height(18.dp))
            Text(
                text = "Category",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 80.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp)
                    .padding(bottom = 50.dp)
            ) {
                itemsIndexed(images) { index, imageUrl, ->
                    ProductCard(imageUrl = imageUrl)
                    Spacer(modifier = Modifier.width(18.dp))
                }
            }

        }
    }
}

@Composable
private fun CarouselSlider(images: List<String>) {
    var index by remember { mutableStateOf(0) }
    val scrollState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(key1 = true, block = {
        coroutineScope.launch {
            while (true) {
                delay(1000)
                if (index == images.size - 1) index = 0
                else index++
                scrollState.animateScrollToItem(index)
            }
        }
    })

    Column(
    ) {
        Box {
            LazyRow(
                state = scrollState,
                horizontalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                itemsIndexed(images) { index, image ->
                    Card(
                        modifier = Modifier.height(150.dp),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 8.dp
                        )
                    ) {
                        AsyncImage(
                            model = image,
                            contentDescription = "Image",
                            contentScale = ContentScale.FillBounds,
                            modifier = Modifier.width(300.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ProductCard(imageUrl: String){
    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        modifier = Modifier
            .size(width = 125.dp, height = 125.dp)
    ) {
        AsyncImage(
            model = imageUrl,
            contentDescription = "Image",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .width(125.dp)
                .height(125.dp)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(){
    var text by remember { mutableStateOf("") }
    var active by remember { mutableStateOf(false) }

    SearchBar(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth(),
        query = text,//text showed on SearchBar
        onQueryChange = {
            text = it
        },
        onSearch = {
            active = false
        },
        active = active,
        onActiveChange = {
            active = it
        },
        placeholder = {
            Text("Search")
        },
        leadingIcon = {
            if (!active) {
                Icon(imageVector = Icons.Default.Search, contentDescription = "Search")
            }
        },
        trailingIcon = {
            if (active) {
                IconButton(
                    onClick = {
                        active = false
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = "Clear",
                        tint = if (active) MaterialTheme.colorScheme.primary else Color.Gray
                    )
                }
            }
        },
    ) {

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