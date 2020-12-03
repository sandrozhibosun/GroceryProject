package com.apolis.grocerytest.models

import java.io.Serializable

data class ShippingAddress(
    val _id: String,
    val city: String,
    val houseNo: String,
    val pincode: Int,
    val type: String
): Serializable