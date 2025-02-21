package com.admin.shoppinappadmin.common


sealed class PageEvent< out T> {
    object Loading:PageEvent<Nothing>()
    data class Success<T>(val success:T):PageEvent<T>()
    data class Error(val error:String):PageEvent<Nothing>()
}
sealed class SubmitEvent{
    object Submit:SubmitEvent()
}


