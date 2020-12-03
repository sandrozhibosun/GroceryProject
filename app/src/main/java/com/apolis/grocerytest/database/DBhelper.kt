package com.apolis.grocerytest.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.apolis.grocerytest.models.Product
import com.apolis.grocerytest.models.ProductInCart

class DBhelper(context: Context):SQLiteOpenHelper(context, Key_DATABASE_NAME,null,
    KEY_DATABASE_VERSION){

    companion object{
        const val Key_DATABASE_NAME ="myCart"
        const val KEY_DATABASE_VERSION=1
        const val TABLE_NAME="cart"
//        const val COLUMN_V="V"
        const val COLUMN_ID="ID"
//        const val COLUMN_CATID="CATID"
//        const val COLUMN_CREATED="CREATED"
//        const val COLUMN_DESCRIPTION="DESCIRPTION"
        const val COLUMN_IMAGE="IMAGE"
//        const val COLUMN_MRP="MRP"
//        const val COLUMN_POSITION="POSITION"
        const val COLUMN_PRICE="PRICE"
        const val COLUMN_MRP="MRP"
        const val COLUMN_PRODUCTNAME="PRODUCTNAME"
//        const val COLUMN_QUANTITY="QUANTITY"
//        const val COLUMN_STATUS="STATUS"
//        const val COLUMN_SUBID="SUBID"
//        const val COLUMN_UNIT="UNIT"
        const val COLUMN_INCART="INCART"

    }
//    val __v: Int,
//    val _id: String,
//    val catId: Int,
//    val created: String,
//    val description: String,
//    val image: String,
//    val mrp: Double,
//    val position: Int,
//    val price: Double,
//    val productName: String,
//    val quantity: Int,
//    val status: Boolean,
//    val subId: Int,
//    val unit: String

    override fun onCreate(sqliteDatabase: SQLiteDatabase?) {
        var createTable="create table $TABLE_NAME($COLUMN_ID char(200), " +
                "$COLUMN_PRODUCTNAME char(100), " +
                "$COLUMN_INCART char(100), " +
                "$COLUMN_IMAGE char(200), "+
                "$COLUMN_MRP REAL, "+
                "$COLUMN_PRICE REAL)"
        sqliteDatabase?.execSQL(createTable)
    }

    override fun onUpgrade(sqliteDatabase: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        var dropTable ="drop table $TABLE_NAME"
        sqliteDatabase!!.execSQL(dropTable)
        onCreate(sqliteDatabase)
    }
    fun addToCart(product: Product){

        var sqliteDatabase =writableDatabase

        var contentValue= ContentValues()
        contentValue.put(COLUMN_ID,product._id)
        contentValue.put(COLUMN_PRODUCTNAME,product.productName)
        contentValue.put(COLUMN_INCART,1)
        contentValue.put(COLUMN_IMAGE,product.image)
        contentValue.put(COLUMN_PRICE,product.price)
        contentValue.put(COLUMN_MRP,product.mrp)



        sqliteDatabase.insert(TABLE_NAME,null,contentValue)
        Log.d("abc","addtoCart")
    }

    fun productPlus(id:String){
        var db =writableDatabase
        var previousCount=getProductInCartById(id)!!.inCart
        var contentValue= ContentValues()
        contentValue.put(COLUMN_INCART,previousCount+1)

        var whereClause = "$COLUMN_ID = ?"
        var whereArgs = arrayOf(id)
        db.update(TABLE_NAME, contentValue, whereClause, whereArgs)

        Log.d("abc","addtoCart")


    }
    fun productminus(id:String){
        var db =writableDatabase
        var previousCount=getProductInCartById(id)!!.inCart
        if(previousCount>1){
        var contentValue= ContentValues()
        contentValue.put(COLUMN_INCART,previousCount-1)

        var whereClause = "$COLUMN_ID = ?"
        var whereArgs = arrayOf(id)
        db.update(TABLE_NAME, contentValue, whereClause, whereArgs)

        Log.d("abc","addtoCart")
        }
        else if(previousCount==1){
            deleteProuduct(id)
        }
        else{
            return
        }


    }
    fun deleteProuduct(id:String){
        var db = writableDatabase
        var whereClause = "$COLUMN_ID = ?"
        var whereArgs = arrayOf(id)
        db.delete(TABLE_NAME, whereClause, whereArgs)
    }
    fun deleteAll(){
        var db = writableDatabase
        db.delete(TABLE_NAME,null,null)
    }

    fun readProduct():ArrayList<ProductInCart>{
        var list:ArrayList<ProductInCart> = ArrayList()

        var db=readableDatabase

        var columns = arrayOf(
            COLUMN_ID,
            COLUMN_PRODUCTNAME,
            COLUMN_INCART,
            COLUMN_IMAGE,
            COLUMN_PRICE,
            COLUMN_MRP
        )
        var cursor=db.query(TABLE_NAME,columns,null,null,null,null,null)
        if(cursor!=null&&cursor.moveToFirst()){
            do {
                var id: String=cursor.getString(cursor.getColumnIndex(COLUMN_ID))
                var productName:String=cursor.getString(cursor.getColumnIndex(COLUMN_PRODUCTNAME))
                var inCart:Int=cursor.getInt(cursor.getColumnIndex(COLUMN_INCART))
                var image:String=cursor.getString(cursor.getColumnIndex(COLUMN_IMAGE))
                var price:Double=cursor.getDouble(cursor.getColumnIndex(COLUMN_PRICE))
                var mrp:Double=cursor.getDouble(cursor.getColumnIndex(COLUMN_MRP))

                var productInCart=ProductInCart(id,productName,inCart,image,price,mrp)
                list.add(productInCart)
            }while ( cursor.moveToNext())

        }
        return list
    }

    fun getProductInCartById(id: String):ProductInCart?{
        var productInCart:ProductInCart?=null
        var db=readableDatabase
        var query="select * from $TABLE_NAME where $COLUMN_ID = ?"
        var whereArg= arrayOf(id)
        var cursor= db.rawQuery(query,whereArg)

        if(cursor!=null&&cursor.moveToFirst()){
            var id: String=cursor.getString(cursor.getColumnIndex(COLUMN_ID))
            var productName:String=cursor.getString(cursor.getColumnIndex(COLUMN_PRODUCTNAME))
            var inCart:Int=cursor.getInt(cursor.getColumnIndex(COLUMN_INCART))
            var image:String=cursor.getString(cursor.getColumnIndex(COLUMN_IMAGE))
            var price:Double=cursor.getDouble(cursor.getColumnIndex(COLUMN_PRICE))
            var mrp:Double=cursor.getDouble(cursor.getColumnIndex(COLUMN_MRP))

            productInCart=ProductInCart(id,productName,inCart,image,price,mrp)
        }
        return productInCart

    }


}