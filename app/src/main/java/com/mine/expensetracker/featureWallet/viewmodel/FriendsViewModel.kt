package com.mine.expensetracker.featureWallet.viewmodel

import android.content.Context
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import com.expense.expensetracker.utils.CustomToast
import com.mine.expensetracker.featureWallet.data.FriendData
import com.mine.expensetracker.featureWallet.repositery.FriendsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class FriendsViewModel(private val repo: FriendsRepository) : ViewModel() {
    private val _friends = MutableStateFlow<List<FriendData>>(emptyList())
    val friends: StateFlow<List<FriendData>> = _friends

    init {
        fetchFriends()
    }

    fun fetchFriends() {
        repo.fetchFriends { list ->
            _friends.value = list
        }
    }

    fun addFriend(context: Context, uniqueId: String, onComplete: (Boolean, String) -> Unit) {
        repo.addFriendByUniqueId(uniqueId) { success, message ->
            if (!success) {
                CustomToast.showToast(context, message ?: "Something went wrong", false)
            }
        }
    }

    fun removeFriend(uniqueId: String, onComplete: (Boolean, String) -> Unit){
        repo.removeFriend(uniqueId) { success, message ->
            if (success) {
                _friends.value = _friends.value.filter { it.uid != uniqueId }
            }
            onComplete(success, message)
        }
    }
}