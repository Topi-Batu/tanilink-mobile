package com.topibatu.tanilink.View

import account_proto.AccountProto
import account_proto.AccountProto.AreaDetail
import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.patrykandpatrick.vico.compose.axis.horizontal.bottomAxis
import com.patrykandpatrick.vico.compose.axis.horizontal.rememberBottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.rememberStartAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.column.columnChart
import com.patrykandpatrick.vico.core.axis.AxisPosition
import com.patrykandpatrick.vico.core.axis.formatter.AxisValueFormatter
import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer
import com.patrykandpatrick.vico.core.entry.entriesOf
import com.patrykandpatrick.vico.core.entry.entryModelOf
import com.patrykandpatrick.vico.core.entry.entryOf
import com.patrykandpatrick.vico.compose.chart.line.lineChart
import com.topibatu.tanilink.Util.Account
import com.topibatu.tanilink.Util.Marketplace
import com.topibatu.tanilink.Util.Prediction
import com.topibatu.tanilink.View.components.BottomBar
import com.topibatu.tanilink.View.components.DatePickerDialogComponent
import com.topibatu.tanilink.View.components.TopBar
import io.grpc.StatusException
import kotlinx.coroutines.launch
import marketplace_proto.MarketplaceProto.AllCommodityDetails
import marketplace_proto.MarketplaceProto.CommodityDetail
import prediction_proto.PredictionProto.AllPredictionDetail
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.random.Random

