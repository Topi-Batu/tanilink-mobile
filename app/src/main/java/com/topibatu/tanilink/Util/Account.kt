package com.topibatu.tanilink.Util

import account_proto.Account.LoginReq
import account_proto.Account.LoginRes
import account_proto.Account.RegisterReq
import account_proto.Account.RegisterRes
import account_proto.AccountsGrpcKt
import android.net.Uri
import io.grpc.ManagedChannelBuilder
import io.grpc.okhttp.OkHttpChannelBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asExecutor
import okhttp3.OkHttp
import okhttp3.OkHttpClient
import okhttp3.Protocol


class Account() {
    val uri: Uri = Uri.parse("http://tanilink.bantuin.me:443")

    val okHttpClient = OkHttpClient.Builder()
        .protocols(listOf(Protocol.H2_PRIOR_KNOWLEDGE))
        // Add any additional OkHttpClient configurations here
        .build()

    val channel = OkHttpChannelBuilder.forAddress(uri.host, uri.port)
        .transportExecutor(okHttpClient.dispatcher.executorService)
        .build()

    private val account = AccountsGrpcKt.AccountsCoroutineStub(channel)

    suspend fun register(name: String, email: String, password: String): RegisterRes {
        try {
            val request = RegisterReq.newBuilder()
                .setFullName(name)
                .setEmail(email)
                .setPassword(password)
                .build()
            val response = account.register(request)
            return response
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
    }

    suspend fun login(email: String, password: String): LoginRes {
        try {
            val request = LoginReq.newBuilder()
                .setEmail(email)
                .setPassword(password)
                .build()
            val response = account.login(request)
            return response
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
    }
}