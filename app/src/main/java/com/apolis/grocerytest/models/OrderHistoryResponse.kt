package com.apolis.grocerytest.models

data class OrderHistoryResponse(
    val count: Int,
    val data: ArrayList<OrderHistory>,
    val error: Boolean
)