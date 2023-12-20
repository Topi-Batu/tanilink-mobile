package com.topibatu.tanilink.View.components

import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.topibatu.tanilink.Util.Marketplace
import io.grpc.StatusException
import kotlinx.coroutines.launch
import marketplace_proto.MarketplaceProto.ProductDetail
import marketplace_proto.MarketplaceProto.ShoppingCartDetail

@Composable
fun ProductItem(
    shoppingCart: ShoppingCartDetail,
    showButton: Boolean,
    isChecked: Boolean?,
    onCheckboxStateChanged: (Boolean) -> Unit?) {
    val marketPlaceRPC = remember { Marketplace() }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val jumlahProductLocal = remember { mutableStateOf(shoppingCart.amount) }

    if(jumlahProductLocal.value == 0){
        // Jika produk kosong, maka tidak menampilkan product item
    } else {
        Column(
            modifier = Modifier.padding(top = 12.dp, bottom = 28.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                // If showCheckBox true, Show Checkbox
                if(showButton){
                    Checkbox(
                        checked = isChecked ?: false,
                        onCheckedChange = { newState ->
                            onCheckboxStateChanged(newState)
                        }
                    )
                }

                Column {
                    Text(text = "Nama Toko", modifier = Modifier.width(88.dp))
                    Spacer(modifier = Modifier.height(8.dp))
                    AsyncImage(
                        model = shoppingCart.product.imageList[0],
                        contentDescription = "Product Image",
                        modifier = Modifier
                            .size(88.dp)
                            .clip(RoundedCornerShape(18.dp))
                            .border(2.dp, Color.Gray, RoundedCornerShape(18.dp))
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f)
                ) {
                    if(showButton) {
                        Button(
                            modifier = Modifier
                                .align(Alignment.End)
                                .height(32.dp)
                                .width(64.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(255, 90, 90),
                                contentColor = Color.White
                            ),
                            onClick = {
                                // Remove product
                                scope.launch {
                                    try {
                                        marketPlaceRPC.removeProductFromShoppingCart(shoppingCart.product.id)
                                        jumlahProductLocal.value = 0
                                    } catch (e: StatusException) {
                                        Toast.makeText(
                                            context,
                                            "Remove Product in Cart Failed: ${e.status.description}",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            }
                        ) {
                            Icon(Icons.Filled.Delete, contentDescription = "Delete")
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))

                    Text(text = shoppingCart.product.name)
                    Spacer(modifier = Modifier.height(8.dp))

                    Text(text = shoppingCart.product.price)
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if(showButton){
                            Button(
                                modifier = Modifier
                                    .height(33.dp)
                                    .width(56.dp),
                                onClick = {
                                    // Decrease amount of product
                                    scope.launch {
                                        try {
                                            marketPlaceRPC.decreaseProductInShoppingCart(shoppingCart.product.id)
                                            jumlahProductLocal.value--
                                        } catch (e: StatusException) {
                                            Toast.makeText(
                                                context,
                                                "Decrease Product Amount in Cart Failed: ${e.status.description}",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }
                                }
                            ) {
                                Text(text = "-")
                            }
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = jumlahProductLocal.value.toString(), fontSize = 24.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        if(showButton){
                            Button(
                                modifier = Modifier
                                    .height(33.dp)
                                    .width(56.dp),
                                onClick = {
                                    // Add amount of product
                                    scope.launch {
                                        try {
                                            marketPlaceRPC.addProductToShoppingCart(shoppingCart.product.id)
                                            jumlahProductLocal.value++
                                        } catch (e: StatusException) {
                                            Toast.makeText(
                                                context,
                                                "Add Product Amount in Cart Failed: ${e.status.description}",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }
                                }
                            ) {
                                Text(text = "+")
                            }
                        }
                    }
                }
            }
        }
        Divider(color = MaterialTheme.colorScheme.primary, thickness = 1.dp)
    }

}

