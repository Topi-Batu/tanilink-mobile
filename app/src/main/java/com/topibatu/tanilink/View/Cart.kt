package com.topibatu.tanilink.View

import android.annotation.SuppressLint
import android.content.res.Resources.Theme
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.topibatu.tanilink.View.components.BottomBar
import com.topibatu.tanilink.View.components.TopBar

data class Product(
    var namaToko: String,
    var namaProduk: String,
    var fotoProduk: String,
    var harga: Int,
    var jumlahProduk: Int,
    var isChecked: Boolean
)

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun CartPage(navController: NavController) {

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

    Scaffold(
        topBar = {
            TopBar(navController, "Cart")
        },
        bottomBar = {
            BottomBar(navController, 1)
        }
    ) {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = LocalConfiguration.current.screenWidthDp.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, end = 8.dp, top = 70.dp, bottom = 90.dp)
        ) {
            itemsIndexed(products) { index, product ->
                ProductItems(product = product)
                Divider(color = MaterialTheme.colorScheme.primary, thickness = 1.dp)
            }
        }

    }
}

@Composable
fun ProductItems(product: Product) {
    Column (
        modifier = Modifier.padding(top = 12.dp, bottom = 28.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = product.isChecked,
                onCheckedChange = { product.isChecked = it }
            )

            Column {
                Text(text = product.namaToko)
                Spacer(modifier = Modifier.height(8.dp))
                AsyncImage(
                    model = product.fotoProduk,
                    contentDescription = "Product Image",
                    modifier = Modifier
                        .size(118.dp)
                        .clip(RoundedCornerShape(18.dp))
                        .border(2.dp, Color.Gray, RoundedCornerShape(18.dp))
                )
            }
            Spacer(modifier = Modifier.width(8.dp))

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
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
                        onClick = { product.jumlahProduk++ }
                    ) {
                        Text(text = "+")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = product.jumlahProduk.toString(), fontSize = 24.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        modifier = Modifier
                            .height(33.dp)
                            .width(56.dp),
                        onClick = { product.jumlahProduk-- }
                    ) {
                        Text(text = "-")
                    }
                }
            }
        }
    }
}