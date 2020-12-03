package com.apolis.grocerytest.models

data class AddressListResponse(
    val count: Int,
    val data: ArrayList<Address>,
    val error: Boolean
)