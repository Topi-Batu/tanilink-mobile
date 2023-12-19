package com.topibatu.tanilink.View

import android.annotation.SuppressLint
import android.content.res.Resources.Theme
import android.widget.Toast
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
import com.topibatu.tanilink.View.components.ProductItem
import com.topibatu.tanilink.View.components.TopBar
import io.grpc.StatusException
import kotlinx.coroutines.launch

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
            BottomBar(navController, -1)
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate("cart_detail")
                }
            ) {
                Icon(Icons.Filled.ArrowForward, "Go To Cart Detail")
            }
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
                ProductItem(product = product, showCheckbox = true)
                Divider(color = MaterialTheme.colorScheme.primary, thickness = 1.dp)
            }
        }

    }
}

