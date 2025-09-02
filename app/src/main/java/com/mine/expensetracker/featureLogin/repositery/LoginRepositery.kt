package com.mine.expensetracker.featureLogin.repositery

import android.content.Context
import android.content.Intent
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.mine.expensetracker.data.prefrences.SharedPref
import com.mine.expensetracker.utils.Constants
import kotlinx.coroutines.tasks.await

//Repository for login
class LoginRepository(private val context: Context) {
    private val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestEmail()
        .build()

    private val googleSignInClient = GoogleSignIn.getClient(context, gso)
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    fun saveUserData(userName: String) {
        SharedPref.setString(context, Constants.USERNAME, userName)
    }

    fun saveEmailData(email: String) {
        SharedPref.setString(context, Constants.EMAIL, email)
    }

    fun saveUID(id: String) {
        SharedPref.setString(context, Constants.UID, id)
    }

    fun getGoogleSignInIntent(): Intent {
        return googleSignInClient.signInIntent
    }

    fun handleGoogleSignInResult(data: Intent?): Result<GoogleSignInAccount> {
        val task = GoogleSignIn.getSignedInAccountFromIntent(data)
        return try {
            val account = task.getResult(ApiException::class.java)
            val uniqueId = account.id
            if (uniqueId != null) {
                saveUID(uniqueId)
                val userMap = mapOf(
                    "uid" to account.id,
                    "name" to account.displayName.orEmpty(),
                    "email" to account.email.orEmpty()
                )
                FirebaseDatabase.getInstance().reference
                    .child("users")
                    .child(uniqueId)
                    .setValue(userMap)
            } else {
                saveUID("")
            }
            Result.success(task.getResult(ApiException::class.java))
        } catch (e: ApiException) {
            Result.failure(e)
        }
    }

    suspend fun loginWithEmail(email: String, password: String): Boolean {
        return try {
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            val user = result.user
            if (user != null) {
                val id = user.uid
                saveUID(id)

                val userMap = mapOf(
                    "uid" to id,
                    "name" to (user.displayName ?: ""),
                    "email" to (user.email ?: email)
                )

                FirebaseDatabase.getInstance().reference
                    .child("users")
                    .child(id)
                    .setValue(userMap)

                true
            } else {
                false
            }
        } catch (e: Exception) {
            false
        }
    }
}