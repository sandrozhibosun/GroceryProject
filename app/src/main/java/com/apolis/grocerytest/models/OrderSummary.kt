package com.apolis.grocerytest.models

import java.io.Serializable

data class OrderSummary(
    val _id: String,
    val deliveryCharges: Int,
    val discount: Int,
    val orderAmount: Int,
    val ourPrice: Int,
    val totalAmount: Int
): Serializable