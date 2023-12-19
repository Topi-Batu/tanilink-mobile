package com.topibatu.tanilink.View.components

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
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.topibatu.tanilink.View.Product

@Composable
fun ProductItem(product: Product, showCheckbox: Boolean) {
    val isCheckedLocal = remember { mutableStateOf(product.isChecked) }
    val jumlahProductLocal = remember { mutableStateOf(product.jumlahProduk) }

    Column(
        modifier = Modifier.padding(top = 12.dp, bottom = 28.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            // If showCheckBox true, Show Checkbox
            if(showCheckbox){
                Checkbox(
                    checked = isCheckedLocal.value,
                    onCheckedChange = { isCheckedLocal.value = it }
                )
            }

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