data class PredictionItem(
    var date: String,
    var price: Float,
)

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun PredictionPage(navController: NavController) {
    val accountRPC = remember { Account() }
    val marketPlaceRPC = remember { Marketplace() }
    val predictionRPC = remember { Prediction() }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    // Graph Data
    val predictions = remember { mutableStateListOf<PredictionItem?>(null) }
    val inputFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy")
    val outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val data = remember { mutableStateOf<Map<LocalDate, Float>>(
        listOf(
            "2022-07-01" to 4f,
            "2022-07-02" to 4f,
            "2022-07-03" to 4f,
            "2022-07-03" to 4f,
            "2022-07-04" to 4f,
            "2022-07-05" to 4f,
            "2022-07-07" to 4f,
            "2022-07-08" to 4f,
            "2022-07-09" to 4f,
            "2022-07-10" to 4f,
            "2022-07-11" to 4f,
            "2022-07-12" to 4f,
            "2022-07-13" to 4f,
            "2022-07-14" to 4f,
            "2022-07-15" to 4f,
        ).associate { (dateString, yValue) ->
            LocalDate.parse(dateString) to yValue
        }
    ) }

    val xValuesToDates = data.value.keys.associateBy { it.toEpochDay().toFloat() }
    val chartEntryModel = entryModelOf(xValuesToDates.keys.zip(data.value.values, ::entryOf))
    val dateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("d")
    val horizontalAxisValueFormatter =
        AxisValueFormatter<AxisPosition.Horizontal.Bottom> { value, _ ->
            (xValuesToDates[value]
                ?: LocalDate.ofEpochDay(value.toLong())).format(dateTimeFormatter)
        }

    val chartEntryModeltmp = entryModelOf(4f, 12f, 8f, 16f, 32f, 4f, 12f, 8f, 16f, 32f, 4f, 12f, 8f, 16f, 32f, 4f, 12f, 8f, 16f, 32f, 4f, 12f, 8f, 16f, 32f, 4f, 12f, 8f, 16f, 32f)

    // Filter Var
    // Commodity Input Var
    val getCommodityRes = remember { mutableStateOf<AllCommodityDetails?>(null) }
    var expandedCommodity by remember { mutableStateOf(false) }
    val commodityState = remember { mutableStateOf<CommodityDetail?>(null) }
    // Date (Year and Month) Var
    var showDatePicker by remember { mutableStateOf(false) }
    val dateState = remember { mutableStateOf("12/21/2023") }
    // Area Var
    val getAreaRes = remember { mutableStateOf<AccountProto.AllAreaDetails?>(null) }
    var expandedArea by remember { mutableStateOf(false) }
    val areaState = remember { mutableStateOf<AreaDetail?>(null) }

    val getPredictionRes = remember { mutableStateOf<AllPredictionDetail?>(null) }

    // Get Data
    LaunchedEffect(key1 = null) {
        // Get Area
        getAreaRes.value = try {
            accountRPC.getAllArea()
        } catch (e: StatusException) {
            Toast.makeText(
                context,
                "Get Area Failed: ${e.status.description}",
                Toast.LENGTH_SHORT
            ).show()
            null
        }
        // Set Default Area
        getAreaRes.value?.let {
            areaState.value = it.areaList[0]
        }

        // Get Commodities
        getCommodityRes.value = try {
            marketPlaceRPC.getAllCommodities()
        } catch (e: StatusException) {
            Toast.makeText(
                context,
                "Get Commodities Failed: ${e.status.description}",
                Toast.LENGTH_SHORT
            ).show()
            null
        }
        // Set Default Commodity
        getCommodityRes.value?.let {
            commodityState.value = it.commoditiesList[1]
        }

        // Get Predictions
        getPredictionRes.value = try {
            areaState.value?.id?.let { area ->
                commodityState.value?.id?.let { commodity ->
                    predictionRPC.getPredictions(
                        commodityId = commodity,
                        areaId = area,
                        date = dateState.value,
                    )
                }
            }
        } catch (e: StatusException) {
            Toast.makeText(
                context,
                "Get Predictions Failed: ${e.status.description}",
                Toast.LENGTH_SHORT
            ).show()
            null
        }
        // Set Data Predictions
        predictions.clear()
        getPredictionRes.value?.let {
            it.predictionsList.forEach { predictionDetail ->
                val date = LocalDate.parse(predictionDetail.date, inputFormatter)
                val formattedDate = date.format(outputFormatter)

                predictions.add(
                    PredictionItem(
                        date = formattedDate,
                        price = predictionDetail.price.toFloat()
                    )
                )
            }
            data.value = predictions.associate { predictionItem ->
                LocalDate.parse(predictionItem?.date) to (predictionItem?.price ?: 0f)
            }
        }

    }


    Scaffold(
        topBar = {
            TopBar(navController, "Prediction Prices")
        },
        bottomBar = {
            BottomBar(navController, 2)
        }
    ) {
        Column(
            modifier = Modifier
                .padding(top = 70.dp, start = 16.dp, end = 16.dp)
        ) {
            // Commodity Title Text
            Text(
                text = "Grafik Data ${commodityState.value?.name}",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Chart
            Chart(
                chart = lineChart(),
                model = chartEntryModel,
                startAxis = rememberStartAxis(),
                bottomAxis = rememberBottomAxis(valueFormatter = horizontalAxisValueFormatter)
//                bottomAxis = rememberBottomAxis()
            )
            Spacer(modifier = Modifier.height(12.dp))

            // Predict Button
            Button(
                onClick = {
                    /* Get New Prediction */
                    scope.launch {
                        // Get Predictions
                        getPredictionRes.value = try {
                            areaState.value?.id?.let { area ->
                                commodityState.value?.id?.let { commodity ->
                                    predictionRPC.getPredictions(
                                        commodityId = commodity,
                                        areaId = area,
                                        date = dateState.value,
                                    )
                                }
                            }
                        } catch (e: StatusException) {
                            Toast.makeText(
                                context,
                                "Get Predictions Failed: ${e.status.description}",
                                Toast.LENGTH_SHORT
                            ).show()
                            null
                        }
                        // Set Data Predictions
                        predictions.clear()
                        getPredictionRes.value?.let {
                            it.predictionsList.forEach { predictionDetail ->
                                val date = LocalDate.parse(predictionDetail.date, inputFormatter)
                                val formattedDate = date.format(outputFormatter)

                                predictions.add(
                                    PredictionItem(
                                        date = formattedDate,
                                        price = predictionDetail.price.toFloat()
                                    )
                                )
                            }
                            data.value = predictions.associate { predictionItem ->
                                LocalDate.parse(predictionItem?.date) to (predictionItem?.price ?: 0f)
                            }
                        }

                    }
                },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(text = "Predict")
            }
            Spacer(modifier = Modifier.height(12.dp))

            // Filter
            Column {
                // Commodity
                getCommodityRes.value?.let {
                    ExposedDropdownMenuBox(
                        expanded = expandedCommodity,
                        onExpandedChange = { expandedCommodity = !expandedCommodity },
                    ) {
                        OutlinedTextField(
                            value = commodityState.value?.name ?: "Null",
                            onValueChange = {},
                            label = { Text("Commodity") },
                            readOnly = true,
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedCommodity) },
                            shape = RoundedCornerShape(32.dp),
                            modifier = Modifier.menuAnchor()
                        )
                        ExposedDropdownMenu(
                            expanded = expandedCommodity,
                            onDismissRequest = { expandedCommodity = false }
                        ) {
                            it.commoditiesList.forEach { item ->
                                DropdownMenuItem(
                                    text = { Text(text = item.name) },
                                    onClick = {
                                        commodityState.value = item
                                        expandedCommodity = false
                                    }
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }

                // Date (Year and Month)
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.clickable(onClick = {
                        showDatePicker = true
                    })
                ) {
                    OutlinedTextField(
                        value = dateState.value,
                        placeholder = { Text("Bulan/Hari/Tahun") },
                        label = { Text("Bulan/Hari/Tahun") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = showDatePicker) },
                        enabled = false,
                        colors = OutlinedTextFieldDefaults.colors(
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
                        ),
                        shape = RoundedCornerShape(32.dp),
                        onValueChange = { },
                    )
                }
                if (showDatePicker) {
                    DatePickerDialogComponent(
                        datePattern = "MM/dd/yyyy",
                        onDateSelected = { dateState.value = it },
                        onDismiss = { showDatePicker = false }
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))

                // Area
                getAreaRes.value?.let {
                    ExposedDropdownMenuBox(
                        expanded = expandedArea,
                        onExpandedChange = { expandedArea = !expandedArea },
                    ) {
                        OutlinedTextField(
                            value = "${areaState.value?.provinsi}, ${areaState.value?.kota}, ${areaState.value?.kecamatan}",
                            onValueChange = {},
                            label = { Text("Area") },
                            readOnly = true,
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedArea) },
                            shape = RoundedCornerShape(32.dp),
                            modifier = Modifier.menuAnchor()
                        )
                        ExposedDropdownMenu(
                            expanded = expandedArea,
                            onDismissRequest = { expandedArea = false }
                        ) {
                            it.areaList.forEach { item ->
                                DropdownMenuItem(
                                    text = { Text(text = "${item.provinsi}, ${item.kota}, ${item.kecamatan}") },
                                    onClick = {
                                        areaState.value = item
                                        expandedArea = false
                                    }
                                )
                            }
                        }
                    }
                }

            }
        }
    }
}