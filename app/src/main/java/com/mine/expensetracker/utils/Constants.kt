package com.mine.expensetracker.utils

class Constants {

    companion object{
        const val USERNAME: String = "USERNAME"
        const val EMAIL: String = "EMAIL"
        const val UID: String = "UID"
        const val PROFILE_URI: String = "PROFILE_URI"
        const val DOB: String = "DOB"
        const val GENDER: String = "GENDER"

        const val DESCRIPTION_ADDED: String = "DESCRIPTION_ADDED"
        const val LOGIN_TYPE: String = "LOGIN_TYPE" // 1 - UserName, 2 - Google`, 3 -Guest
        const val LOGGED_IN_TOKEN: String = "LOGGED_IN_TOKEN"
        const val STORAGE_PREF: String = "STORAGE_PREF" // true - online, false - offline

        const val NOTIFICATION: String = "NOTIFICATION"
        const val REMINDERS: String = "REMINDERS"
        const val DARK_MODE: String = "DARK_MODE"

        //DATA
        const val PROFILE_SCREEN: String = "{\"profileSettings\":[{\"id\":1,\"order\":1,\"displayName\":\"Edit Profile\",\"isActive\":true,\"isOpenable\":true,\"category\":\"normal\"},{\"id\":2,\"order\":2,\"displayName\":\"Settings\",\"isActive\":true,\"isOpenable\":true,\"category\":\"normal\"},{\"id\":3,\"order\":3,\"displayName\":\"Notifications\",\"isActive\":true,\"isOpenable\":true,\"category\":\"normal\"},{\"id\":4,\"order\":4,\"displayName\":\"Share\",\"isActive\":true,\"isOpenable\":false,\"category\":\"normal\"},{\"id\":5,\"order\":6,\"displayName\":\"Logout\",\"isActive\":true,\"isOpenable\":false,\"category\":\"danger\"},{\"id\":6,\"order\":5,\"displayName\":\"About\",\"isActive\":true,\"category\":\"normal\",\"isOpenable\":true}]}"
    }
}