package com.topibatu.tanilink.Util

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import com.orhanobut.hawk.Hawk
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.UUID

class Photo {
    suspend fun uploadToStorage(uri: Uri, context: Context, type: String): Deferred<String> = coroutineScope {
        async {
            val storage = Firebase.storage
            // Create a storage reference from our app
            val storageRef = storage.reference

            val unique_image_name = Hawk.get<String>("user-id")
            var spaceRef: StorageReference

            if (type == "image") {
                spaceRef = storageRef.child("images/$unique_image_name.jpg")
            } else {
                spaceRef = storageRef.child("videos/$unique_image_name.mp4")
            }

            val byteArray: ByteArray? = context.contentResolver
                .openInputStream(uri)
                ?.use { it.readBytes() }

            return@async if (byteArray != null) {
                try {
                    val uploadTask = spaceRef.putBytes(byteArray).await()
                    // Save image
                    val imageUrl = spaceRef.downloadUrl.await().toString()

                    // Display success message
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            context,
                            "Upload Image Successed",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    imageUrl
                } catch (e: Exception) {
                    // Handle any exceptions
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            context,
                            "Upload Image Failed: ${e.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    ""
                }
            } else {
                ""
            }
        }
    }


}

