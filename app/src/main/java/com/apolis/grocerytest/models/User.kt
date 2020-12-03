package com.apolis.grocerytest.models

data class User(
    val userId: String,
    var name: String? = null,
    var email: String? = null,
    var password: String? = null,
    var mobile:String?=null
)