package com.topibatu.tanilink.Util

import account_proto.AccountProto
import account_proto.AccountProto.AccountDetail
import account_proto.AccountProto.AddressDetail
import account_proto.AccountProto.AllAddressDetails
import account_proto.AccountProto.AllAreaDetails
import account_proto.AccountProto.BatchEditAddressReq
import account_proto.AccountProto.BatchEditAddressReq.EditAddressReq
import account_proto.AccountProto.EditProfileReq
import account_proto.AccountProto.EmailReq
import account_proto.AccountProto.RegisterReq
import account_proto.AccountProto.RegisterRes
import account_proto.AccountProto.LoginReq
import account_proto.AccountProto.LoginRes
import account_proto.AccountsGrpcKt
import account_proto.BatchEditAddressReqKt
import android.net.Uri
import com.google.protobuf.Empty
import com.orhanobut.hawk.Hawk
import com.orhanobut.hawk.Hawk.put
import io.grpc.Metadata
import io.grpc.StatusException
import io.grpc.okhttp.OkHttpChannelBuilder
import com.topibatu.tanilink.View.components.Address
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

    val headers = Metadata().apply {
        put(Metadata.Key.of("Authorization", Metadata.ASCII_STRING_MARSHALLER), "Bearer ${Hawk.get<String>("access-token")}")
    }

    suspend fun register(name: String, email: String, phoneNumber: String, gender: String, dateOfBirth: String, password: String, confirmPassword: String): RegisterRes {
        try {
            val request = RegisterReq.newBuilder()
                .setFullName(name)
                .setEmail(email)
                .setPhoneNumber(phoneNumber)
                .setGender(gender)
                .setDateOfBirth(dateOfBirth)
                .setPassword(password)
                .setConfirmPassword(confirmPassword)
                .build()
            val response = account.register(request)
            return response
        } catch (e: StatusException) {
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
        } catch (e: StatusException) {
            e.printStackTrace()
            throw e
        }
    }

    suspend fun forgotPassword(email: String): Empty {
        try {
            val request = EmailReq.newBuilder()
                .setEmail(email)
                .build()
            val response = account.forgotPassword(request)
            return response
        } catch (e: StatusException) {
            e.printStackTrace()
            throw e
        }
    }

    suspend fun isEmailConfirmed(email: String): Empty {
        try {
            val request = EmailReq.newBuilder()
                .setEmail(email)
                .build()
            val response = account.isEmailConfirmed(request)
            return response
        } catch (e: StatusException) {
            e.printStackTrace()
            throw e
        }
    }

    suspend fun resendVerificationEmail(email: String): Empty {
        try {
            val request = EmailReq.newBuilder()
                .setEmail(email)
                .build()
            val response = account.resendVerificationMail(request)
            return response
        } catch (e: StatusException) {
            e.printStackTrace()
            throw e
        }
    }

    suspend fun getProfile(): AccountDetail {
        try {
            val request = Empty.newBuilder().build()
            val response = account.getProfile(request, headers)
            return response
        } catch (e: StatusException) {
            e.printStackTrace()
            throw e
        }
    }

    suspend fun editProfile(fullName: String, phoneNumber: String, picture: String, gender: String, dateOfBirth: String): AccountDetail {
        try {
            val request = EditProfileReq.newBuilder()
                .setFullName(fullName)
                .setPhoneNumber(phoneNumber)
                .setPicture(picture)
                .setGender(gender)
                .setDateOfBirth(dateOfBirth)
                .build()
            val response = account.editProfile(request, headers)
            return response
        } catch (e: StatusException) {
            e.printStackTrace()
            throw e
        }
    }

    suspend fun getAllArea(): AllAreaDetails {
        try {
            val request = Empty.newBuilder().build()
            val response = account.getAllArea(request, headers)
            return response
        } catch (e: StatusException) {
            e.printStackTrace()
            throw e
        }
    }

    suspend fun getAddress(): AllAddressDetails {
        try {
            val request = Empty.newBuilder().build()
            val response = account.getAddress(request, headers)
            return response
        } catch (e: StatusException) {
            e.printStackTrace()
            throw e
        }
    }

    suspend fun editAddress(addresses: List<Address>): AllAddressDetails {
        try {
            val requestBuilder = BatchEditAddressReq.newBuilder()

            for (address in addresses) {
                val editAddressBuilder = EditAddressReq.newBuilder()
                    .setId(address.id ?: "")
                    .setAreaId(address.areaDetail?.id)
                    .setDetail(address.addressDetail)

                requestBuilder.addAddress(editAddressBuilder.build())
            }

            val request = requestBuilder.build()
            val response = account.editAddress(request, headers)
            return response
        } catch (e: StatusException) {
            e.printStackTrace()
            throw e
        }
    }

    suspend fun deleteAddress(addressId: String): AllAddressDetails {
        try {
            val request = AccountProto.AddressIdReq.newBuilder()
                .setAddressId(addressId)
                .build()
            val response = account.deleteAddress(request, headers)
            return response
        } catch (e: StatusException) {
            e.printStackTrace()
            throw e
        }
    }

}