package com.apolis.grocerytest.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import com.apolis.grocerytest.R
import com.apolis.grocerytest.helper.SessionManager
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity() {


    private val delayedTime:Long = 2000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        var handler =Handler()
        handler.postDelayed({
            checkLogin()

        },delayedTime)



    }

    override fun onResume() {
        super.onResume()
        checkLogin()
    }

    private fun checkLogin(){

        if(SessionManager(this).isLoggedIn()){
            var intent= Intent(this,MainActivity::class.java)
            startActivity(intent)
            finish()
        }else{
            init()
            splash_progressbar.visibility= View.GONE
        }


    }
    private fun init(){
        login_button.setOnClickListener {
            startActivity(Intent(this,LoginActivity::class.java))
        }
        register_button.setOnClickListener {
            startActivity(Intent(this,RegisterActivity::class.java))
    }
        guestIn_button.setOnClickListener {
            startActivity(Intent(this,MainActivity::class.java))
        }

    }

//    override fun onClick(v: View?) {
//
//        when(v){
//            login_button->{
//                Log.d("abc","clicked")
//                startActivity(Intent(this,LoginActivity::class.java))}
//            register_button->{
//                startActivity(Intent(this,RegisterActivity::class.java))}
//            guestIn_button->{
//                startActivity(Intent(this,MainActivity::class.java))}
//        }
//
//    }

}