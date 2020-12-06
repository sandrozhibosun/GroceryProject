package com.apolis.grocerytest.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.core.view.MenuItemCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.apolis.grocerytest.R
import com.apolis.grocerytest.adapters.AdapterOrderHistoryDetail
import com.apolis.grocerytest.database.DBhelper

import com.apolis.grocerytest.models.OrderHistory
import com.apolis.grocerytest.models.ShippingAddress
import kotlinx.android.synthetic.main.activity_order_history_detail.*

import kotlinx.android.synthetic.main.activity_payment.city_text_view
import kotlinx.android.synthetic.main.activity_payment.houseNo_text_view
import kotlinx.android.synthetic.main.activity_payment.pincode_text_view
import kotlinx.android.synthetic.main.activity_payment.text_view_type
import kotlinx.android.synthetic.main.app_bar.*
import kotlinx.android.synthetic.main.menu_cart.view.*

class OrderHistoryDetail : AppCompatActivity() {
    lateinit var adapterOrderHistoryDetail: AdapterOrderHistoryDetail
    lateinit var address: ShippingAddress
    lateinit var orderhistory: OrderHistory
    lateinit var dBhelper: DBhelper
    private var textViewCartCount: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_history_detail)
        orderhistory = intent.getSerializableExtra(OrderHistory.OrderHistory_Key) as OrderHistory
        address = orderhistory.shippingAddress
        dBhelper = DBhelper(this)
        init()
    }

    override fun onResume() {
        super.onResume()
        init()
    }

    private fun init() {
        setupToolbar()
        updateUi()

        city_text_view.text = address.city
        houseNo_text_view.text = address.houseNo
        text_view_type.text = address.type
        pincode_text_view.text = address.pincode.toString()
        adapterOrderHistoryDetail = AdapterOrderHistoryDetail(this)
        adapterOrderHistoryDetail.setData(orderhistory.products)

        orderhistorydetail_recycler_view.adapter = adapterOrderHistoryDetail
        orderhistorydetail_recycler_view.layoutManager = LinearLayoutManager(this)
        sub_total_text.text = "$${orderhistory.orderSummary.totalAmount}"
        total_text.text = "$${orderhistory.orderSummary.ourPrice}"
        discount_text.text = "$${orderhistory.orderSummary.discount}"

    }

    private fun setupToolbar() {


        var toolbar = toolbar
        toolbar.title = "Order Summary"

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

            else -> {
                Log.d("abc", "can't recon")
                super.onOptionsItemSelected(item)
            }
        }
        return true
    }
}