package com.topibatu.tanilink.View.components

import account_proto.AccountProto
import account_proto.AccountProto.AreaDetail
import android.R
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.topibatu.tanilink.Util.Account
import io.grpc.StatusException
import io.netty.channel.unix.NativeInetAddress.address
import kotlinx.coroutines.launch
import org.checkerframework.checker.units.qual.Area
import java.util.UUID

data class Address(
    var id: String?,
    var areaDetail: AreaDetail?,
    var addressDetail: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddressField(value: String, setShowDialog: (Boolean) -> Unit, setValue: (String) -> Unit) {
    val accountRPC = remember { Account() }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val addressRes = remember { mutableStateOf<AccountProto.AllAddressDetails?>(null) }
    val areaRes = remember { mutableStateOf<AccountProto.AllAreaDetails?>(null) }

    val address = remember { mutableStateListOf<Address>() }

    LaunchedEffect(key1 = null) {

        // Get Address
        addressRes.value = try {
            accountRPC.getAddress()
        } catch (e: StatusException) {
            Toast.makeText(
                context,
                "Get Address Failed: ${e.status.description}",
                Toast.LENGTH_SHORT
            ).show()
            null
        }
        
        addressRes.value?.addressList?.forEachIndexed { index, addressDetail ->
            address.add(
                Address(
                    id = addressDetail.id,
                    areaDetail = addressDetail.area,
                    addressDetail = addressDetail.detail
                )
            )
        }

        // Get All Area
        areaRes.value = try {
            accountRPC.getAllArea()
        } catch (e: StatusException) {
            Toast.makeText(
                context,
                "Get All Area Failed: ${e.status.description}",
                Toast.LENGTH_SHORT
            ).show()
            null
        }
    }

    Dialog(onDismissRequest = { setShowDialog(false) }) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color.White
        ) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                Column(modifier = Modifier.padding(20.dp)) {

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Addresses",
                            style = TextStyle(
                                fontSize = 24.sp,
                                fontFamily = FontFamily.Default,
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = "",
                            tint = colorResource(R.color.darker_gray),
                            modifier = Modifier
                                .width(30.dp)
                                .height(30.dp)
                                .clickable { setShowDialog(false) }
                        )
                    }
                    Spacer(modifier = Modifier.height(20.dp))

                    // Addresses List
                    LazyVerticalGrid(
                        columns = GridCells.Adaptive(minSize = LocalConfiguration.current.screenWidthDp.dp),
                    ) {
                        address.let {
                            // Address
                            itemsIndexed(address) { index, _ ->
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    // Area
                                    var expanded by remember { mutableStateOf(false) }
                                    ExposedDropdownMenuBox(
                                        expanded = expanded,
                                        onExpandedChange = { expanded = !expanded }) {
                                        OutlinedTextField(
                                            value = address[index].areaDetail?.kecamatan
                                                ?: " ", // Ambil kecamatan
                                            label = { Text(text = "Area") },
                                            onValueChange = {},
                                            readOnly = true,
                                            trailingIcon = {
                                                ExposedDropdownMenuDefaults.TrailingIcon(
                                                    expanded = expanded
                                                )
                                            },
                                            shape = RoundedCornerShape(32.dp),
                                            modifier = Modifier
                                                .menuAnchor()
                                                .width(LocalConfiguration.current.screenWidthDp.dp / 3)
                                        )

                                        ExposedDropdownMenu(
                                            expanded = expanded,
                                            onDismissRequest = { expanded = false }
                                        ) {
                                            areaRes.value?.areaList?.forEach { item ->
                                                DropdownMenuItem(
                                                    text = { Text(text = item.provinsi + ", " + item.kota + ", " + item.kecamatan) },
                                                    onClick = {
                                                        address[index].areaDetail = item
                                                        expanded = false
                                                    },
                                                    contentPadding = PaddingValues(vertical = 8.dp)
                                                )
                                            }
                                        }
                                    }
                                    Spacer(modifier = Modifier.width(8.dp))

                                    // Address Detail
                                    val addressDetailState =
                                        remember { mutableStateOf(address[index].addressDetail) }
                                    OutlinedTextField(
                                        value = addressDetailState.value,
                                        onValueChange = {
                                            addressDetailState.value = it
                                            address[index].addressDetail = addressDetailState.value
                                        },
                                        label = { Text(text = "Detail Alamat") },
                                        shape = RoundedCornerShape(32.dp),
                                        modifier = Modifier.width(LocalConfiguration.current.screenWidthDp.dp / 3)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))

                                    // Delete Icon
                                    Icon(
                                        imageVector = Icons.Filled.Delete,
                                        contentDescription = "Hapus Alamat",
                                        tint = Color.Red,
                                        modifier = Modifier
                                            .width(30.dp)
                                            .height(30.dp)
                                            .clickable {
                                                /* TODO: Delete Items */
                                                scope.launch {
                                                    try {
                                                        address[index].id?.let { id ->
                                                            accountRPC.deleteAddress(id)
                                                        }
                                                        // Update address
                                                        addressRes.value = try {
                                                            accountRPC.getAddress()
                                                        } catch (e: StatusException) {
                                                            Toast.makeText(
                                                                context,
                                                                "Get Address Failed: ${e.status.description}",
                                                                Toast.LENGTH_SHORT
                                                            ).show()
                                                            null
                                                        }
                                                        address.removeAll(address)
                                                        addressRes.value?.addressList?.forEachIndexed { index, addressDetail ->
                                                            address.add(
                                                                Address(
                                                                    id = addressDetail.id,
                                                                    areaDetail = addressDetail.area,
                                                                    addressDetail = addressDetail.detail
                                                                )
                                                            )
                                                        }

                                                        Toast.makeText(
                                                            context,
                                                            "Address Deleted",
                                                            Toast.LENGTH_SHORT
                                                        ).show()
                                                    } catch (e: StatusException) {
                                                        Toast.makeText(
                                                            context,
                                                            "Update Address Failed: ${e.status.description}",
                                                            Toast.LENGTH_SHORT
                                                        ).show()
                                                    }
                                                }
                                            }
                                    )
                                }
                            }
                        }


                    }
                    Spacer(modifier = Modifier.height(20.dp))


                    Row {
                        // Add Button
                        Button(
                            onClick = {
                                /* Add Address */
                                address.add(Address(null, null, ""))
                            },
                            shape = RoundedCornerShape(50.dp),
                            modifier = Modifier
                                .weight(2f)
                                .height(50.dp)
                        ) {
                            Text(text = "Add")
                        }
                        Spacer(modifier = Modifier.width(8.dp))

                        // Done Button
                        Button(
                            onClick = {
                                /* Save Data */
                                scope.launch {
                                    try {
                                        accountRPC.editAddress(address)
                                        Toast.makeText(
                                            context,
                                            "Address Updated",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    } catch (e: StatusException) {
                                        Toast.makeText(
                                            context,
                                            "Update Address Failed: ${e.status.description}",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                                setShowDialog(false)
                            },
                            shape = RoundedCornerShape(50.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(50.dp)
                        ) {
                            Icon(imageVector = Icons.Filled.Done, contentDescription = "Save")
                        }
                    }

                }
            }
        }
    }
}