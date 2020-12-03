package com.apolis.grocerytest.models

import java.io.Serializable

data class ProductInCart(
    var _id:String,
    var productName:String,
    var inCart:Int=0,
    var Image:String,
    var price:Double,
    var mrp:Double
):Serializable{

}