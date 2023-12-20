package com.topibatu.tanilink.View

import account_proto.AccountProto
import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.topibatu.tanilink.Util.Account
import com.topibatu.tanilink.Util.Marketplace
import com.topibatu.tanilink.View.components.BottomBar
import com.topibatu.tanilink.View.components.ProductItem
import com.topibatu.tanilink.View.components.TopBar
import io.grpc.StatusException
import marketplace_proto.MarketplaceProto

data class Address(
    var id: String?,
    var addressDetail: String
)

data class OrderNotes(
    var orderId: String,
    var notes: String,
)

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun CartDetailPage(navController: NavController, invoiceId: String) {
    val accountRPC = remember { Account() }
    val marketPlaceRPC = remember { Marketplace() }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    // Address Var
    val getAddressRes = remember { mutableStateOf<AccountProto.AllAddressDetails?>(null) }
    val address = remember { mutableStateListOf<Address>() }
    var expandedAddress by remember { mutableStateOf(false) }
    val addressState = remember { mutableStateOf<Address?>(null) }

    // Profile Var
    val getProfileRes = remember { mutableStateOf<AccountProto.AccountDetail?>(null) }

    // Invoice Var
    val getInvoiceRes = remember { mutableStateOf<MarketplaceProto.InvoiceDetail?>(null) }

    // Order Notes Var
    val orderNotes = remember { mutableStateListOf<OrderNotes?>(null) }

    LaunchedEffect(key1 = null) {
        // Get User Profile
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

        // Get User Address
        getAddressRes.value = try {
            accountRPC.getAddress()
        } catch (e: StatusException) {
            Toast.makeText(
                context,
                "Get Address Failed: ${e.status.description}",
                Toast.LENGTH_SHORT
            ).show()
            null
        }

        // Fill address value
        getAddressRes.value?.addressList?.forEachIndexed { index, addressDetail ->
            address.add(
                Address(
                    id = addressDetail.id,
                    addressDetail = "${addressDetail.detail}, ${addressDetail.area.provinsi}, ${addressDetail.area.kota}, ${addressDetail.area.kecamatan}"
                )
            )
        }
        getAddressRes.value?.let {
            addressState.value = address[0]
        }

        // Get Invoices
        getInvoiceRes.value = try {
            marketPlaceRPC.getInvoiceById(invoiceId)
        } catch (e: StatusException) {
            Toast.makeText(
                context,
                "Get User Invoices Failed: ${e.status.description}",
                Toast.LENGTH_SHORT
            ).show()
            null
        }

        // Fill Order Notes
        getInvoiceRes.value?.let {
            it.ordersList.forEach { orderDetail ->
                orderNotes.add(
                    OrderNotes(
                        orderId = orderDetail.id,
                        notes = orderDetail.notes
                    )
                )
            }
        }

    }


    Scaffold(
        topBar = {
            TopBar(navController, "Cart Detail")
        },
    ) {
        Column(
            modifier = Modifier.padding(top = 78.dp, start = 16.dp, end = 16.dp)
        ) {
            // Text Nama User
            Text(
                text = getProfileRes.value?.fullName ?: "NULL",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Dropdown Alamat
            ExposedDropdownMenuBox(
                expanded = expandedAddress,
                onExpandedChange = { expandedAddress = !expandedAddress },
            ) {
                OutlinedTextField(
                    value = addressState.value?.addressDetail ?: "NULL",
                    onValueChange = {},
                    label = { Text("Alamat") },
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedAddress) },
                    shape = RoundedCornerShape(32.dp),
                    modifier = Modifier
                        .height(58.dp)
                        .width(218.dp)
                        .menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = expandedAddress,
                    onDismissRequest = { expandedAddress = false }
                ) {
                    address.forEach { item ->
                        DropdownMenuItem(
                            text = { Text(text = item.addressDetail) },
                            onClick = {
                                addressState.value = item
                                expandedAddress = false
                            }
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))

            getInvoiceRes.value?.let {
                var biayaItem: Long = 0
                var ongkosKirim: Long = 0
                var totalPembayaran = it.totalPrice

                // ProductItems
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(minSize = LocalConfiguration.current.screenWidthDp.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(LocalConfiguration.current.screenHeightDp.dp / 1.85f)
                        .padding(start = 8.dp, end = 8.dp)
                ) {
                    it.ordersList.forEach { orderDetail ->
                        // Hitung Total Ongkos Kirim
                        ongkosKirim += orderDetail.deliveryPrice

                        itemsIndexed(orderDetail.shoppingCartsList) { index, shoppingCart ->
                            ProductItem(
                                shoppingCart = shoppingCart,
                                showButton = false,
                                isChecked = null,
                                onCheckboxStateChanged = { null })
                        }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))

                // Informasi Pembayaran
                biayaItem = totalPembayaran - ongkosKirim
                Column {
                    Text(text = "Informasi Pembayaran", fontWeight = FontWeight.ExtraBold)
                    Text(text = "Biaya Item: Rp. $biayaItem")
                    Text(text = "Biaya Ongkos Kirim: Rp. $ongkosKirim")
                    Text(
                        text = "Total Pembayaran: Rp. $totalPembayaran",
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Button Bayar
                Button(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    onClick = {
                        /* TODO: PlaceOrder */
                    }
                ) {
                    Text(text = "Bayar")
                }
            }

        }

    }

}
