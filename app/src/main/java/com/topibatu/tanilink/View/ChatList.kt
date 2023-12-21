package com.topibatu.tanilink.View

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.patrykandpatrick.vico.compose.axis.horizontal.rememberBottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.rememberStartAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.core.axis.AxisPosition
import com.patrykandpatrick.vico.core.axis.formatter.AxisValueFormatter
import com.patrykandpatrick.vico.core.entry.entryModelOf
import com.patrykandpatrick.vico.core.entry.entryOf
import com.patrykandpatrick.vico.views.chart.line.lineChart
import com.topibatu.tanilink.View.components.BottomBar
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import com.topibatu.tanilink.View.components.TopBar

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ChatListPage(navController: NavController) {

    Scaffold(
        topBar = {
            TopBar(navController, "Chat")
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
            itemsIndexed(List(10) { "" }) { index, tmp ->
                ChatBox(
                    navController = navController,
                    photoUrl = "https://firebasestorage.googleapis.com/v0/b/topibatu-2a076.appspot.com/o/assets%2Fdefault_profile_picture.png?alt=media&token=0135a63e-af90-4c35-8109-d48a9efaf3be",
                    username = "Pak Tani",
                    lastMessage = "Masih belum panen"
                )
            }
        }

    }
}

@Composable
private fun ChatBox(navController: NavController, photoUrl: String, username: String, lastMessage: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                navController.navigate("chat")
            }
    ) {
        AsyncImage(
            model = photoUrl,
            contentDescription = "avatar",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(62.dp)
                .clip(CircleShape)
                .border(2.dp, Color.Gray, CircleShape)
        )
        Column(
            modifier = Modifier
                .padding(start = 8.dp)
                .weight(1f)
        ) {
            Text(
                text = "${username}\n" + lastMessage,
                lineHeight = 32.sp
            )
        }
        Text(
            text = "00.00",
            modifier = Modifier.align(Alignment.Top)
        )
    }

}