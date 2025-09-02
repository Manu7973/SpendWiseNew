package com.mine.expensetracker.featureWallet.ViewModelFactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mine.expensetracker.featureWallet.repositery.FriendsRepository
import com.mine.expensetracker.featureWallet.viewmodel.FriendsViewModel

class FriendsViewModelFactory(
    private val repo: FriendsRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FriendsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FriendsViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}