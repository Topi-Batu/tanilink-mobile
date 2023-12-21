package com.topibatu.tanilink.Util

import android.net.Uri
import com.google.protobuf.Empty
import com.orhanobut.hawk.Hawk
import io.grpc.Metadata
import io.grpc.StatusException
import io.grpc.okhttp.OkHttpChannelBuilder
import marketplace_proto.MarketplaceGrpcKt
import marketplace_proto.MarketplaceProto
import okhttp3.OkHttpClient
import okhttp3.Protocol
import prediction_proto.PredictionProto.AllPredictionDetail
import prediction_proto.PredictionProto.PredictionReq
import prediction_proto.PredictionsGrpcKt

class Prediction() {
    val uri: Uri = Uri.parse("http://tanilink.bantuin.me:443")

    val okHttpClient = OkHttpClient.Builder()
        .protocols(listOf(Protocol.H2_PRIOR_KNOWLEDGE))
        // Add any additional OkHttpClient configurations here
        .build()

    val channel = OkHttpChannelBuilder.forAddress(uri.host, uri.port)
        .transportExecutor(okHttpClient.dispatcher.executorService)
        .build()

    private val prediction = PredictionsGrpcKt.PredictionsCoroutineStub(channel)

    val headers = Metadata().apply {
        put(Metadata.Key.of("Authorization", Metadata.ASCII_STRING_MARSHALLER), "Bearer ${Hawk.get<String>("access-token")}")
    }


    suspend fun getPredictions(commodityId: String, areaId: String, date: String): AllPredictionDetail {
        try {
            val request = PredictionReq.newBuilder()
                .setCommodityId(commodityId)
                .setAreaId(areaId)
                .setDate(date)
                .build()
            val response = prediction.getPredictions(request)
            return response
        } catch (e: StatusException) {
            e.printStackTrace()
            throw e
        }
    }

}