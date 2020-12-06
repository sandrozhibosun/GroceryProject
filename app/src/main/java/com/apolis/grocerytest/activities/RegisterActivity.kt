package com.apolis.grocerytest.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.MenuItemCompat
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.RequestFuture
import com.android.volley.toolbox.Volley
import com.apolis.grocerytest.R
import com.apolis.grocerytest.app.EndPoint
import com.apolis.grocerytest.database.DBhelper
import com.apolis.grocerytest.helper.SessionManager
import com.apolis.grocerytest.models.User
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.app_bar.*
import kotlinx.android.synthetic.main.menu_cart.view.*
import org.json.JSONObject

class RegisterActivity : AppCompatActivity() {
    lateinit var dBhelper: DBhelper
    private var textViewCartCount: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        dBhelper=DBhelper(this)

        regisInit()
    }

    override fun onResume() {
        super.onResume()
        regisInit()
    }

    private fun regisInit() {
        setupToolbar()
        CreateAccount.setOnClickListener {
            if (TextUtils.isEmpty(email_text_register.text)) {
                Toast.makeText(applicationContext, "name cannot be null", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(password_text_register.text)) {
                Toast.makeText(applicationContext, "password cannot be null", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener


            }
            var name: String = name_text_register.text.toString()
            var email: String = email_text_register.text.toString()
            var password: String = password_text_register.text.toString()
            var mobile: String = mobile_text_register.text.toString()

            var paras = HashMap<String, String>()

            paras["firstName"] = name
            paras["email"] = email
            paras["password"] = password
            paras["mobile"] = mobile


            var jsonObject = JSONObject(paras as Map<*, *>)


            var request = JsonObjectRequest(Request.Method.POST, EndPoint.getRegister(), jsonObject,
                Response.Listener {
                    Toast.makeText(applicationContext, "Register successfully", Toast.LENGTH_SHORT)
                        .show()
                    var intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                },
                Response.ErrorListener {
                    Toast.makeText(applicationContext, "Register failed", Toast.LENGTH_SHORT).show()
                })
            Volley.newRequestQueue(this).add(request)
        }

        alreadyRegister_button.setOnClickListener() {
            var intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

    }
    private fun setupToolbar() {


        var toolbar = toolbar
        toolbar.title = "History Order"

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