package com.apolis.grocerytest.helper

import android.content.Context
import com.apolis.grocerytest.models.Address
import com.apolis.grocerytest.models.User

class SessionManager(var mContext: Context){

    private val FILE_NAME = "login_pref"
    private val Key_UserId="UserId"
    private val KEY_NAME = "name"
    private val KEY_EMAIL = "email"
    private val KEY_PASSWORD = "password"
    private val KEY_Mobile = "mobile"
    private val KEY_IS_LOGGED_IN = "isLoggedIn"

    private val Key_ADDRESSID="AddressID"
    private val KEY_PINCODE="PINCODE"
    private val KEY_STREETNAME="STREETNAME"
    private val Key_City="CITY"
    private val KEY_HOUSENO="HOUSENO"
    private val KEY_TYPE="TYPE"

    private val KEY_HAS_DEFAULT_ADDRESS="setDefaultAddreess"

    var sharedPreferences = mContext.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
    var editor = sharedPreferences.edit()


    fun isLoggedIn(): Boolean{
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false)
    }

    fun register(user: User){
        editor.putString(Key_UserId, user.userId)
        editor.putString(KEY_NAME, user.name)
        editor.putString(KEY_EMAIL, user.email)
        editor.putString(KEY_PASSWORD, user.password)
        editor.putString(KEY_Mobile,user.mobile)
        editor.putBoolean(KEY_IS_LOGGED_IN, true)
        editor.commit()
    }

    fun login(user: User): Boolean{

        editor.putString(Key_UserId, user.userId)
        editor.putString(KEY_NAME, user.name)
        editor.putString(KEY_EMAIL, user.email)
        editor.putString(KEY_PASSWORD, user.password)
        editor.putString(KEY_Mobile,user.mobile)
        editor.putBoolean(KEY_IS_LOGGED_IN, true)
        editor.commit()
        return true
    }

    fun getUserName(): String?{
        return sharedPreferences.getString(KEY_NAME, null)
    }
    fun getUserEmail(): String?{
        return sharedPreferences.getString(KEY_EMAIL, null)
    }
    fun getUserId():String?{
        return sharedPreferences.getString(Key_UserId, null)
    }

    fun getUser(): User{
        var userId=sharedPreferences.getString(Key_UserId,null)
        var name = sharedPreferences.getString(KEY_NAME, null)
        var email = sharedPreferences.getString(KEY_EMAIL,null)
        var password = sharedPreferences.getString(KEY_PASSWORD, null)
        var mobile=sharedPreferences.getString(KEY_Mobile,null)
        var user = User(userId!!,name, email, password,mobile)
        return user
    }
    fun setDefaultAddress(address: Address):Boolean{
        editor.putString(Key_ADDRESSID,address._id)
        editor.putInt(KEY_PINCODE, address.pincode)
        editor.putString(KEY_STREETNAME, address.streetName)
        editor.putString(Key_City, address.city)
        editor.putString(KEY_HOUSENO, address.houseNo)
        editor.putString(KEY_TYPE, address.type)
        editor.putBoolean(KEY_HAS_DEFAULT_ADDRESS,true)
        editor.commit()
        return true

    }
    fun getDefaultAddress():Address{
        var _id=sharedPreferences.getString(Key_ADDRESSID,null)
        var pincode=sharedPreferences.getInt(KEY_PINCODE,0)
        var streetName=sharedPreferences.getString(KEY_STREETNAME,null)
        var city=sharedPreferences.getString(Key_City,null)
        var houseNo=sharedPreferences.getString(KEY_HOUSENO,null)
        var type=sharedPreferences.getString(KEY_TYPE,null)
        var userId=sharedPreferences.getString(Key_UserId,null)
        var address=Address(_id=_id!!,pincode = pincode!!,streetName = streetName!!,city = city!!,houseNo = houseNo!!
        ,type = type!!,userId = userId!!)
        return address

    }
    fun hasSetDefaultAddress():Boolean{
        return sharedPreferences.getBoolean(KEY_HAS_DEFAULT_ADDRESS, false)
    }

    fun logout(){
        editor.clear()
        editor.commit()
    }

}