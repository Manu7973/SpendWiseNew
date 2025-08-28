package com.mine.expensetracker.featureLogin.repositery

import android.content.Context
import android.content.Intent
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
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
            }else{
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
            if(result.user!=null){
                saveUID(result.user?.uid ?: "")
                return true
            }else{
                return false
            }
        } catch (e: Exception) {
            false
        }
    }
}