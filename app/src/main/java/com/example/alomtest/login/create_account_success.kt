package com.example.alomtest.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.alomtest.R

class create_account_success:AppCompatActivity() {
    lateinit var email: String
    lateinit var password:String
    lateinit var name:String
    lateinit var birthday:String
    lateinit var gender:String
    lateinit var height:String
    lateinit var weight:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_create_account_success)

        email=intent.getStringExtra("useremail").toString()
        password=intent.getStringExtra("userpassword").toString()
        name=intent.getStringExtra("username").toString()
        birthday=intent.getStringExtra("userbirth").toString()
        gender=intent.getStringExtra("usergender").toString()
        weight=intent.getStringExtra("userweight").toString()
        height=intent.getStringExtra("userheight").toString()
        gender=intent.getStringExtra("usergender").toString()

        val click = findViewById<ConstraintLayout>(R.id.background)

        click.setOnClickListener{
            Log.d("click", "클릭감지")

            val intent = Intent(this@create_account_success,first::class.java)
            startActivity(intent)
            finish()
        }

//        val topLayout = findViewById<ConstraintLayout>(R.id.background)
//        topLayout.setOnClickListener {
//            Log.d("root", "디버깅")
//            val intent = Intent(this@create_account_success, first::class.java)
//            startActivity(intent)
//            finish()
//        }


    }


}