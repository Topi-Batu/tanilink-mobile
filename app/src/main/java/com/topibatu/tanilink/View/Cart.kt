package com.topibatu.tanilink.View

import android.annotation.SuppressLint
import android.content.res.Resources.Theme
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.topibatu.tanilink.Util.Marketplace
import com.topibatu.tanilink.View.components.BottomBar
import com.topibatu.tanilink.View.components.ProductItem
import com.topibatu.tanilink.View.components.TopBar
import io.grpc.StatusException
import kotlinx.coroutines.launch
import marketplace_proto.MarketplaceProto


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun CartPage(navController: NavController) {
    val marketPlaceRPC = remember { Marketplace() }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    // Tab State
    var state by remember { mutableStateOf(0) }
    val titles = listOf("Shopping Cart", "Invoices")

    // gRPC Response
    val getCartRes = remember { mutableStateOf<MarketplaceProto.AllShoppingCartDetail?>(null) }
    val getInvoiceRes = remember { mutableStateOf<MarketplaceProto.AllInvoiceDetail?>(null) }
    val getCheckoutRes = remember { mutableStateOf<MarketplaceProto.InvoiceDetail?>(null) }

    // Checkbox State
    val isChecked = remember { mutableStateListOf<Boolean?>(null) }
    fun onCheckboxStateChanged(index: Int, newState: Boolean) {
        isChecked[index] = newState
    }

    LaunchedEffect(key1 = null) {
        // Get Shopping Cart
        getCartRes.value = try {
            marketPlaceRPC.getUserShoppingCarts()
        } catch (e: StatusException) {
            Toast.makeText(
                context,
                "Get User Shopping Carts Failed: ${e.status.description}",
                Toast.LENGTH_SHORT
            ).show()
            null
        }

        // Fill isChecked Value
        getCartRes.value?.let {
            val productAmount = it.shoppingCartsList.count()
            isChecked.addAll(List(productAmount) { false })
        }

        // Get User Invoices
        getInvoiceRes.value = try {
            marketPlaceRPC.getUserInvoices()
        } catch (e: StatusException) {
            Toast.makeText(
                context,
                "Get User Invoices Failed: ${e.status.description}",
                Toast.LENGTH_SHORT
            ).show()
            null
        }
    }


    Scaffold(
        topBar = {
            TopBar(navController, "Cart")
        },
        bottomBar = {
            BottomBar(navController, -1)
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    // In Cart Tab
                    if (state == 0) {
                        var productIdList: MutableList<String> = mutableListOf()
                        isChecked.forEachIndexed { index, isCheckedValue ->
                            getCartRes.value?.let {
                                if (isCheckedValue != null) {
                                    if (isCheckedValue) {
                                        productIdList.add(it.shoppingCartsList[index].id)
                                    }
                                }
                            }
                        }

                        // Checkout
                        scope.launch {
                            getCheckoutRes.value = try {
                                marketPlaceRPC.checkout(productIdList)
                            } catch (e: StatusException) {
                                Toast.makeText(
                                    context,
                                    "Checkout Failed: ${e.status.description}",
                                    Toast.LENGTH_SHORT
                                ).show()
                                null
                            }

                            // Navigate to Cart Detail
                            getCheckoutRes.value?.let {
                                Toast.makeText(
                                    context,
                                    "Checkout Success",
                                    Toast.LENGTH_SHORT
                                ).show()
                                navController.navigate("cart_detail/${it.id}")
                            }
                        }

                        // In Invoices Tab
                    } else if (state == 1) {

                    }
                }
            ) {
                Icon(Icons.Filled.ArrowForward, "Go To Cart Detail")
            }
        }
    ) {

        Column(
            modifier = Modifier.padding(top = 50.dp)
        ) {
            TabRow(selectedTabIndex = state) {
                titles.forEachIndexed { index, title ->
                    Tab(
                        selected = state == index,
                        onClick = { state = index },
                        text = {
                            Text(
                                text = title,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    )
                }
            }
            // State Shopping Cart
            if (state == 0) {
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(minSize = LocalConfiguration.current.screenWidthDp.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 8.dp, end = 8.dp, bottom = 90.dp)
                ) {
                    getCartRes.value?.let {
                        itemsIndexed(it.shoppingCartsList) { index, shoppingCart ->
                            ProductItem(
                                shoppingCart = shoppingCart,
                                showButton = true,
                                isChecked = isChecked[index],
                                onCheckboxStateChanged = { newState ->
                                    onCheckboxStateChanged(
                                        index,
                                        newState
                                    )
                                }
                            )
                        }
                    }
                }

                // State Invoices
            } else if (state == 1) {
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(minSize = LocalConfiguration.current.screenWidthDp.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 8.dp, end = 8.dp, bottom = 90.dp)
                ) {
                    getInvoiceRes.value?.let {
                        itemsIndexed(it.invoicesList) { index, invoice ->
                            Column (
                                modifier = Modifier.padding(vertical = 12.dp)
                                    .clickable {
                                        navController.navigate("cart_detail/${invoice.id}")
                                    }
                            ) {
                                Text(text = "Invoice #${index+1}")
                                Text(text = "Address: ${invoice.ordersList[0].address}")

                                // Loop Orders
                                invoice.ordersList.forEachIndexed { index, orderDetail ->
                                    // Get Product List
                                    var productList = ""
                                    orderDetail.shoppingCartsList.forEach { shoppingCartDetail ->
                                        productList += "${shoppingCartDetail.product.name} @ ${shoppingCartDetail.amount}, "
                                    }

                                    Text(text = "\nOrder #${index+1}")
                                    Text(text = "Product List: \n$productList")
                                    Text(text = "Delivery Price: ${orderDetail.deliveryPrice}")
                                    Text(text = "Total Price: ${orderDetail.totalPrice}")
                                    Text(text = "Status: ${orderDetail.status}")
                                }

                            }
                            Divider(color = MaterialTheme.colorScheme.primary, thickness = 1.dp)
                        }
                    }
                }
            }
        }

    }
}

