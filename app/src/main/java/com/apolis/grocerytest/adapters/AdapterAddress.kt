package com.apolis.grocerytest.adapters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.apolis.grocerytest.R
import com.apolis.grocerytest.activities.PaymentActivity
import com.apolis.grocerytest.app.EndPoint
import com.apolis.grocerytest.helper.SessionManager

import com.apolis.grocerytest.models.Address
import com.apolis.grocerytest.models.AddressListResponse
import com.apolis.grocerytest.models.Category
import com.google.gson.Gson
import kotlinx.android.synthetic.main.row_adapter_address.view.*


class AdapterAddress(var mContext: Context, var mList: ArrayList<Address> = ArrayList(),var count:Int =0) :
    RecyclerView.Adapter<AdapterAddress.MyViewHolder>() {

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(address: Address) {

            itemView.street_name_text_view.text=address.streetName
            itemView.city_text_view.text=address.city
            itemView.houseNo_text_view.text=address.houseNo
            Log.d("abc","${address.type}")
            itemView.text_view_type.text=address.type
            Log.d("abc","${address.pincode}")
            itemView.pincode_text_view.text=address.pincode.toString()
            itemView.delete_address_button.setOnClickListener {
                var requestQueue= Volley.newRequestQueue(mContext)
                Log.d("abc",address._id)
                var request= StringRequest(
                    Request.Method.DELETE, EndPoint.deleteAddress(address._id),
                    Response.Listener {

                        Toast.makeText(mContext, "delete successful", Toast.LENGTH_SHORT).show()

                    },
                    Response.ErrorListener {
                        Toast.makeText(mContext, it.message.toString(), Toast.LENGTH_SHORT).show()
                    })
                requestQueue.add(request)
            }
            itemView.useAddress_button.setOnClickListener {
                var sessionManager=SessionManager(mContext)
                sessionManager.setDefaultAddress(address)
//                var intent=Intent(mContext,PaymentActivity::class.java)
//                intent.putExtra(Address.Address_Key,address)
//                mContext.startActivity(intent)
                broadcastItem()
            }


        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.row_adapter_address, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return count
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        var address = mList[position]
        holder.bind(address)
    }
    fun setData(number: Int,addresslist: ArrayList<Address>) {
        mList = addresslist
        count=number
        notifyDataSetChanged()
    }
    fun broadcastItem(){
        var intent=Intent("Alert_Set_Address")
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent)
    }

}




