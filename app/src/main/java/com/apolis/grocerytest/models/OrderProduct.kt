package com.apolis.grocerytest.models

import java.io.Serializable

data class OrderProduct(
 var _id: String,
 var  mrp: Double,
 var  price: Double,
 var  quantity:Int,
 var image: String
): Serializable {

}