package com.apolis.grocerytest.models

import java.io.Serializable

data class OrderHistory(
    val __v: Int,
    val _id: String,
    val date: String,
    val orderStatus: String,
    val orderSummary: OrderSummary,
    val products: ArrayList<OrderProduct?>,
    val shippingAddress: ShippingAddress,
    val user: UserXX,
    val userId: String
):Serializable{
    companion object{
         const val OrderHistory_Key="ORDER_HISTORY"
    }
}