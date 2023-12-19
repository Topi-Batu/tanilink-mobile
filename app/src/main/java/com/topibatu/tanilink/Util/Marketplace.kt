package com.topibatu.tanilink.Util

import account_proto.AccountProto
import account_proto.AccountsGrpcKt
import android.net.Uri
import com.google.protobuf.Empty
import com.orhanobut.hawk.Hawk
import com.topibatu.tanilink.View.Product
import io.grpc.Metadata
import io.grpc.StatusException
import io.grpc.okhttp.OkHttpChannelBuilder
import marketplace_proto.MarketplaceGrpcKt
import marketplace_proto.MarketplaceProto
import marketplace_proto.MarketplaceProto.AllCommodityDetails
import marketplace_proto.MarketplaceProto.AllProductDetails
import marketplace_proto.MarketplaceProto.IdReq
import okhttp3.OkHttpClient
import okhttp3.Protocol

class Marketplace() {
    val uri: Uri = Uri.parse("http://tanilink.bantuin.me:443")

    val okHttpClient = OkHttpClient.Builder()
        .protocols(listOf(Protocol.H2_PRIOR_KNOWLEDGE))
        // Add any additional OkHttpClient configurations here
        .build()

    val channel = OkHttpChannelBuilder.forAddress(uri.host, uri.port)
        .transportExecutor(okHttpClient.dispatcher.executorService)
        .build()

    private val marketplace = MarketplaceGrpcKt.MarketplaceCoroutineStub(channel)

    val headers = Metadata().apply {
        put(Metadata.Key.of("Authorization", Metadata.ASCII_STRING_MARSHALLER), "Bearer ${Hawk.get<String>("access-token")}")
    }

    suspend fun getAllCommodities(): AllCommodityDetails {
        try {
            val request = Empty.newBuilder().build()
            val response = marketplace.getAllCommodities(request)
            return response
        } catch (e: StatusException) {
            e.printStackTrace()
            throw e
        }
    }

    suspend fun getProductByCommodityId(commodityId: String): AllProductDetails {
        try {
            val request = IdReq.newBuilder()
                .setId(commodityId)
                .build()
            val response = marketplace.getProductByCommodityId(request)
            return response
        } catch (e: StatusException) {
            e.printStackTrace()
            throw e
        }
    }

    suspend fun getProductById(productId: String): MarketplaceProto.ProductDetail {
        try {
            val request = IdReq.newBuilder()
                .setId(productId)
                .build()
            val response = marketplace.getProductById(request)
            return response
        } catch (e: StatusException) {
            e.printStackTrace()
            throw e
        }
    }

    suspend fun searchProduct(query: String): MarketplaceProto.AllProductDetails {
        try {
            val request = MarketplaceProto.Query.newBuilder()
                .setQuery(query)
                .build()
            val response = marketplace.searchProduct(request)
            return response
        } catch (e: StatusException) {
            e.printStackTrace()
            throw e
        }
    }


}