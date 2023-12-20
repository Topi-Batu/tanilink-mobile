package com.topibatu.tanilink.View

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.topibatu.tanilink.Util.Marketplace
import com.topibatu.tanilink.View.components.BottomBar
import com.topibatu.tanilink.View.components.TopBar
import io.grpc.StatusException
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import marketplace_proto.MarketplaceProto

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ProductDetailPage(navController: NavController, productId: String) {
    val marketPlaceRPC = remember { Marketplace() }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val getProductRes = remember { mutableStateOf<MarketplaceProto.ProductDetail?>(null) }

    LaunchedEffect(key1 = null) {
        // Get Product
        getProductRes.value = try {
            marketPlaceRPC.getProductById(productId)
        } catch (e: StatusException) {
            Toast.makeText(
                context,
                "Get Product Failed: ${e.status.description}",
                Toast.LENGTH_SHORT
            ).show()
            null
        }
    }

    Scaffold(
        topBar = {
            TopBar(navController = navController, "Product Detail")
        },
        bottomBar = {
            BottomBar(navController, -1)
        }
    ) {
        Column(
            modifier = Modifier
                .padding(top = 64.dp, start = 24.dp, end = 24.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            getProductRes.value?.let {
                // Image
//                Image(
//                    painter = rememberAsyncImagePainter(it.imageList[0]),
//                    contentDescription = "avatar",
//                    contentScale = ContentScale.Crop,
//                    modifier = Modifier
//                        .size(160.dp)
//                        .border(2.dp, Color.Gray)
//                )
                CarouselSlider(images = it.imageList)
                Spacer(modifier = Modifier.height(36.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        // Nama Toko
                        Text(text = "Shop Name")
                        // Harga
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = "Rp.${it.price}", fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = { /*TODO*/ }) {
                        Text(text = "Chat")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    // Add to Cart
                    Button(onClick = {
                        /* Add To Shopping Cart */
                        scope.launch {
                            try {
                                marketPlaceRPC.addProductToShoppingCart(it.id)
                                Toast.makeText(
                                    context,
                                    "Product Added to Shopping Cart",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } catch (e: StatusException) {
                                Toast.makeText(
                                    context,
                                    "Add Product to Cart Failed: ${e.status.description}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Filled.ShoppingCart,
                            contentDescription = "Add to Cart"
                        )
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
                // Stock
                Text(
                    text = "Stock: ${it.availableStock}",
                    fontWeight = FontWeight.SemiBold,
                )
                Spacer(modifier = Modifier.height(18.dp))
                Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.Start) {
                    // Description
                    Text(text = "Description", fontWeight = FontWeight.Bold)
                    Text(
                        text = it.description
                    )
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
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(160.dp)
                                .border(2.dp, Color.Gray)
                        )
                    }
                }
            }
        }
    }
}


