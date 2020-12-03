package com.apolis.grocerytest.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.apolis.grocerytest.R
import com.apolis.grocerytest.app.Config
import com.apolis.grocerytest.database.DBhelper
import com.apolis.grocerytest.models.ProductInCart
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.row_adapter_payment.view.*


class AdapterPayment(var mContext: Context, var mlist: ArrayList<ProductInCart> = ArrayList()) :
    RecyclerView.Adapter<AdapterPayment.MyViewHoloder>() {


    inner class MyViewHoloder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(productInCart: ProductInCart) {
            itemView.text_payment_product_name.text = productInCart.productName
            itemView.text_payment_product_price.text = "price: ${productInCart.price}"
            Picasso
                .get()
                .load("${Config.IMAGE_URL + productInCart.Image}")
                .error(R.drawable.ic_launcher_foreground)
                .into(itemView.product_payment_image_view)
            itemView.text_payment_product_count.text = productInCart.inCart.toString()


        }

    }



    fun setData(productInCartList: ArrayList<ProductInCart>) {
        mlist = productInCartList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHoloder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.row_adapter_payment, parent, false)
        return MyViewHoloder(view)
    }

    override fun getItemCount(): Int {
        return mlist.size
    }

    override fun onBindViewHolder(holder: MyViewHoloder, position: Int) {
        var productInCart = mlist[position]
        holder.bind(productInCart)
    }
}