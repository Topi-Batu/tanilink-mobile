package com.topibatu.tanilink.View

import android.annotation.SuppressLint
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
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
import coil.compose.AsyncImage
import com.topibatu.tanilink.Util.Photo
import com.topibatu.tanilink.View.components.TopBar

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun PaymentPage(navController: NavController) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    // Default TextField Color
    val defaultTextFieldColor: TextFieldColors = OutlinedTextFieldDefaults.colors(
        disabledTextColor = MaterialTheme.colorScheme.onSurface,
        disabledContainerColor = Color.Transparent,
        disabledBorderColor = MaterialTheme.colorScheme.outline,
        disabledLeadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
        disabledTrailingIconColor = MaterialTheme.colorScheme.onSurface,
        disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
        disabledPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
        disabledSupportingTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
        disabledPrefixColor = MaterialTheme.colorScheme.onSurfaceVariant,
        disabledSuffixColor = MaterialTheme.colorScheme.onSurfaceVariant
    )

    // Photo Upload Service
    var uri by remember { mutableStateOf<Uri?>(null) }
    val photoServices = remember { Photo() }
    val singlePhotoPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = {
            uri = it
        }
    )

    Scaffold(
        topBar = {
            TopBar(navController, "Cart Detail")
        },
    ) {
        Column (
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
        ) {
            Text(text = "Pembayaran", fontSize = 24.sp, fontWeight = FontWeight.ExtraBold)
            Spacer(modifier = Modifier.height(12.dp))
            
            // Payment QRIS Image
            AsyncImage(
                model = "https://firebasestorage.googleapis.com/v0/b/topibatu-2a076.appspot.com/o/assets%2Fpayment_qris.jpg?alt=media&token=17c7132a-4952-4800-8767-64537c9978ee",
                contentDescription = "QRIS Payment",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .height(300.dp)
                    .width(200.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .border(2.dp, Color.Gray, RoundedCornerShape(18.dp))
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Informasi Pembayaran
            Column {
                Text(text = "Informasi Pembayaran", fontWeight = FontWeight.ExtraBold)
                Text(text = "Biaya Item: Rp. {biayaItem}")
                Text(text = "Biaya Ongkos Kirim: Rp. {ongkosKirim}")
                Text(
                    text = "Total Pembayaran: Rp. {totalPembayaran}",
                    fontWeight = FontWeight.SemiBold
                )
            }
            Spacer(modifier = Modifier.height(18.dp))

            // Upload Bukti Bayar
            Row (
                modifier = Modifier.padding(horizontal = 12.dp)
            ){
                OutlinedTextField(
                    value = "Upload Bukti Bayar",
                    onValueChange = {  },
                    enabled = false,
                    colors = defaultTextFieldColor,
                    shape = RoundedCornerShape(32.dp),
                    modifier = Modifier.clickable {
                        // Pick Image
                        singlePhotoPicker.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    }
                )
                
                // Cek Uploaded Bukti Bayar
                Spacer(modifier = Modifier.width(12.dp))
                Button(
                    onClick = { /*TODO*/ }
                ) {
                    Text(text = "Cek Foto")
                }
            }
            

            // Bayar
            Button(onClick = { /*TODO*/ }) {
                Text(text = "Bayar")
            }
        }
    }
}