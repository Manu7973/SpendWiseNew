package com.mine.expensetracker.featureWallet.repositery

import android.content.Context
import android.util.Log
import androidx.compose.ui.platform.LocalContext
import com.expense.expensetracker.utils.CustomToast
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.mine.expensetracker.data.prefrences.SharedPref
import com.mine.expensetracker.featureWallet.data.FriendData
import com.mine.expensetracker.utils.Constants
import com.google.firebase.database.ValueEventListener

class FriendsRepository(
    context: Context,
    private val db: DatabaseReference = FirebaseDatabase.getInstance().reference,
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
) {

    private val currentUid = SharedPref.getString(context, Constants.UID)

    //Add friend
    fun addFriendByUniqueId(uniqueId: String, onComplete: (Boolean, String) -> Unit) {
        if (currentUid == null) {
            onComplete(false, "User not logged in")
        }

        if (currentUid == uniqueId) {
            onComplete(false, "You can't add yourself as a friend")
            return
        }

        db.child("users").child(uniqueId).get()
            .addOnSuccessListener { snapshot ->
                if (snapshot.exists()) {
                    val friendUid = snapshot.key ?: return@addOnSuccessListener
                    db.child("users").child(currentUid).child("friends").child(friendUid)
                        .setValue(true)
                    db.child("users").child(friendUid).child("friends").child(currentUid)
                        .setValue(true)
                    onComplete(true, "null")
                } else {
                    onComplete(false, "Friend not found")
                }
            }
            .addOnFailureListener { e ->
                Log.e("FriendsRepo", "Failed to fetch user", e)
                onComplete(false, "Failed to fetch user")
            }
    }


    //Fetch list of friends.
    fun fetchFriends(onResult: (List<FriendData>) -> Unit) {
        if (currentUid == null) return

        db.child("users").child(currentUid).child("friends")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val friends = mutableListOf<FriendData>()
                    val tasks = snapshot.children.mapNotNull { friendSnap ->
                        val friendUid = friendSnap.key ?: return@mapNotNull null
                        db.child("users").child(friendUid).get()
                    }

                    Tasks.whenAllSuccess<DataSnapshot>(tasks)
                        .addOnSuccessListener { results ->
                            results.forEachIndexed { i, userSnap ->
                                val friendUid =
                                    snapshot.children.elementAt(i).key ?: return@forEachIndexed
                                val name = userSnap.child("name").value as? String ?: "Unknown"

                                val pairId =
                                    listOf(currentUid, friendUid).sorted().joinToString("_")
                                db.child("balances").child(pairId).get()
                                    .addOnSuccessListener { balSnap ->
                                        val balance =
                                            (balSnap.child(currentUid).getValue(Double::class.java)
                                                ?: 0.0)
                                        friends.add(FriendData(friendUid, name, balance))
                                        // ✅ Emit updated list
                                        onResult(friends.toList())
                                    }
                            }
                        }
                }

                override fun onCancelled(error: DatabaseError) {}
            })
    }

}

