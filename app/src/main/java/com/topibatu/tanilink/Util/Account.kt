package com.topibatu.tanilink.Util

import account_proto.Account.RegisterReq
import account_proto.Account.RegisterRes
import account_proto.AccountsGrpcKt
import android.net.Uri
import io.grpc.ManagedChannelBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asExecutor


class Account() {
    private val channel = let {
        val uri: Uri = Uri.parse("http://tanilink.bantuin.me:443")

        println("Connecting to ${uri.host}:${uri.port}")

        val builder = ManagedChannelBuilder.forAddress(uri.host, uri.port)
        if (uri.scheme == "https") {
            builder.useTransportSecurity()
        } else {
            builder.usePlaintext()
        }

        builder.executor(Dispatchers.IO.asExecutor()).build()
    }

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
}