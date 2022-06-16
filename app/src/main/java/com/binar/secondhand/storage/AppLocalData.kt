package com.binar.secondhand.storage

class AppLocalData (private var storage: Storage){

    fun setUserLoggedIn(user: UserLoggedIn) {
        storage.apply {
            setString(KEY_ACCESS_TOKEN, user.accessToken)
            setString(KEY_NAME, user.name)
            setString(KEY_EMAIL, user.email)
        }
    }

    fun dropUserLoggedIn() {
        storage.apply {
            remove(KEY_ACCESS_TOKEN)
            remove(KEY_NAME)
            remove(KEY_EMAIL)
        }
    }

    val isUserHasLoggedIn: Boolean
        get() = !storage.getString(KEY_ACCESS_TOKEN).isNullOrEmpty()

    val getAccessToken: String?
        get() = storage.getString(KEY_ACCESS_TOKEN)

    val getName: String?
        get() = storage.getString(KEY_NAME)

    companion object {
        const val KEY_ACCESS_TOKEN = "access_token"
        const val KEY_NAME = "name"
        const val KEY_EMAIL = "email"
    }
}