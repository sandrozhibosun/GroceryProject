package com.apolis.grocerytest.adapters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.apolis.grocerytest.R
import com.apolis.grocerytest.activities.SubCategoryActivity
import com.apolis.grocerytest.app.Config
import com.apolis.grocerytest.models.Category
import com.apolis.grocerytest.models.SubCategory
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.row_adapter_category.view.*

import kotlin.collections.ArrayList

class AdapterCategory(var mContext:Context,var mList:ArrayList<Category> = ArrayList()):
        RecyclerView.Adapter<AdapterCategory.MyViewHolder>(){

    inner class MyViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){

        fun bind(category: Category) {
            itemView.text_name_category.text=category.catName
            Picasso
                .get()
                .load("${Config.IMAGE_URL+category.catImage}")
                .error(R.drawable.ic_launcher_foreground)
                .into(itemView.image_view_category)

            itemView.setOnClickListener() {
                var intent = Intent(mContext, SubCategoryActivity::class.java)
                intent.putExtra(Category.Category_Key, category)
                Log.d("abc","SetCategory")
                mContext.startActivity(intent)

            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.row_adapter_category, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        var category = mList[position]
        holder.bind(category)
    }
    fun setData(categoryList: ArrayList<Category>) {
        mList = categoryList
        notifyDataSetChanged()
    }



}