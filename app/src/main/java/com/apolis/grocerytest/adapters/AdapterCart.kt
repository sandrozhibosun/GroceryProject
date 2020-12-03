package com.apolis.grocerytest.adapters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.RecyclerView
import com.apolis.grocerytest.R
import com.apolis.grocerytest.app.Config
import com.apolis.grocerytest.database.DBhelper
import com.apolis.grocerytest.models.ProductInCart
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_cart.view.*
import kotlinx.android.synthetic.main.row_adapter_cart.view.*


class AdapterCart(var mContext: Context, var mlist: ArrayList<ProductInCart> = ArrayList()) :
    RecyclerView.Adapter<AdapterCart.MyViewHoloder>() {


    inner class MyViewHoloder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(productInCart: ProductInCart, position: Int) {
            itemView.textcart_product_name.text = productInCart.productName
            itemView.textcart_product_price.text = "price: $${productInCart.price}"
            Picasso
                .get()
                .load("${Config.IMAGE_URL + productInCart.Image}")
                .error(R.drawable.ic_launcher_foreground)
                .into(itemView.productcart_image_view)
            itemView.text_product_count.text = productInCart.inCart.toString()
            var dBhelper = DBhelper(mContext)
            itemView.button_plus.setOnClickListener() {
                dBhelper.productPlus(productInCart._id)
                itemView.text_product_count.text =
                    dBhelper.getProductInCartById(productInCart._id)!!.inCart.toString()
                    broadcastItem()


            }
            itemView.button_minus.setOnClickListener {

                if (dBhelper.getProductInCartById(productInCart._id)!!.inCart > 1) {
                    dBhelper.productminus(productInCart._id)
                    itemView.text_product_count.text =
                        dBhelper.getProductInCartById(productInCart._id)!!.inCart.toString()
                } else {
                    dBhelper.productminus(productInCart._id)
                    removeItem(position)
                }
                broadcastItem()


            }
            itemView.delete_item_button.setOnClickListener {
                removeItem(position)
                dBhelper.deleteProuduct(productInCart._id)
                broadcastItem()
            }


        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHoloder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.row_adapter_cart, parent, false)
        return MyViewHoloder(view)
    }

    override fun getItemCount(): Int {
        return mlist.size
    }

    override fun onBindViewHolder(holder: MyViewHoloder, position: Int) {
        var productInCart = mlist[position]
        holder.bind(productInCart, position)
    }

    fun setData(productInCartList: ArrayList<ProductInCart>) {
        mlist = productInCartList
        notifyDataSetChanged()
    }

    fun removeItem(position: Int) {
        mlist.removeAt(position)
        notifyItemRemoved(position)
    }

    fun broadcastItem(){

        var intent=Intent("Alert_Change")

        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent)


    }

}