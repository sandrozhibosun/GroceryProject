package com.apolis.grocerytest.models

import java.io.Serializable

data class Product(
    val __v: Int?=null,
    val _id: String,
    val catId: Int?=null,
    val created: String?=null,
    val description: String?=null,
    val image: String,
    val mrp: Double?=null,
    val position: Int?=null,
    val price: Double,
    val productName: String,
    val quantity: Int?=null,
    val status: Boolean?=null,
    val subId: Int?=null,
    val unit: String?=null

)
    : Serializable {
    companion object{
        const val Product_Key="Product"
    }
}