package com.topibatu.tanilink.View

import android.annotation.SuppressLint
import android.os.Build
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.topibatu.tanilink.View.components.BottomBar
import com.topibatu.tanilink.View.components.DatePickerDialogComponent
import com.topibatu.tanilink.View.components.TopBar
import io.grpc.StatusException
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter


@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun PredictionPage(navController: NavController) {
    // Graph Data
    val context = LocalContext.current
    val data = listOf("2022-07-01" to 2f, "2022-07-02" to 6f, "2022-07-04" to 4f).associate { (dateString, yValue) ->
        LocalDate.parse(dateString) to yValue
    }
    val xValuesToDates = data.keys.associateBy { it.toEpochDay().toFloat() }
    val chartEntryModel = entryModelOf(xValuesToDates.keys.zip(data.values, ::entryOf))
    val dateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("d MMM")
    val horizontalAxisValueFormatter = AxisValueFormatter<AxisPosition.Horizontal.Bottom> { value, _ ->
        (xValuesToDates[value] ?: LocalDate.ofEpochDay(value.toLong())).format(dateTimeFormatter)
    }

    // Filter Var
    // Commodity Input Var
    var expandedCommodity by remember { mutableStateOf(false) }
    val commodityList = arrayOf("Beras Medium", "Cabai Rawit Merah", "Cabai Merah Keriting")
    val commodityState = remember { mutableStateOf(commodityList[0]) }
    // Date (Year and Month) Var
    var showDatePicker by remember { mutableStateOf(false) }
    val dateState = remember { mutableStateOf("06/2023") }
    // Area Var
    var expandedArea by remember { mutableStateOf(false) }
    val areaList = arrayOf("Semarang")
    val areaState = remember { mutableStateOf(areaList[0]) }

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
            Text(text = "Grafik Data ${commodityState.value}", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))

            // Chart
            Chart(
                chart = lineChart(),
                model = chartEntryModel,
                startAxis = rememberStartAxis(),
                bottomAxis = rememberBottomAxis(valueFormatter = horizontalAxisValueFormatter)
            )
            Spacer(modifier = Modifier.height(12.dp))

            // Filter
            Column {
                // Commodity
                ExposedDropdownMenuBox(
                    expanded = expandedCommodity,
                    onExpandedChange = { expandedCommodity = !expandedCommodity },
                ) {
                    OutlinedTextField(
                        value = commodityState.value,
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
                        commodityList.forEach { item ->
                            DropdownMenuItem(
                                text = { Text(text = item) },
                                onClick = {
                                    commodityState.value = item
                                    expandedCommodity = false
                                }
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))

                // Date (Year and Month)
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.clickable(onClick = {
                        showDatePicker = true
                    })
                ) {
                    OutlinedTextField(
                        value = dateState.value,
                        placeholder = { Text("Bulan/Tahun") },
                        label = { Text("Bulan/Tahun") },
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
                        datePattern = "MMM/yyyy",
                        onDateSelected = { dateState.value = it },
                        onDismiss = { showDatePicker = false }
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))

                // Area
                ExposedDropdownMenuBox(
                    expanded = expandedArea,
                    onExpandedChange = { expandedArea = !expandedArea },
                ) {
                    OutlinedTextField(
                        value = areaState.value,
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
                        areaList.forEach { item ->
                            DropdownMenuItem(
                                text = { Text(text = item) },
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