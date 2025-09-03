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
        val myId = currentUid ?: return

        db.child("users").child(myId).child("friends")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val friends = mutableListOf<FriendData>()

                    snapshot.children.forEach { friendSnap ->
                        val friendUid = friendSnap.key ?: return@forEach

                        // 🔹 Listen to friend's name
                        db.child("users").child(friendUid).child("name")
                            .addValueEventListener(object : ValueEventListener {
                                override fun onDataChange(nameSnap: DataSnapshot) {
                                    val name = nameSnap.getValue(String::class.java) ?: "Unknown"

                                    // 🔹 Listen to balance for this friend
                                    val pairId = listOf(myId, friendUid).sorted().joinToString("_")
                                    db.child("balances").child(pairId)
                                        .addValueEventListener(object : ValueEventListener {
                                            override fun onDataChange(balSnap: DataSnapshot) {
                                                val balance =
                                                    balSnap.child(myId).getValue(Double::class.java)
                                                        ?: 0.0

                                                // ✅ Update friend object
                                                val existingIndex = friends.indexOfFirst { it.uid == friendUid }
                                                if (existingIndex != -1) {
                                                    friends[existingIndex] =
                                                        friends[existingIndex].copy(
                                                            name = name,
                                                            balance = balance
                                                        )
                                                } else {
                                                    friends.add(FriendData(friendUid, name, balance))
                                                }

                                                onResult(friends.toList())
                                            }

                                            override fun onCancelled(error: DatabaseError) {}
                                        })
                                }

                                override fun onCancelled(error: DatabaseError) {}
                            })
                    }
                }

                override fun onCancelled(error: DatabaseError) {}
            })
    }


    fun removeFriend(uniqueId: String, onComplete: (Boolean, String) -> Unit) {
        val myId = currentUid ?: return onComplete(false, "User not logged in")

        val updates = hashMapOf<String, Any?>(
            "users/$myId/friends/$uniqueId" to null,
            "users/$uniqueId/friends/$myId" to null
        )

        db.updateChildren(updates)
            .addOnSuccessListener {
                onComplete(true, "Friend removed successfully")
            }
            .addOnFailureListener { e ->
                onComplete(false, e.message ?: "Failed to remove friend")
            }
    }


}

