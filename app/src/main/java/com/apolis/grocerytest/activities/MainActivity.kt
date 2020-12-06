package com.apolis.grocerytest.activities

import android.content.ClipData
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.GridLayout
import android.widget.TextView
import android.widget.Toast
import android.widget.Toolbar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.core.view.MenuItemCompat
import androidx.core.view.isVisible
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.apolis.grocerytest.R
import com.apolis.grocerytest.adapters.AdapterCategory
import com.apolis.grocerytest.adapters.AdapterSlider
import com.apolis.grocerytest.app.EndPoint
import com.apolis.grocerytest.database.DBhelper
import com.apolis.grocerytest.helper.SessionManager
import com.apolis.grocerytest.models.CategoryResponse
import com.google.android.material.navigation.NavigationView
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar.*
import kotlinx.android.synthetic.main.main_content.*
import kotlinx.android.synthetic.main.menu_cart.view.*
import kotlinx.android.synthetic.main.nav_header.view.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    lateinit var adapterCategory: AdapterCategory
    lateinit var adapterSlider: AdapterSlider
    private lateinit var drawLayout: DrawerLayout
    private lateinit var navView: NavigationView
    private lateinit var sessionManager: SessionManager
    private var textViewCartCount: TextView? = null
    lateinit var dBhelper: DBhelper


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        dBhelper = DBhelper(this)


        sessionManager = SessionManager(this)
        Log.d("abc", "session main check: ${sessionManager.isLoggedIn()}")
        init()

    }

    override fun onResume() {
        super.onResume()
        setupNavi()
        updateUi()
    }


    private fun init() {
        updateUi()

        setupToolbar()
        getData()
        setupNavi()
        adapterSlider = AdapterSlider(this)
        slider_recycler_view.adapter = adapterSlider
        slider_recycler_view.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        adapterCategory = AdapterCategory(this)
        category_recycler_view.adapter = adapterCategory
//        category_recycler_view.layoutManager =
//            StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
        category_recycler_view.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

    }


    private fun setupToolbar() {


        var toolbar = toolbar
        toolbar.title = "Grocery"
        toolbar.subtitle = "This is Grocery App"
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }


    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {

//        if (sessionManager.isLoggedIn()) {
//
//
//        } else {
//
//            menu?.findItem(R.id.Login_action)?.setVisible(true)
//            menu?.findItem(R.id.Signup_action)?.setVisible(true)
//            menu?.findItem(R.id.Logout_action)?.setVisible(false)
//            menu?.findItem(R.id.UserName)?.setTitle("Guest")
//        }
        if (sessionManager.isLoggedIn()) {
            menu?.findItem(R.id.Login_action)?.setVisible(false)
            menu?.findItem(R.id.Signup_action)?.setVisible(false)
            menu?.findItem(R.id.Logout_action)?.setVisible(true)
            menu?.findItem(R.id.UserName)?.setTitle("Welcome, ${sessionManager.getUserName()}")
            //nav
            menu?.findItem(R.id.item_Login)?.isVisible = false
            menu?.findItem(R.id.item_register)?.isVisible = false
            menu?.findItem(R.id.item_Logout)?.isVisible = true
            menu?.findItem(R.id.item_address)?.isVisible = true
            menu?.findItem(R.id.item_order_history)?.isVisible = true

        } else {
            menu?.findItem(R.id.Login_action)?.setVisible(true)
            menu?.findItem(R.id.Signup_action)?.setVisible(true)
            menu?.findItem(R.id.Logout_action)?.setVisible(false)
            menu?.findItem(R.id.UserName)?.setTitle("Guest")
            //nav
            menu?.findItem(R.id.item_Login)?.isVisible = true
            menu?.findItem(R.id.item_register)?.isVisible = true
            menu?.findItem(R.id.item_Logout)?.isVisible = false
            menu?.findItem(R.id.item_address)?.isVisible = false
            menu?.findItem(R.id.item_order_history)?.isVisible = false

        }
        //cart


        return true
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


    private fun getData() {

        var requestQueue = Volley.newRequestQueue(this)
        Log.d("abc", "ready to request")
        var request = StringRequest(
            Request.Method.GET, EndPoint.getCategory(),
            Response.Listener {
                category_progress_bar.visibility = View.GONE
                Log.d("abc", "ready to get jsonArray")
                var gson = Gson()
                var categoryResponse = gson.fromJson(it.toString(), CategoryResponse::class.java)

                adapterCategory.setData(categoryResponse.data)
                adapterSlider.setData(categoryResponse.data)

            },
            Response.ErrorListener {
                Toast.makeText(applicationContext, it.message.toString(), Toast.LENGTH_SHORT).show()
            })

        requestQueue.add(request)


    }

    private fun setupNavi() {
        drawLayout = drawer_layout
        navView = nav_view
        var nav_menu = navView.menu
        var headView = navView.getHeaderView(0)
        if (sessionManager.isLoggedIn()) {
            nav_menu.findItem(R.id.item_Login).isVisible = false
            nav_menu.findItem(R.id.item_register).isVisible = false
            nav_menu.findItem(R.id.item_Logout).isVisible = true
            nav_menu.findItem(R.id.item_address).isVisible = true
            nav_menu.findItem(R.id.item_order_history).isVisible = true
            headView.text_view_header_name.text = sessionManager.getUserName()
            headView.text_view_header_email.isVisible = true
            headView.text_view_header_email.text = sessionManager.getUserEmail()
        } else {
            nav_menu.findItem(R.id.item_Login).isVisible = true
            nav_menu.findItem(R.id.item_register).isVisible = true
            nav_menu.findItem(R.id.item_Logout).isVisible = false
            nav_menu.findItem(R.id.item_address).isVisible = false
            nav_menu.findItem(R.id.item_order_history).isVisible = false
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
            R.id.item_order_history -> {
                startActivity(Intent(this, OrderHistoryActivity::class.java))

            }
            R.id.item_address -> {
                startActivity(Intent(this, AddressListActivity::class.java))
            }
        }


        drawLayout.closeDrawer(GravityCompat.START)


        return true
    }

}



