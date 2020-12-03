package com.apolis.grocerytest.models

data class SubCategoryResponse(
    val count: Int,
    val data: ArrayList<SubCategory>,
    val error: Boolean
)