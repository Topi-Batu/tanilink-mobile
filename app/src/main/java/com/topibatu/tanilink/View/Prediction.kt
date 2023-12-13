package com.topibatu.tanilink.View

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.patrykandpatrick.vico.compose.axis.horizontal.rememberBottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.rememberStartAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.core.axis.AxisPosition
import com.patrykandpatrick.vico.core.axis.formatter.AxisValueFormatter
import com.patrykandpatrick.vico.core.entry.entryModelOf
import com.patrykandpatrick.vico.core.entry.entryOf
import com.patrykandpatrick.vico.views.chart.line.lineChart
import com.topibatu.tanilink.View.components.BottomBar
import com.topibatu.tanilink.View.components.TopBar
import java.time.LocalDate
import java.time.format.DateTimeFormatter


@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun PredictionPage(navController: NavController) {
    // TODO: Cari graph visualizer

    Scaffold(
        topBar = {
            TopBar(navController, "Prediction Prices")
        },
        bottomBar = {
            BottomBar(navController, 2)
        }
    ) {
        Row (
            modifier = Modifier
                .padding(top = 70.dp, start = 16.dp, end = 16.dp)
        ) {
            val data = listOf("2022-07-01" to 2f, "2022-07-02" to 6f, "2022-07-04" to 4f).associate { (dateString, yValue) ->
                LocalDate.parse(dateString) to yValue
            }
            val xValuesToDates = data.keys.associateBy { it.toEpochDay().toFloat() }
            val chartEntryModel = entryModelOf(
                xValuesToDates.keys.toList().zip(data.values.toList()) { x, y -> entryOf(x, y) }
            )
            val dateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("MMM-YY")
            val horizontalAxisValueFormatter = AxisValueFormatter<AxisPosition.Horizontal.Bottom> { value, _ ->
                (xValuesToDates[value] ?: LocalDate.ofEpochDay(value.toLong())).format(dateTimeFormatter)
            }

            Chart(
                chart = lineChart(LocalContext.current),
                model = chartEntryModel,
                startAxis = rememberStartAxis(),
                bottomAxis = rememberBottomAxis(valueFormatter = horizontalAxisValueFormatter)
            )
        }
    }
}