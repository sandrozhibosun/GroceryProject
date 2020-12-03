package com.apolis.grocerytest.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.apolis.grocerytest.R
import com.apolis.grocerytest.adapters.AdapterOrderHistory
import com.apolis.grocerytest.app.EndPoint
import com.apolis.grocerytest.helper.SessionManager
import com.apolis.grocerytest.models.OrderHistoryResponse
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_order_history.*
import kotlinx.android.synthetic.main.app_bar.*

class OrderHistoryActivity : AppCompatActivity() {

    private lateinit var sessionManager: SessionManager
    private lateinit var adapterOrderHistory: AdapterOrderHistory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_history)

        sessionManager= SessionManager(this)
        init()
    }

    private fun init(){
        setupToolbar()

        getData()
        adapterOrderHistory= AdapterOrderHistory(this)
        orderhistory_recycler_view.adapter=adapterOrderHistory
        orderhistory_recycler_view.layoutManager=LinearLayoutManager(this)

    }

    private fun getData(){
        var requestQueue=Volley.newRequestQueue(this)
        var request=StringRequest(Request.Method.GET,EndPoint.getOrderByUserId(sessionManager.getUserId()!!),
        Response.Listener {
           orderhistory_progress_bar.visibility = View.GONE
            var gson=Gson()
            var orderhistoryList=gson.fromJson(it.toString(),OrderHistoryResponse::class.java)
            adapterOrderHistory.setData(orderhistoryList.count,orderhistoryList.data)

        },
        Response.ErrorListener {
            Toast.makeText(this,it.message.toString(),Toast.LENGTH_SHORT).show()
        })
        requestQueue.add(request)
    }

    override fun onResume() {
        super.onResume()
        init()
    }
    private fun setupToolbar() {


        var toolbar = toolbar
        toolbar.title = "History Order"

        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.mainmenu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            android.R.id.home -> {

                finish()
            }
            R.id.cart_action -> {
                var Intent = Intent(this, CartActivity::class.java)
                startActivity(Intent)
                Log.d("abc", "back")

                true
            }
            R.id.Login_action -> {
                Log.d("abc", "login")
                var intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)

                true
            }
            R.id.Signup_action -> {
                Log.d("abc", "sign up")
                var intent = Intent(this, RegisterActivity::class.java)
                startActivity(intent)

                true
            }
            R.id.Logout_action -> {
                sessionManager.logout()


                true
            }
            else -> {
                Log.d("abc", "can't recon")
                super.onOptionsItemSelected(item)
            }
        }
        return true
    }

}