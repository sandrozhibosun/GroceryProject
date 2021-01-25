package com.apolis.grocerytest.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

import com.apolis.grocerytest.R
import com.apolis.grocerytest.adapters.AdapterProduct
import com.apolis.grocerytest.app.EndPoint
import com.apolis.grocerytest.models.CategoryResponse
import com.apolis.grocerytest.models.ProductSub
import com.apolis.grocerytest.models.SubCategory
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_sub_pro_category.view.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"


/**
 * A simple [Fragment] subclass.
 * Use the [SubProCategoryFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SubProCategoryFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var subCategory: SubCategory? = null
    lateinit var adapterProduct: AdapterProduct




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            subCategory = it.getSerializable(ARG_PARAM1) as SubCategory

        }
    }

    override fun onResume() {
        super.onResume()
        getData()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
       var view= inflater.inflate(R.layout.fragment_sub_pro_category, container, false)

        init(view)
        view.subcategory_progress_bar.visibility = View.GONE
        return view
    }

    private fun init(view:View){

        getData()

        adapterProduct=AdapterProduct(activity!!)
        view.subcategory_recycler_view.adapter=adapterProduct
        view.subcategory_recycler_view.layoutManager=LinearLayoutManager(activity)


    }


    private fun getData(){

//        var requestQueue = Volley.newRequestQueue(this)
//        Log.d("abc", "ready to request")
//        var request = StringRequest(
//            Request.Method.GET, url,
//            Response.Listener {
//                category_progress_bar.visibility = View.GONE
//                Log.d("abc", "ready to get jsonArray")
//                var gson = Gson()
//                var categoryResponse = gson.fromJson(it.toString(), CategoryResponse::class.java)
//
//                adapterCategory.setData(categoryResponse.data)
//
//            },
//            Response.ErrorListener {
//                Toast.makeText(applicationContext, it.message.toString(), Toast.LENGTH_SHORT).show()
//            })
//
//        requestQueue.add(request)

        var requestQueue= Volley.newRequestQueue(activity)
        var request= StringRequest(
            Request.Method.GET,EndPoint.getProductBySubId(subCategory?.subId!!),
        Response.Listener {

            var gson=Gson()
            var productSub=gson.fromJson(it.toString(),ProductSub::class.java)
            adapterProduct.setData(productSub.data)
        },
        Response.ErrorListener {
            Toast.makeText(activity, it.message.toString(), Toast.LENGTH_SHORT).show()
        })
        requestQueue.add(request)
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SubProCategoryFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: SubCategory) =
            SubProCategoryFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_PARAM1, param1)

                }
            }
    }
}