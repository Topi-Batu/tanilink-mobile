package com.topibatu.tanilink.View

import android.annotation.SuppressLint
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.topibatu.tanilink.View.components.BottomBar
import com.topibatu.tanilink.View.components.TopBar

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun CartDetailPage(navController: NavController) {

    // tmp product data
    val products: List<Product> = listOf<Product>(
        Product(
            namaToko = "Toko Satu Roda",
            namaProduk = "Beras",
            fotoProduk = "https://cdn.mos.cms.futurecdn.net/2aipAM72aBPS7Ny4L2MeNn-1200-80.jpg",
            harga = 14000,
            jumlahProduk = 4,
            isChecked = false
        ),
        Product(
            namaToko = "Toko Dua Roda",
            namaProduk = "Cabai",
            fotoProduk = "https://oyster.ignimgs.com/mediawiki/apis.ign.com/genshin-impact/f/f5/Hu_Tao_Birthday_Banner.png?width=1280",
            harga = 14000,
            jumlahProduk = 8,
            isChecked = true
        ),
        Product(
            namaToko = "Toko Tiga Roda",
            namaProduk = "Semen",
            fotoProduk = "https://media.suara.com/pictures/970x544/2022/10/09/72774-karakter-hu-tao-genshin-impact.jpg",
            harga = 14000,
            jumlahProduk = 38,
            isChecked = false
        ),
        Product(
            namaToko = "Toko Tiga Roda",
            namaProduk = "Semen",
            fotoProduk = "https://media.suara.com/pictures/970x544/2022/10/09/72774-karakter-hu-tao-genshin-impact.jpg",
            harga = 14000,
            jumlahProduk = 38,
            isChecked = false
        ),
        Product(
            namaToko = "Toko Tiga Roda",
            namaProduk = "Semen",
            fotoProduk = "https://media.suara.com/pictures/970x544/2022/10/09/72774-karakter-hu-tao-genshin-impact.jpg",
            harga = 14000,
            jumlahProduk = 38,
            isChecked = false
        ),
        Product(
            namaToko = "Toko Tiga Roda",
            namaProduk = "Semen",
            fotoProduk = "https://media.suara.com/pictures/970x544/2022/10/09/72774-karakter-hu-tao-genshin-impact.jpg",
            harga = 14000,
            jumlahProduk = 38,
            isChecked = false
        ),
        Product(
            namaToko = "Toko Tiga Roda",
            namaProduk = "Semen",
            fotoProduk = "https://media.suara.com/pictures/970x544/2022/10/09/72774-karakter-hu-tao-genshin-impact.jpg",
            harga = 14000,
            jumlahProduk = 38,
            isChecked = false
        ),
    )

    // Alamat Var
    var expandedAddress by remember { mutableStateOf(false) }
    val addressList = arrayOf("Alamat 1", "Alamat 2", "Alamat 3")
    val addressState = remember { mutableStateOf(addressList[0]) }

    Scaffold(
        topBar = {
            TopBar(navController, "Cart Detail")
        },
    ) {
        Column (
            modifier = Modifier.padding(top = 78.dp, start = 16.dp, end = 16.dp)
        ) {
            // Text Nama User
            Text(text = "Nama User", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))

            // Dropdown Alamat
            ExposedDropdownMenuBox(
                expanded = expandedAddress,
                onExpandedChange = { expandedAddress = !expandedAddress },
            ) {
                OutlinedTextField(
                    value = addressState.value,
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
                    addressList.forEach { item ->
                        DropdownMenuItem(
                            text = { Text(text = item) },
                            onClick = {
                                addressState.value = item
                                expandedAddress = false
                            }
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))

            // ProductItems
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = LocalConfiguration.current.screenWidthDp.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(LocalConfiguration.current.screenHeightDp.dp / 1.85f)
                    .padding(start = 8.dp, end = 8.dp)
            ) {
                itemsIndexed(products) { index, product ->
                    ProductItem(product = product)
                    Divider(color = MaterialTheme.colorScheme.primary, thickness = 1.dp)
                }
            }
            Spacer(modifier = Modifier.height(8.dp))

            // Informasi Pembayaran
            Column {
                Text(text = "Informasi Pembayaran", fontWeight = FontWeight.ExtraBold)
                Text(text = "Biaya Item: Rp. {BIAYA-ITEM}")
                Text(text = "Biaya Ongkos Kirim: Rp. {BIAYA-ONGKIR}")
                Text(text = "Total Pembayaran: Rp. {BIAYA-TOTAL}", fontWeight = FontWeight.SemiBold)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Button Bayar
            Button(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                onClick = { /*TODO*/ }
            ) {
                Text(text = "Bayar")
            }


        }

    }

}

@Composable
private fun ProductItem(product: Product) {
    val jumlahProductLocal = remember { mutableStateOf(product.jumlahProduk) }

    Column(
        modifier = Modifier.padding(top = 12.dp, bottom = 28.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(text = product.namaToko)
                Spacer(modifier = Modifier.height(8.dp))
                AsyncImage(
                    model = product.fotoProduk,
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
                Button(
                    modifier = Modifier
                        .align(Alignment.End)
                        .height(32.dp)
                        .width(64.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(255, 90, 90),
                        contentColor = Color.White
                    ),
                    onClick = { /*TODO*/ }
                ) {
                    Icon(Icons.Filled.Delete, contentDescription = "Delete")
                }
                Spacer(modifier = Modifier.height(16.dp))

                Text(text = product.namaProduk)
                Spacer(modifier = Modifier.height(8.dp))

                Text(text = product.harga.toString())
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        modifier = Modifier
                            .height(33.dp)
                            .width(56.dp),
                        onClick = { jumlahProductLocal.value-- }
                    ) {
                        Text(text = "-")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = jumlahProductLocal.value.toString(), fontSize = 24.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        modifier = Modifier
                            .height(33.dp)
                            .width(56.dp),
                        onClick = { jumlahProductLocal.value++ }
                    ) {
                        Text(text = "+")
                    }
                }
            }
        }
    }
}