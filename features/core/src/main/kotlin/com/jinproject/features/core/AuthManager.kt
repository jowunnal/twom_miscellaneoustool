package com.jinproject.features.core

import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.annotation.concurrent.Immutable
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

@Immutable
object AuthManager {
    private val auth get() = Firebase.auth
    private val currentUser get() = auth.currentUser

    val isEmailVerified: Boolean get() = currentUser?.isEmailVerified ?: false

    val isActive: Boolean get() = currentUser != null && isEmailVerified

    val userEmail get() = currentUser?.email

    val uid get() = currentUser?.uid

    suspend fun signInFirebaseAuth(email: String, password: String): Unit =
        suspendCancellableCoroutine { cont ->
            val authCredential = EmailAuthProvider.getCredential(email, password)
            auth.signInWithCredential(authCredential)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful)
                        cont.resume(Unit)
                    else
                        cont.resumeWithException(
                            task.exception?.cause ?: Throwable("Failed to sign in")
                        )
                }
        }

    suspend fun signUpFirebaseAuth(email: String, password: String): Unit =
        suspendCancellableCoroutine { cont ->
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { signUpTask ->
                    if (signUpTask.isSuccessful)
                        cont.resume(Unit)
                    else
                        cont.resumeWithException(
                            signUpTask.exception?.cause ?: Throwable("Failed to sign up")
                        )
                }
        }

    suspend fun sendEmailVerification(): Unit = suspendCancellableCoroutine { cont ->
        currentUser?.sendEmailVerification()?.addOnCompleteListener { verifyTask ->
            if (verifyTask.isSuccessful)
                cont.resume(Unit)
            else {
                cont.resumeWithException(
                    verifyTask.exception ?: Throwable("Failed to send email verification")
                )
            }
        }
    }

    suspend fun reAuthenticate(password: String): Unit =
        suspendCancellableCoroutine { cont ->
            if (userEmail.isNullOrBlank())
                cont.resumeWithException(Throwable("email is null"))

            val credential = EmailAuthProvider.getCredential(currentUser?.email!!, password)

            currentUser?.reauthenticate(credential)?.addOnCompleteListener { task ->
                if (task.isSuccessful)
                    cont.resume(Unit)
                else
                    cont.resumeWithException(
                        task.exception ?: Throwable("Failed to re-authenticate")
                    )
            }
        }

    suspend fun updateEmail(email: String?): Unit = suspendCancellableCoroutine { cont ->
        if (!email.isNullOrBlank())
            currentUser?.updateEmail(email)?.addOnCompleteListener { task ->
                if (task.isSuccessful)
                    cont.resume(Unit)
                else
                    cont.resumeWithException(task.exception ?: Throwable("Failed to update email"))
            }
        else
            cont.resumeWithException(Throwable("Email cannot be null or blank"))
    }

    suspend fun updatePassword(password: String?): Unit = suspendCancellableCoroutine { cont ->
        if (!password.isNullOrBlank())
            currentUser?.updatePassword(password)?.addOnCompleteListener { task ->
                if (task.isSuccessful)
                    cont.resume(Unit)
                else
                    cont.resumeWithException(
                        task.exception ?: Throwable("Failed to update password")
                    )
            }
        else
            cont.resumeWithException(Throwable("Password cannot be null or blank"))
    }

    fun signOut() {
        auth.signOut()
    }

    suspend fun deleteUser(password: String) {
        kotlin.runCatching { currentUser?.delete() }.onFailure { t ->
            if (t is FirebaseAuthRecentLoginRequiredException)
                reAuthenticate(password)
        }
    }
}