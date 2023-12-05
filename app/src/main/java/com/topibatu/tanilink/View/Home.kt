package com.topibatu.tanilink.View

import android.annotation.SuppressLint
import android.graphics.drawable.Icon
import androidx.compose.animation.core.animate
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.material3.IconButton as IconButton

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home(navController: NavController) {
    // TODO: FIX SCROLL, ENHANCE BOTTOM NAV, SEPARATE TOP AND BOTTOM NAV INTO DIFFERENT FILES

    var text by remember { mutableStateOf("") }
    var active by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
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
                .height(2000.dp)
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

@Composable
private fun BottomBar() {
    BottomAppBar(
        containerColor = MaterialTheme.colorScheme.primary,
        content = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Box(
                    modifier = Modifier
                        .width(80.dp)
                        .height(80.dp)
                        .background(
                            color = MaterialTheme.colorScheme.secondary,
                            shape = RoundedCornerShape(16.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Home,
                        contentDescription = "Home Icon",
                        modifier = Modifier
                            .offset(y = (-8).dp)
                            .size(24.dp),
                        tint = MaterialTheme.colorScheme.onSecondary
                    )
                    Text(
                        text = "Home",
                        modifier = Modifier
                            .padding(top = 24.dp)
                            .align(Alignment.Center),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSecondary
                    )
                }

                Box(
                    modifier = Modifier
                        .width(80.dp)
                        .height(80.dp)
                        .background(
                            color = MaterialTheme.colorScheme.secondary,
                            shape = RoundedCornerShape(16.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Home,
                        contentDescription = "Home Icon",
                        modifier = Modifier
                            .offset(y = (-8).dp)
                            .size(24.dp),
                        tint = MaterialTheme.colorScheme.onSecondary
                    )
                    Text(
                        text = "Home",
                        modifier = Modifier
                            .padding(top = 24.dp)
                            .align(Alignment.Center),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSecondary
                    )
                }

                Box(
                    modifier = Modifier
                        .width(80.dp)
                        .height(80.dp)
                        .background(
                            color = MaterialTheme.colorScheme.secondary,
                            shape = RoundedCornerShape(16.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Home,
                        contentDescription = "Home Icon",
                        modifier = Modifier
                            .offset(y = (-8).dp)
                            .size(24.dp),
                        tint = MaterialTheme.colorScheme.onSecondary
                    )
                    Text(
                        text = "Home",
                        modifier = Modifier
                            .padding(top = 24.dp)
                            .align(Alignment.Center),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSecondary
                    )
                }

            }
        }
    )
}