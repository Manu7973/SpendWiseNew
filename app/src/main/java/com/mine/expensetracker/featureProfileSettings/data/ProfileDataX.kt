package com.mine.expensetracker.featureProfileSettings.data

data class ProfileDataX(
    val profileSettings: List<ProfileSetting>
)

data class ProfileSetting(
    val id: Int,
    val order: Int,
    val displayName: String,
    val isActive: Boolean,
    val isOpenable: Boolean = false,
    val category: String
)
