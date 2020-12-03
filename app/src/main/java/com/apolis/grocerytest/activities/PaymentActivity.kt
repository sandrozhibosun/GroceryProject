package com.apolis.grocerytest.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.apolis.grocerytest.R
import com.apolis.grocerytest.adapters.AdapterPayment
import com.apolis.grocerytest.database.DBhelper
import com.apolis.grocerytest.helper.SessionManager
import com.apolis.grocerytest.models.Address

import com.apolis.grocerytest.models.ProductInCart
import kotlinx.android.synthetic.main.activity_cart.*
import kotlinx.android.synthetic.main.activity_payment.*
import kotlinx.android.synthetic.main.activity_payment.checkTotalLayout
import kotlinx.android.synthetic.main.activity_payment.discount_text
import kotlinx.android.synthetic.main.activity_payment.sub_total_text
import kotlinx.android.synthetic.main.activity_payment.total_text
import kotlinx.android.synthetic.main.app_bar.*
import kotlinx.android.synthetic.main.cart_content.*


class PaymentActivity : AppCompatActivity(),View.OnClickListener {

    lateinit var address: Address
    lateinit var dBhelper: DBhelper
    lateinit var sessionManager: SessionManager
    lateinit var adapterPayment:AdapterPayment
    var subtotal=0.00
    var total=0.00
    var discount=0.00

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)


        sessionManager= SessionManager(this)
        dBhelper= DBhelper(this)

        init()
    }

    override fun onResume() {
        super.onResume()
        init()
    }
    private fun init(){
        address = sessionManager.getDefaultAddress()
        setupToolbar()

        
        street_name_text_view.text=address.streetName
        city_text_view.text=address.city
        houseNo_text_view.text=address.houseNo
        text_view_type.text=address.type
        pincode_text_view.text=address.pincode.toString()

        adapterPayment= AdapterPayment(this)
        subTotal()
        getData()


        payment_recycler_view.adapter=adapterPayment
        payment_recycler_view.layoutManager=LinearLayoutManager(this)
        payOnline_button.setOnClickListener(this)
        change_default_address.setOnClickListener(this)


    }
    private fun getData(){

        var productInCartList=dBhelper.readProduct()
        adapterPayment.setData(productInCartList)



    }


    fun subTotal(){
        var addsubTotal:Double=0.00
        var addTotal:Double=0.00
        var productInCartList=ArrayList<ProductInCart>()

        productInCartList=dBhelper.readProduct()
        for(i in productInCartList)
        {
            addTotal+=i.price*i.inCart
            addsubTotal+=i.mrp*i.inCart

        }


        sub_total_text.text="$${addsubTotal.toString()}"
        total_text.text="$${addTotal.toString()}"
        discount_text.text="$${(addsubTotal-addTotal).toString()}"
        subtotal=addsubTotal
        total=addTotal
        discount=(subtotal-total)

    }

    override fun onClick(v: View?) {
        when(v){
            payCash_button->{

            }
            payOnline_button->{
                var intent= Intent(this,OrderConfirmationActivity::class.java)
                intent.putExtra(Address.Address_Key,address)
                intent.putExtra("SUBTOTAL",subtotal)
                intent.putExtra("TOTAL",total)
                intent.putExtra("DISCOUNT",discount)
                startActivity(intent)
                finish()
            }
            change_default_address->{
                startActivity(Intent(this,AddressListActivity::class.java))
            }

        }
    }
    private fun setupToolbar() {


        var toolbar = toolbar
        toolbar.title = "Order Summary"

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