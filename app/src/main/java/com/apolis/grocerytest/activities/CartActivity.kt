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
import android.widget.Adapter
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.core.view.isVisible
import androidx.drawerlayout.widget.DrawerLayout
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.apolis.grocerytest.R
import com.apolis.grocerytest.adapters.AdapterCart
import com.apolis.grocerytest.database.DBhelper
import com.apolis.grocerytest.helper.SessionManager
import com.apolis.grocerytest.models.Address
import com.apolis.grocerytest.models.ProductInCart
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_cart.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar.*
import kotlinx.android.synthetic.main.cart_content.*
import kotlinx.android.synthetic.main.nav_header.view.*

class CartActivity : AppCompatActivity(),NavigationView.OnNavigationItemSelectedListener {

    lateinit var adapterCart: AdapterCart
    lateinit var sessionManager:SessionManager
    private lateinit var drawLayout: DrawerLayout
    private lateinit var navView: NavigationView



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        sessionManager=SessionManager(this)
        init()

        val broadcastReceiver=object:BroadcastReceiver(){
            override fun onReceive(context: Context?, intent: Intent?) {
                when(intent?.action){
                    "Alert_Change"->{


                        subTotal()
                    }
                }
            }
        }
        LocalBroadcastManager.getInstance(this)
            .registerReceiver(broadcastReceiver, IntentFilter("Alert_Change"))
    }

    override fun onResume() {
        super.onResume()
        init()
    }

    private fun init(){

        setupToolbar()
//        setupNavi()
        adapterCart= AdapterCart(this)
        subTotal()
        getData()
        cart_recycler_view.adapter=adapterCart
        cart_recycler_view.layoutManager=LinearLayoutManager(this)
        checkout_button.setOnClickListener {
            if(sessionManager.isLoggedIn()){

            if(sessionManager.hasSetDefaultAddress()){
                var intent=Intent(this,PaymentActivity::class.java)
                intent.putExtra(Address.Address_Key,sessionManager.getDefaultAddress())
                startActivity(intent)
            }
            else{
            startActivity(Intent(this,AddressListActivity::class.java))
            }}
            else{
                Toast.makeText(this,"Please login to check out",Toast.LENGTH_SHORT).show()
            }
        }
        goshopping_button.setOnClickListener {


            finish()
            startActivity(Intent(this,MainActivity::class.java))
        }



    }

    private fun getData(){
        var productInCartList=ArrayList<ProductInCart>()
        var dBhelper=DBhelper(this)
        productInCartList=dBhelper.readProduct()
        adapterCart.setData(productInCartList)



    }
    fun subTotal(){
        var addsubTotal:Double=0.00
        var addTotal:Double=0.00
        var productInCartList=ArrayList<ProductInCart>()
        var dBhelper=DBhelper(this)
        productInCartList=dBhelper.readProduct()
        for(i in productInCartList)
        {
            addTotal+=i.price*i.inCart
            addsubTotal+=i.mrp*i.inCart

        }
        if(addsubTotal.equals(0.00))
        {
            checkTotalLayout.visibility= View.GONE
            checkout_button.visibility=View.GONE
            goshopping_button.visibility=View.VISIBLE
        }
        else{
            checkTotalLayout.visibility= View.VISIBLE
            checkout_button.visibility=View.VISIBLE
            goshopping_button.visibility=View.GONE

        }

        sub_total_text.text="$${addsubTotal.toString()}"
        total_text.text="$${addTotal.toString()}"
        discount_text.text="$${(addsubTotal-addTotal).toString()}"

    }
    private fun setupNavi() {
        drawLayout = cart_drawer_layout
        navView = cart_nav_view
        var nav_menu = navView.menu
        var headView = navView.getHeaderView(0)
        if (sessionManager.isLoggedIn()) {
            nav_menu.findItem(R.id.item_Login).isVisible=false
            nav_menu.findItem(R.id.item_register).isVisible=false
            nav_menu.findItem(R.id.item_Logout).isVisible=true
            nav_menu.findItem(R.id.item_address).isVisible=true
            nav_menu.findItem(R.id.item_order_history).isVisible=true
            headView.text_view_header_name.text = sessionManager.getUserName()
            headView.text_view_header_email.isVisible = true
            headView.text_view_header_email.text = sessionManager.getUserEmail()
        } else {
            nav_menu.findItem(R.id.item_Login).isVisible=true
            nav_menu.findItem(R.id.item_register).isVisible=true
            nav_menu.findItem(R.id.item_Logout).isVisible=false
            nav_menu.findItem(R.id.item_address).isVisible=false
            nav_menu.findItem(R.id.item_order_history).isVisible=false
            headView.text_view_header_name.text = "Guest"
            headView.text_view_header_email.isVisible = false

        }
        var toggle = ActionBarDrawerToggle(this, drawLayout, toolbar, 0, 0)
        drawLayout.addDrawerListener(toggle)
        toggle.syncState()
        navView.setNavigationItemSelectedListener(this)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {

            R.id.item_category -> {
                startActivity(Intent(this, MainActivity::class.java))
            }
            R.id.item_Login -> {
                Log.d("abc", "login")
                var intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)


            }
            R.id.item_register -> {
                Log.d("abc", "sign up")
                var intent = Intent(this, RegisterActivity::class.java)
                startActivity(intent)


            }
            R.id.item_Logout -> {
                sessionManager.logout()

            }
            R.id.item_order_history->{
                startActivity(Intent(this,OrderHistoryActivity::class.java))

            }

            R.id.item_address->{
                startActivity(Intent(this,AddressListActivity::class.java))
            }
        }


        drawLayout.closeDrawer(GravityCompat.START)


        return true
    }

    private fun setupToolbar() {


        var toolbar = toolbar
        toolbar.title = "Customer Cart"

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