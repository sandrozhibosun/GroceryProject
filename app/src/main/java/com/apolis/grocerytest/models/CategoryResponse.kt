package com.apolis.grocerytest.models

data class CategoryResponse(
    val count: Int,
    val data: ArrayList<Category>,
    val error: Boolean
)