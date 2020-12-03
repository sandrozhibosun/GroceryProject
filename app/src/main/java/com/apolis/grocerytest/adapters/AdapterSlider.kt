package com.apolis.grocerytest.adapters

import android.content.Context

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.apolis.grocerytest.R

import com.apolis.grocerytest.app.Config
import com.apolis.grocerytest.models.Category
import com.squareup.picasso.Picasso

import kotlinx.android.synthetic.main.row_adapter_slider.view.*

class AdapterSlider(var mContext: Context, var mList:ArrayList<Category> = ArrayList()):
    RecyclerView.Adapter<AdapterSlider.MyViewHolder>(){

    inner class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        fun bind(category: Category) {

            Picasso
                .get()
                .load("${Config.IMAGE_URL+category.catImage}")
                .error(R.drawable.ic_launcher_foreground)
                .into(itemView.image_view_slider)

            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.row_adapter_slider, parent, false)
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