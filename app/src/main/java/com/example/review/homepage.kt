package com.example.review

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class homepage : AppCompatActivity() {
//declare the variables
    var profileUpdate: TextView? = null
    var reviews: TextView?= null
    var postReview: TextView? = null
 //   var myMessage: TextView?= null
    var logOutBtn: Button?=null
    val preferenceHelper = preferenceHelper(this)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.homepage)
//initialise the variables
        profileUpdate= findViewById(R.id.update)
        reviews      = findViewById(R.id.all_reviews)
        postReview   = findViewById(R.id.post)
        logOutBtn    = findViewById(R.id.logout)
//        myMessage    = findViewById(R.id.)

        //if profile update is selected go to activity7

        profileUpdate!!.setOnClickListener {
            val myIntent = Intent(this, updateprofile::class.java)
            startActivity(myIntent)
        }

        //if clicked on reviews

        reviews!!.setOnClickListener {

            val myIntent = Intent(this, displayReviews::class.java)
            startActivity(myIntent)

        }

        //if clicked on post

        postReview!!.setOnClickListener {

            val myIntent = Intent(this, PostReview::class.java)
            startActivity(myIntent)
        }

        //if clicked on my messages

//        myMessage!!.setOnClickListener{
//
//            val myIntent = Intent(this, MainActivity9::class.java)
//            startActivity(myIntent)
//        }
        logOutBtn!!.setOnClickListener {
// to clear values stored in shared preference
            preferenceHelper.clearSharedPreferences()
          // logging out go to logout page
            val myIntent = Intent(this, login::class.java)
            startActivity(myIntent)
        }




    }
}