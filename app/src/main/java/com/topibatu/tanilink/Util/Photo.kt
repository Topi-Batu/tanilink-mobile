package com.topibatu.tanilink.Util

import android.content.Context
import android.net.Uri
import android.widget.Toast
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import java.util.UUID

class Photo {
    fun uploadToStorage(uri: Uri, context: Context, type: String, onUrlReady: (Uri?) -> Unit) {
        val storage = Firebase.storage
        // Create a storage reference from our app
        var storageRef = storage.reference

        val unique_image_name = UUID.randomUUID()
        var spaceRef: StorageReference

        if (type == "image") {
            spaceRef = storageRef.child("images/$unique_image_name.jpg")
        } else {
            spaceRef = storageRef.child("videos/$unique_image_name.mp4")
        }

        val byteArray: ByteArray? = context.contentResolver
            .openInputStream(uri)
            ?.use { it.readBytes() }

        byteArray?.let {

            var uploadTask = spaceRef.putBytes(byteArray)
            uploadTask.addOnFailureListener {
                Toast.makeText(
                    context,
                    "Upload Image Failed",
                    Toast.LENGTH_SHORT
                ).show()
                // Handle unsuccessful uploads
            }.addOnSuccessListener { taskSnapshot ->
                // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
                // ...
                // Save image
                spaceRef.downloadUrl.addOnSuccessListener { uri ->
                    // Call the callback with the URL
                    onUrlReady(uri)
                }.addOnFailureListener { exception ->
                    // Handle failure to get download URL
                    Toast.makeText(
                        context,
                        "Failed to get download URL",
                        Toast.LENGTH_SHORT
                    ).show()
                    exception.printStackTrace()
                }

                Toast.makeText(
                    context,
                    "Upload Image Successed",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    }

}
