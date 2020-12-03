package com.apolis.grocerytest.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.apolis.grocerytest.R
import com.apolis.grocerytest.adapters.VpAdapter
import com.apolis.grocerytest.app.Config
import com.apolis.grocerytest.database.DBhelper
import com.apolis.grocerytest.helper.SessionManager
import com.apolis.grocerytest.models.Category
import com.apolis.grocerytest.models.Product
import com.apolis.grocerytest.models.SubCategory
import com.apolis.grocerytest.models.SubCategoryResponse
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_product_detail.*
import kotlinx.android.synthetic.main.activity_sub_category.*
import kotlinx.android.synthetic.main.app_bar.*
import kotlinx.android.synthetic.main.fragment_sub_pro_category.*
import kotlinx.android.synthetic.main.row_adapter_cart.view.*
import kotlinx.android.synthetic.main.row_adapter_subcategory.view.*

class ProductDetailActivity : AppCompatActivity() {
    var product: Product? = null
    lateinit var dBhelper: DBhelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)
        product = intent.getSerializableExtra(Product.Product_Key) as Product
        Picasso
            .get()
            .load("${Config.IMAGE_URL + product!!.image}")
            .error(R.drawable.ic_launcher_foreground)
            .into(detail_image_view)
        detail_text_description.text = product!!.description
        dBhelper = DBhelper(this)
        init()

    }

    override fun onResume() {
        super.onResume()
        init()
    }

    private fun init() {

        setupToolbar()
        var productInCart = dBhelper.getProductInCartById(product!!._id)
        if (dBhelper.getProductInCartById(product!!._id) == null) {
            add_button.visibility = View.VISIBLE
            plus_and_minus.visibility = View.GONE
            add_button.setOnClickListener {
                dBhelper = DBhelper(this)
                dBhelper.addToCart(product!!)
                add_button.visibility = View.GONE
                plus_and_minus.visibility = View.VISIBLE
                var Intent = Intent(this, CartActivity::class.java)
                startActivity(Intent)
            }
        } else {
            add_button.visibility = View.GONE
            plus_and_minus.visibility = View.VISIBLE
            text_product_count.text = productInCart!!.inCart.toString()
            button_plus.setOnClickListener() {
                dBhelper.productPlus(productInCart._id)
                text_product_count.text = dBhelper.getProductInCartById(productInCart._id)!!.inCart.toString()


            }
            button_minus.setOnClickListener {

                if (dBhelper.getProductInCartById(productInCart._id)!!.inCart > 1) {
                    dBhelper.productminus(productInCart._id)
                    text_product_count.text =
                        dBhelper.getProductInCartById(productInCart._id)!!.inCart.toString()
                } else {
                    dBhelper.productminus(productInCart._id)
//                    onResume()
                    add_button.visibility = View.VISIBLE
                    plus_and_minus.visibility = View.GONE

                }


            }

        }




    }

    private fun setupToolbar() {
        var toolbar = toolbar
        toolbar.title = "Product details"
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        var sessionManager = SessionManager(this)
        if (sessionManager.isLoggedIn()) {
            menu?.findItem(R.id.Login_action)?.setVisible(false)
            menu?.findItem(R.id.Signup_action)?.setVisible(false)
            menu?.findItem(R.id.Logout_action)?.setVisible(true)
            menu?.findItem(R.id.UserName)?.setTitle("Welcome, ${sessionManager.getUserName()}")
        } else {
            menu?.findItem(R.id.Login_action)?.setVisible(true)
            menu?.findItem(R.id.Signup_action)?.setVisible(true)
            menu?.findItem(R.id.Logout_action)?.setVisible(false)
            menu?.findItem(R.id.UserName)?.setTitle("Guest")
        }
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.mainmenu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        var sessionManager = SessionManager(this)
        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }
            R.id.cart_action -> {
                Log.d("abc", "back")
                var Intent = Intent(this, CartActivity::class.java)
                startActivity(Intent)
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