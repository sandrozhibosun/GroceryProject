package com.apolis.grocerytest.models

import java.io.Serializable

data class Address(
    val __v: Int=0,
    val _id: String,
    val city: String,
    val houseNo: String,
    val pincode: Int,
    val streetName: String,
    val type: String,
    val userId: String
):Serializable{
    companion object{
        const val Address_Key="ADDRESS"
    }
}