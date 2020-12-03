package com.apolis.grocerytest.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.apolis.grocerytest.R
import com.apolis.grocerytest.app.EndPoint
import com.apolis.grocerytest.helper.SessionManager
import com.apolis.grocerytest.models.CategoryResponse
import com.apolis.grocerytest.models.LoginResponse
import com.apolis.grocerytest.models.User
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.app_bar.*
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        init()
    }

    private fun init() {
        setupToolbar()

        login.setOnClickListener {
            Log.d("abc", "clicked login")


            var email: String = email_text.text.toString()
            var password: String = password_text.text.toString()
            var paras = HashMap<String, String>()
            paras["email"] = email
            paras["password"] = password
            var jsonObject = JSONObject(paras as Map<*, *>)
//            var user = User(email = email, password = password)
//            var sessionManager = SessionManager(this)
//            if (sessionManager.login(user)) {
//                finish()
//            } else {
//                Toast.makeText(applicationContext, "Login Failed", Toast.LENGTH_SHORT).show()
//            }
            var request = JsonObjectRequest(
                Request.Method.POST, EndPoint.getLogin(), jsonObject,
                Response.Listener {
                    Toast.makeText(applicationContext, "Login successfully", Toast.LENGTH_SHORT)
                        .show()

                    var gson = Gson()
                    var loginResponse = gson.fromJson(it.toString(), LoginResponse::class.java)
                    var user=User(loginResponse.user._id
                        ,loginResponse.user.firstName,
                        loginResponse.user.email,
                        loginResponse.user.password,
                        loginResponse.user.mobile)

                    var sessionManager = SessionManager(this)
                    sessionManager.login(user)

                    Log.d("abc", "session login: ${sessionManager.isLoggedIn()}")
                    finish()
                },
                Response.ErrorListener {
                    Log.d("abc", "session login:error")
                    Toast.makeText(applicationContext, "Register failed", Toast.LENGTH_SHORT).show()
                })
            Volley.newRequestQueue(this).add(request)


        }

        Signup.setOnClickListener {
            var intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            finish()

        }


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

            else -> {
                Log.d("abc", "can't recon")
                super.onOptionsItemSelected(item)
            }
        }
        return true
    }

}
