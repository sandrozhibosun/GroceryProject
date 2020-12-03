package com.apolis.grocerytest.adapters

import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.apolis.grocerytest.R
import com.apolis.grocerytest.activities.CartActivity
import com.apolis.grocerytest.activities.ProductDetailActivity
import com.apolis.grocerytest.activities.SubCategoryActivity
import com.apolis.grocerytest.app.Config
import com.apolis.grocerytest.database.DBhelper
import com.apolis.grocerytest.models.Category
import com.apolis.grocerytest.models.Product
import com.apolis.grocerytest.models.ProductInCart
import com.squareup.picasso.Picasso

import kotlinx.android.synthetic.main.row_adapter_subcategory.view.*

class AdapterProduct(var mContext: Context, var mList:ArrayList<Product> = ArrayList()):
    RecyclerView.Adapter<AdapterProduct.ProductViewHolder>(){

    inner class ProductViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        fun bind(product: Product) {
            itemView.text_product_name.text = product.productName
            itemView.text_product_price.text = "price:$${product.price}"
            itemView.text_product_mrp.text = "$${product.mrp.toString()}"
            itemView.text_product_mrp.apply {
                paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            }
            Picasso
                .get()
                .load("${Config.IMAGE_URL + product.image}")
                .error(R.drawable.ic_launcher_foreground)
                .into(itemView.product_image_view)

            itemView.setOnClickListener() {
                var intent = Intent(mContext, ProductDetailActivity::class.java)
                intent.putExtra(Product.Product_Key, product)
                mContext.startActivity(intent)

            }
            var dBhelper = DBhelper(mContext)
            var productInCart = dBhelper.getProductInCartById(product._id)
            itemView.add_button.setOnClickListener {
                dBhelper.addToCart(product)
                itemView.add_button.visibility = View.GONE
                itemView.plus_and_minus.visibility = View.VISIBLE
//                    var Intent = Intent(mContext, CartActivity::class.java)
//                    mContext.startActivity(Intent)
            }
            itemView.text_product_count.text = "1"
            itemView.button_plus.setOnClickListener() {

                if(dBhelper.getProductInCartById(product._id)==null)
                {
                    return@setOnClickListener
                }
                dBhelper.productPlus(product._id)
                itemView.text_product_count.text =
                    dBhelper.getProductInCartById(product._id)!!.inCart.toString()


            }
            itemView.button_minus.setOnClickListener {
                if(dBhelper.getProductInCartById(product._id)==null)
                {
                    return@setOnClickListener
                }

                if (dBhelper.getProductInCartById(product._id)!!.inCart > 1) {
                    dBhelper.productminus(product._id)
                    itemView.text_product_count.text =
                        dBhelper.getProductInCartById(product._id)!!.inCart.toString()
                } else {
                    dBhelper.productminus(product._id)
                    itemView.add_button.visibility = View.VISIBLE
                    itemView.plus_and_minus.visibility = View.GONE
                }
            }
            if (productInCart == null) {
                itemView.add_button.visibility = View.VISIBLE
                itemView.plus_and_minus.visibility = View.GONE

            } else {
                itemView.add_button.visibility = View.GONE
                itemView.plus_and_minus.visibility = View.VISIBLE
                itemView.text_product_count.text = productInCart.inCart.toString()
//                itemView.button_plus.setOnClickListener() {
//                    dBhelper.productPlus(productInCart._id)
//                    itemView.text_product_count.text =
//                        dBhelper.getProductInCartById(productInCart._id)!!.inCart.toString()
//
//
//                }
//                itemView.button_minus.setOnClickListener {
//
//                    if (dBhelper.getProductInCartById(productInCart._id)!!.inCart > 1) {
//                        dBhelper.productminus(productInCart._id)
//                        itemView.text_product_count.text =
//                            dBhelper.getProductInCartById(productInCart._id)!!.inCart.toString()
//                    } else {
//                        dBhelper.productminus(productInCart._id)
//                        itemView.add_button.visibility = View.VISIBLE
//                        itemView.plus_and_minus.visibility = View.GONE
//                    }
//                }

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.row_adapter_subcategory, parent, false)
        return ProductViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        var product = mList[position]
        holder.bind(product)
    }
    fun setData(productList: ArrayList<Product>) {
        mList = productList
        notifyDataSetChanged()
    }



}