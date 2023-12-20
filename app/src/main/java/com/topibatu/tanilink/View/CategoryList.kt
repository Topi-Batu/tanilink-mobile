@file:OptIn(ExperimentalMaterial3Api::class)

package com.topibatu.tanilink.View

import account_proto.AccountProto
import android.annotation.SuppressLint
import android.widget.Toast
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.topibatu.tanilink.Util.Marketplace
import com.topibatu.tanilink.View.components.TopBar
import io.grpc.StatusException
import marketplace_proto.MarketplaceProto

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun CategoryListPage(navController: NavController, param: String) {
    val marketPlaceRPC = remember { Marketplace() }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val getProductRes = remember { mutableStateOf<MarketplaceProto.AllProductDetails?>(null) }

    LaunchedEffect(key1 = null) {
        // Check if it is query or commodityId
        // if query
        if(param.contains("query:")) {
            getProductRes.value = try {
                marketPlaceRPC.searchProduct(query = param.replace("query:", ""))
            } catch (e: StatusException) {
                Toast.makeText(
                    context,
                    "Get Products Failed: ${e.status.description}",
                    Toast.LENGTH_SHORT
                ).show()
                null
            }

        // if commodity Id
        } else {
            // Get Products
            getProductRes.value = try {
                marketPlaceRPC.getProductByCommodityId(commodityId = param)
            } catch (e: StatusException) {
                Toast.makeText(
                    context,
                    "Get Products Failed: ${e.status.description}",
                    Toast.LENGTH_SHORT
                ).show()
                null
            }
        }



    }

    Scaffold(
        topBar = {
            TopBar(navController, "Category List")
        },
    ) {
        LazyColumn(modifier = Modifier.padding(top = 62.dp)) {
            getProductRes.value?.let {
                itemsIndexed(it.productsList) { index, product ->
                    Row(
                        modifier = Modifier
                            .padding(top = 10.dp, start = 24.dp, end = 24.dp)
                            .clickable {
                                navController.navigate("product_detail/${product.id}")
                            }
                    ) {
                        Image(
                            painter = rememberAsyncImagePainter(product.imageList[0]),
                            contentDescription = "Product Image",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(160.dp)
                                .clip(RoundedCornerShape(18.dp))
                                .border(2.dp, Color.Gray, RoundedCornerShape(18.dp))
                        )
                        Spacer(modifier = Modifier.width(48.dp))
                        Column(
                            modifier = Modifier.height(160.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(text = product.name)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(text = "Rp. ${product.price}", fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(onClick = {
                                navController.navigate("product_detail/${product.id}")
                            }) {
                                Text(text = "Buy")
                            }
                        }
                    }
                }
            }
        }
    }
}


