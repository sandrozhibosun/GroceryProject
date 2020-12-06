package com.apolis.grocerytest.activities

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.MenuItemCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.apolis.grocerytest.R
import com.apolis.grocerytest.adapters.AdapterAddress
import com.apolis.grocerytest.adapters.AdapterProduct
import com.apolis.grocerytest.app.EndPoint
import com.apolis.grocerytest.database.DBhelper
import com.apolis.grocerytest.helper.SessionManager
import com.apolis.grocerytest.models.AddressListResponse
import com.apolis.grocerytest.models.ProductSub
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_address_list.*
import kotlinx.android.synthetic.main.app_bar.*
import kotlinx.android.synthetic.main.fragment_sub_pro_category.view.*
import kotlinx.android.synthetic.main.menu_cart.view.*
import kotlinx.android.synthetic.main.row_adapter_address.*

class AddressListActivity : AppCompatActivity(),View.OnClickListener {

    lateinit var adapterAddress: AdapterAddress
    lateinit var sessionManager: SessionManager
    lateinit var dBhelper: DBhelper
    private var textViewCartCount: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_address_list)
        sessionManager= SessionManager(this)
        dBhelper=DBhelper(this)
        init()
        val broadcastReceiver=object: BroadcastReceiver(){
            override fun onReceive(context: Context?, intent: Intent?) {
                when(intent?.action){
                    "Alert_Set_Address"->{
                        Toast.makeText(applicationContext,"Set Default Address successfully",Toast.LENGTH_SHORT).show()

                        finish()
                    }
                }
            }
        }
        LocalBroadcastManager.getInstance(this)
            .registerReceiver(broadcastReceiver, IntentFilter("Alert_Set_Address"))

    }

    override fun onResume() {
        super.onResume()
        init()
    }

    private fun init(){
        setupToolbar()
        updateUi()
        getData()
        adapterAddress= AdapterAddress(this)
        address_list_recycler_view.adapter=adapterAddress
        address_list_recycler_view.layoutManager=LinearLayoutManager(this)


        UserName.text=sessionManager.getUserName()
        add_adress_button.setOnClickListener(this)
    }

    private fun getData(){
        var requestQueue= Volley.newRequestQueue(this)
        Log.d("abc",sessionManager.getUserId()!!)
        var request= StringRequest(
            Request.Method.GET, EndPoint.getAddress(sessionManager.getUserId()!!),
            Response.Listener {

                var gson= Gson()
                var addressList=gson.fromJson(it.toString(), AddressListResponse::class.java)
                adapterAddress.setData(addressList.count,addressList.data)
            },
            Response.ErrorListener {
                Toast.makeText(this, it.message.toString(), Toast.LENGTH_SHORT).show()
            })
        requestQueue.add(request)
    }

    override fun onClick(v: View?) {
        when(v){
            add_adress_button->{
                startActivity(Intent(this,AddAddressActivity::class.java))
            }

        }
    }
    private fun setupToolbar() {


        var toolbar = toolbar
        toolbar.title = "Address Management"

        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun updateUi() {
        if (dBhelper.readProduct().size == 0) {
            textViewCartCount?.visibility = View.GONE
        } else {
            textViewCartCount?.visibility = View.VISIBLE
            textViewCartCount?.text = dBhelper.readProduct().size.toString()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.mainmenu, menu)
        var item = menu?.findItem(R.id.cart_action)
        MenuItemCompat.setActionView(item, R.layout.menu_cart)
        var view = MenuItemCompat.getActionView(item)
        textViewCartCount = view.text_view_cart_count
        view.setOnClickListener() {
            var Intent = Intent(this, CartActivity::class.java)
            startActivity(Intent)
        }
        updateUi()

        return super.onCreateOptionsMenu(menu)
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