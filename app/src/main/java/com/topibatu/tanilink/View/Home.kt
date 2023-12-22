package com.topibatu.tanilink.View

import account_proto.AccountProto
import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import com.orhanobut.hawk.Hawk
import com.topibatu.tanilink.R
import com.topibatu.tanilink.Util.Account
import com.topibatu.tanilink.Util.Marketplace
import com.topibatu.tanilink.Util.Photo
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.material3.IconButton as IconButton
import com.topibatu.tanilink.View.components.BottomBar
import com.topibatu.tanilink.View.components.FirebaseMessagingNotificationPermissionDialog
import io.grpc.StatusException
import marketplace_proto.MarketplaceProto
import marketplace_proto.MarketplaceProto.CommodityDetail

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun HomePage(navController: NavController) {
    val accountRPC = remember { Account() }
    val marketPlaceRPC = remember { Marketplace() }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    // Firebase Cloud Messaging (Notification Service)
    val showNotificationDialog = remember { mutableStateOf(false) }
    val notificationPermissionState = rememberPermissionState(
        permission = Manifest.permission.POST_NOTIFICATIONS
    )
    if (showNotificationDialog.value) FirebaseMessagingNotificationPermissionDialog(
        showNotificationDialog = showNotificationDialog,
        notificationPermissionState = notificationPermissionState
    )

    val getProfileRes = remember { mutableStateOf<AccountProto.AccountDetail?>(null) }
    val getCommoditiesRes = remember { mutableStateOf<MarketplaceProto.AllCommodityDetails?>(null) }

    LaunchedEffect(null) {
        // Get Profile
        getProfileRes.value = try {
            accountRPC.getProfile()
        } catch (e: StatusException) {
            Toast.makeText(
                context,
                "Get Profile Failed: ${e.status.description}",
                Toast.LENGTH_SHORT
            ).show()
            null
        }

        // Get Commodities
        getCommoditiesRes.value = try {
            marketPlaceRPC.getAllCommodities()
        } catch (e: StatusException) {
            Toast.makeText(
                context,
                "Get All Commodities Failed: ${e.status.description}",
                Toast.LENGTH_SHORT
            ).show()
            null
        }

        // Check Request Notification Permission
        if (notificationPermissionState.status.isGranted ||
            Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU
        ) {
            Firebase.messaging.subscribeToTopic("main")
        } else showNotificationDialog.value = true
    }

    val profileName = getProfileRes.value?.fullName ?: "..."

    Scaffold(
        topBar = {
            TopBar(navController = navController)
        },
        bottomBar = {
            BottomBar(navController, 0)
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    /* TODO: Redirect to seller page */
                    Toast.makeText(
                        context,
                        "Redirecting to Seller Page ...",
                        Toast.LENGTH_SHORT
                    ).show()
                    val webIntent: Intent = Uri.parse("https://tanilink.bantuin.me/Dashboard").let { webpage ->
                        Intent(Intent.ACTION_VIEW, webpage)
                    }
                    ContextCompat.startActivity(context, webIntent, null)
                }
            ) {
                Icon(Icons.Filled.Add, "Add Product")
            }
        }
    ) {
        val images = listOf(
            "https://firebasestorage.googleapis.com/v0/b/topibatu-2a076.appspot.com/o/assets%2Fbanner1.png?alt=media&token=985a3e0b-8773-4c84-afae-bcd7c01e695d",
            "https://firebasestorage.googleapis.com/v0/b/topibatu-2a076.appspot.com/o/assets%2Fbanner1.png?alt=media&token=985a3e0b-8773-4c84-afae-bcd7c01e695d",
            "https://firebasestorage.googleapis.com/v0/b/topibatu-2a076.appspot.com/o/assets%2Fbanner1.png?alt=media&token=985a3e0b-8773-4c84-afae-bcd7c01e695d",
            "https://firebasestorage.googleapis.com/v0/b/topibatu-2a076.appspot.com/o/assets%2Fbanner1.png?alt=media&token=985a3e0b-8773-4c84-afae-bcd7c01e695d",
            "https://firebasestorage.googleapis.com/v0/b/topibatu-2a076.appspot.com/o/assets%2Fbanner1.png?alt=media&token=985a3e0b-8773-4c84-afae-bcd7c01e695d",
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
                text = "Welcome to TaniLink, $profileName",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )

            // Banner
            Spacer(modifier = Modifier.height(6.dp))
            CarouselSlider(images)

            // Special Product
            Spacer(modifier = Modifier.height(18.dp))
            Text(
                text = "Commodities",
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
                getCommoditiesRes.value?.let {
                    itemsIndexed(it.commoditiesList) { index, commodity ->
                        ProductCard(navController = navController, commodity = commodity)
                        Spacer(modifier = Modifier.width(18.dp))
                    }
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
private fun ProductCard(navController: NavController, commodity: CommodityDetail) {
    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        modifier = Modifier
            .size(width = 125.dp, height = 125.dp)
            .clickable {
                navController.navigate("category_list/${commodity.id}")
            }
    ) {
        AsyncImage(
            model = commodity.image,
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
private fun TopBar(navController: NavController) {
    var text by remember { mutableStateOf("") }
    var active by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        SearchBar(
            query = text, //text showed on SearchBar
            modifier = Modifier.weight(1f),
            onQueryChange = {
                text = it
            },
            onSearch = {
                active = false
                navController.navigate("category_list/query:$it")
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
        Spacer(modifier = Modifier.width(4.dp))
        Icon(
            Icons.Filled.ShoppingCart,
            contentDescription = "Cart",
            modifier = Modifier
                .size(32.dp)
                .clickable {
                    navController.navigate("cart")
                }
        )
    }

}
