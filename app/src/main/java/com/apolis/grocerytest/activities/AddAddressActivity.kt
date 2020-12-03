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
import kotlinx.android.synthetic.main.activity_add_address.*
import kotlinx.android.synthetic.main.app_bar.*
import kotlinx.android.synthetic.main.row_adapter_address.*
import org.json.JSONObject

class AddAddressActivity : AppCompatActivity() {

    lateinit var sessionManager: SessionManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_address)
        sessionManager= SessionManager(this)
        init()
    }

    private fun init(){
        setupToolbar()
         sub_address_button.setOnClickListener {

             var pincode=pincode_text.text.toString().toInt()
             var streetName=street_name_text.text.toString()
             var city=city_text.text.toString()
             var houseNo=houseNo_text.text.toString()
             var type=type_text.text.toString()
             var userId=sessionManager.getUserId()!!

             var paras=HashMap<String,Any>()
             paras["pincode"]=pincode
             paras["streetName"]=streetName
             paras["city"]=city
             paras["houseNo"]=houseNo
             paras["type"]=type
             paras["userId"]=userId

             var jsonObject=JSONObject(paras as Map<*,*>)
             var request=JsonObjectRequest(Request.Method.POST,EndPoint.addAddress(),jsonObject,
                    Response.Listener {
                        Toast.makeText(applicationContext, "add Address successful", Toast.LENGTH_SHORT).show()
                        finish()
             },
                    Response.ErrorListener {
                        Toast.makeText(applicationContext, "add Address failed", Toast.LENGTH_SHORT).show()
                 }
             )
             Volley.newRequestQueue(this).add(request)

         }
    }
    private fun setupToolbar() {


        var toolbar = toolbar
        toolbar.title = "Add Your Address"

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