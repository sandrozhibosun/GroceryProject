package com.apolis.grocerytest.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.core.view.MenuItemCompat
import androidx.core.view.isVisible
import androidx.drawerlayout.widget.DrawerLayout
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.apolis.grocerytest.R
import com.apolis.grocerytest.app.EndPoint
import com.apolis.grocerytest.database.DBhelper
import com.apolis.grocerytest.helper.SessionManager
import com.apolis.grocerytest.models.Address
import com.apolis.grocerytest.models.OrderProduct
import com.google.android.material.navigation.NavigationView
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_cart.*
import kotlinx.android.synthetic.main.activity_order_confirmation.*

import kotlinx.android.synthetic.main.app_bar.*
import kotlinx.android.synthetic.main.menu_cart.view.*
import kotlinx.android.synthetic.main.nav_header.view.*
import kotlinx.android.synthetic.main.orderconfirmation_content.*
import org.json.JSONArray
import org.json.JSONObject
import kotlin.math.log

class OrderConfirmationActivity : AppCompatActivity(),NavigationView.OnNavigationItemSelectedListener {

    lateinit var address: Address
    lateinit var dBhelper: DBhelper
    lateinit var sessionManager: SessionManager
    private lateinit var drawLayout: DrawerLayout
    private lateinit var navView: NavigationView
    var subtotal: Double = 0.00
    var discount: Double = 0.00
    var total: Double = 0.00

    private var textViewCartCount: TextView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_confirmation)

        successful_layout.visibility=View.GONE
        order_confirmation_button.visibility=View.VISIBLE
        address = intent.getSerializableExtra(Address.Address_Key) as Address
        subtotal = intent.getDoubleExtra("SUBTOTAL", 0.00)
        total = intent.getDoubleExtra("TOTAL", 0.00)
        discount = intent.getDoubleExtra("DISCOUNT", 0.00)

        sessionManager = SessionManager(this)
        dBhelper = DBhelper(this)

        init()
    }
    override fun onResume() {
        super.onResume()
        init()
    }

    private fun init() {
        setupToolbar()
        setupNavi()
        updateUi()

        order_confirmation_button.setOnClickListener {

            var user = sessionManager.getUser()

            var products = dBhelper.readProduct()
            var orderproducts = ArrayList<OrderProduct>()

            for (i in products) {
                var orderproduct = OrderProduct(i._id, i.mrp, i.price, i.inCart, i.Image)
                orderproducts.add(orderproduct)

            }






            var paras = HashMap<String, Any>()
            var shipParas = HashMap<String, Any>()
            var orderSumParas = HashMap<String, Any>()
            var userParas = HashMap<String, String>()


            shipParas["pincode"] = address.pincode
            shipParas["city"] = address.city
            shipParas["houseNo"] = address.houseNo
            shipParas["_id"] = address._id
            shipParas["type"] = address.type
            var shipJsonObject = JSONObject(shipParas as Map<*, *>)


            orderSumParas["deliveryCharges"] = 0
            orderSumParas["totalAmount"] = subtotal
            orderSumParas["orderAmount"] = subtotal
            orderSumParas["discount"] = discount
            orderSumParas["ourPrice"] = total
            var orderSumJsonObject=JSONObject(orderSumParas as Map<*, *>)

            userParas["_id"]=user.userId
            userParas["mobile"]=user.mobile!!
            userParas["email"]=user.email!!
            var userJsonObject=JSONObject(userParas as Map<*,*>)


            paras["orderStatus"] = "Confirmed"
            paras["shippingAddress"] = shipJsonObject
            paras["orderSummary"]=orderSumJsonObject
            paras["userId"]=user.userId
            paras["user"]=userJsonObject


//notice the diff of JsonArray and JSONArray
            var jsonArray =JSONArray(Gson().toJson(orderproducts))

            Log.d("123",jsonArray.toString())

            var jsonObject = JSONObject(paras as Map<*, *>)


//            Log.d("123","${jsonelement.toString()}")

            jsonObject.put("products",jsonArray)


            Log.d("123","${jsonObject.toString()}")



            var request = JsonObjectRequest(
                Request.Method.POST, EndPoint.postOrder(), jsonObject,
                Response.Listener {
                    Toast.makeText(applicationContext, "post order successful", Toast.LENGTH_SHORT)
                        .show()
                    successful_layout.visibility=View.VISIBLE
                    order_confirmation_button.visibility=View.GONE
                    dBhelper.deleteAll()

                },
                Response.ErrorListener {
                    Toast.makeText(applicationContext, it.message.toString(), Toast.LENGTH_SHORT)
                        .show()
                    Log.d("abc", it.message.toString())
                }
            )
            Volley.newRequestQueue(this).add(request)

        }
        back_shoping_button.setOnClickListener {
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }

    }
    private fun setupNavi() {
        drawLayout = orderconfirmation_drawer_layout
        navView = orderconfirmation_nav_view
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
        toolbar.title = "Grocery"
        toolbar.subtitle = "This is Grocery App"
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
                startActivity(Intent(this, SplashActivity::class.java))
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