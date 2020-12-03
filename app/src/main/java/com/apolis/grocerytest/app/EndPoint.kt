package com.apolis.grocerytest.app

class EndPoint{

    companion object{

        private val URL_CATEGORY="category"
        private val URL_SUB_CATEGORY="subcategory/"
        private val URL_PRODUCT_BY_SUB_ID="products/sub/"
        private val URL_REGISTER="auth/register"
        private val URL_LOGIN="auth/login"
        private val URL_ADDRESS="address/"
        private val URL_ADDADDRESS="address"
        private val URL_Order="orders"


        fun getCategory():String{
            return "${Config.BASE_URL+ URL_CATEGORY}"
        }
        fun getSubCategoryByCatId(catId:Int):String{
            return "${Config.BASE_URL+ URL_SUB_CATEGORY+catId}"
        }
        fun getProductBySubId(subId:Int):String{
            return "${Config.BASE_URL+ URL_PRODUCT_BY_SUB_ID+subId}"
        }
        fun getRegister():String{
            return "${Config.BASE_URL+ URL_REGISTER}"
        }
        fun getLogin():String{
            return "${Config.BASE_URL+ URL_LOGIN}"
        }
        fun getAddress(userId:String):String{
            return "${Config.BASE_URL+ URL_ADDRESS+userId}"
        }
        fun addAddress():String{
            return "${Config.BASE_URL+ URL_ADDADDRESS}"
        }
        fun deleteAddress(Id:String):String{
            return "${Config.BASE_URL+ URL_ADDRESS+Id}"
        }
        fun postOrder():String{
           return "${Config.BASE_URL+ URL_Order}"
        }
        fun getOrderByUserId(userId: String):String{
            return "${Config.BASE_URL+ URL_Order+"/"+userId}"
        }



    }

}