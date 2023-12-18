package com.topibatu.tanilink.View.components

import android.R
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
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddressField(value: String, setShowDialog: (Boolean) -> Unit, setValue: (String) -> Unit) {
    // tmp Area List
    val areaList = listOf<List<String>>(
        listOf("Jawa Tengah", "Semarang", "Gajah Mungkur"),
        listOf("Jawa Tengah", "Semarang2", "Gajah Mungkur2"),
        listOf("Jawa Tengah", "Semarang3", "Gajah Mungkur2"),
    )

    // tmp Dataset (dari backend)
    data class DataAddress(
        var area: List<String>,
        var addressDetail: String,
    )

    val data = listOf<DataAddress>(
        DataAddress(areaList[1], "Jalan Tunjungan"),
        DataAddress(areaList[0], "Jalan Dr Soetomo"),
        DataAddress(areaList[2], "Jalan Bung Tomo"),
        DataAddress(areaList[2], "Jalan Soekarno"),
    )


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
                        itemsIndexed(data) { index, address ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Area
                                var expanded by remember { mutableStateOf(false) }
                                var areaState by remember { mutableStateOf(address.area) } // TODO: Ganti ini jadi dynamic
                                ExposedDropdownMenuBox(
                                    expanded = expanded,
                                    onExpandedChange = { expanded = !expanded }) {
                                    OutlinedTextField(
                                        value = areaState[2], // Ambil kecamatan
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
                                        areaList.forEach { item ->
                                            DropdownMenuItem(
                                                text = { Text(text = item.joinToString(", ")) },
                                                onClick = {
                                                    areaState = item
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
                                    remember { mutableStateOf(address.addressDetail) } // TODO: Ganti ini jadi dynamic
                                OutlinedTextField(
                                    value = addressDetailState.value,
                                    onValueChange = { addressDetailState.value = it },
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
                                        }
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(20.dp))

                    // Done Button
                    Box(modifier = Modifier.padding(40.dp, 0.dp, 40.dp, 0.dp)) {
                        Button(
                            onClick = {
                                /* TODO: Pass Data (if needed) */
                                setShowDialog(false)
                            },
                            shape = RoundedCornerShape(50.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                        ) {
                            Text(text = "Done")
                        }
                    }
                }
            }
        }
    }
}