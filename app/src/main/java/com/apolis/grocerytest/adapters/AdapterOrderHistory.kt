package com.apolis.grocerytest.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.apolis.grocerytest.R
import com.apolis.grocerytest.activities.OrderHistoryDetail
import com.apolis.grocerytest.app.Config
import com.apolis.grocerytest.models.OrderHistory
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.row_adapter_category.view.*
import kotlinx.android.synthetic.main.row_adapter_orderhistory.view.*

class AdapterOrderHistory (var mContext:Context,var mList:ArrayList<OrderHistory> =ArrayList(),var count:Int=0):
        RecyclerView.Adapter<AdapterOrderHistory.MyViewHolder>(){
    inner class MyViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){

        fun bind(orderHistory: OrderHistory){
            itemView.text_order_history_date.text=orderHistory.date
            itemView.text_order_history_status.text=orderHistory.orderStatus
            itemView.order_history_amount.text="$${orderHistory.orderSummary.ourPrice.toString()}"
            Picasso
                .get()
                .load("${Config.IMAGE_URL+orderHistory.products[0]?.image}")
                .error(R.drawable.ic_launcher_foreground)
                .into(itemView.orderhistory_image_view)
            itemView.setOnClickListener(){
                var intent= Intent(mContext,OrderHistoryDetail::class.java)
                intent.putExtra(OrderHistory.OrderHistory_Key,orderHistory)
                mContext.startActivity(intent)
            }
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view=LayoutInflater.from(mContext).inflate(R.layout.row_adapter_orderhistory,parent,false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return count
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        var orderHistory=mList[position]
        holder.bind(orderHistory)
    }

    fun setData(number:Int, orderHistoryList:ArrayList<OrderHistory>){
        mList=orderHistoryList
        count=number
        notifyDataSetChanged()
    }

}