package com.jinproject.features.core

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.concurrent.CancellationException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

object RealtimeDatabaseManager {
    private val database =
        Firebase.database("https://twom-collection-server-default-rtdb.firebaseio.com/").reference
    private val aiTokenRef = database.child("ai_token").child(AuthManager.uid!!)

    suspend fun incToken(count: Long = 10, origin: Long) {
        updateAiToken(count, origin = origin)
    }

    suspend fun decToken(origin: Long) {
        require(origin > 0) {
            "토큰이 없어서 구매 실패"
        }

        updateAiToken(-1L, origin = origin)
    }

    private suspend fun updateAiToken(count: Long, origin: Long) = suspendCancellableCoroutine { cont ->
        aiTokenRef.setValue(origin + count).addOnCompleteListener { task ->
            if (task.isSuccessful)
                cont.resume(Unit)
            else
                cont.resumeWithException(task.exception ?: Throwable("토큰 업데이트 실패"))
        }
    }

    fun getAiTokenFlow(): Flow<Long> = callbackFlow {
        val callback = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val count = dataSnapshot.value as? Long ?: 0L
                trySend(count)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                cancel(CancellationException(databaseError.message))
                channel.close()
            }
        }
        aiTokenRef.addValueEventListener(callback)

        awaitClose {
            aiTokenRef.removeEventListener(callback)
        }
    }
}