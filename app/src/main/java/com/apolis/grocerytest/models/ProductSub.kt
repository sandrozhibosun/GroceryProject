package com.apolis.grocerytest.models

data class ProductSub(
    val count: Int,
    val data: ArrayList<Product>,
    val error: Boolean
)