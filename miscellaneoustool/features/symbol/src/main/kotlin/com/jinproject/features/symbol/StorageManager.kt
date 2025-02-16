package com.jinproject.features.symbol

import androidx.core.net.toUri
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ListResult
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.ktx.storage
import com.google.firebase.storage.storageMetadata
import com.jinproject.features.core.AuthManager
import com.jinproject.features.core.AuthManager.uid
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

internal object StorageManager {
    private val storageRef get() = Firebase.storage("gs://twom-collection-server.firebasestorage.app").reference
    private val userPath = storageRef.child("users").child(AuthManager.uid!!)

    suspend fun getImageList(count: Int = 50, pageToken: String?): ListResult =
        suspendCancellableCoroutine { cont ->
            val imagePath = userPath

            val imageResults = pageToken?.let {
                imagePath.list(count, it)
            } ?: imagePath.list(count)

            imageResults.addOnCompleteListener { result ->
                if (result.isSuccessful)
                    cont.resume(result.result)
                else
                    cont.resumeWithException(result.exception?.cause ?: Throwable("이미지 다운로드 실패"))
            }

        }

    suspend fun uploadImage(uri: String, fileName: String): UploadTask.TaskSnapshot = suspendCancellableCoroutine { cont ->
        val imagePath = userPath.child("$fileName.jpg")

        val metadata = storageMetadata {
            contentType = "image/jpg"
        }

        imagePath.putFile(uri.toUri(), metadata).addOnCompleteListener { result ->
            if (result.isSuccessful)
                cont.resume(result.result)
            else
                cont.resumeWithException(result.exception?.cause ?: Throwable("이미지 업로드 실패"))
        }
    }
}