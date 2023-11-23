package com.example.review

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import org.json.JSONObject
import java.util.*

class PostReview : AppCompatActivity() {

    var postReview: EditText?=null
    var postButton: Button?=null

    var url = "http://192.168.0.9/PHPREST/api/postReview.php?op=1";
    var posted = url + "&reviewPosted=1"


    var preferenceHelper: preferenceHelper? =null
    var user_id: String? = null
    var Password: String? = null
    val defaultValue = ""



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.postreview)


        postReview=findViewById(R.id.post_review)
        postButton=findViewById(R.id.postButton)

//get the username and password stored in sharedpreference
        preferenceHelper = preferenceHelper(applicationContext)

        user_id= preferenceHelper!!.getString("id", defaultValue)
        Password= preferenceHelper!!.getString("globalPassword", defaultValue)
//if post button is clicked go to postreviews function
        postButton!!.setOnClickListener {
            if (postReview?.text.toString().isNullOrBlank()) {
                Toast.makeText(this, "enter a review", Toast.LENGTH_SHORT).show()
            } else{
                postReviewss()
            }

        }
    }

    private fun postReviewss(){

       var postReview = postReview!!.text.toString()
        val queue = Volley.newRequestQueue(this)

        val stringRequest = object : StringRequest(
            com.android.volley.Request.Method.POST,
            posted,
            Response.Listener<String>{ response ->
                try {
                    val obj = JSONObject(response)
                    //Toast.makeText(this, response.toString(), Toast.LENGTH_LONG).show()

                    val isError = obj.getBoolean("error")
                    val errorMessage = obj.getString("message")

                    if (isError) {
                        //failed to post review
                        Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()


                    } else {
                        //if review is posted successfull then toast a message and go to homepage activity
                        Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
                        val myIntent= Intent(this, homepage::class.java)
                        startActivity(myIntent)
                    }
                }
                catch (e: JSONException){
                    e.printStackTrace()
                }
            },
            object : Response.ErrorListener{
                override fun onErrorResponse(error: VolleyError?) {
                    if (error != null) {
                        Toast.makeText(applicationContext, error.message, Toast.LENGTH_LONG).show()
                    };
                }
            })

        {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                //pass values to server using post method
                params.put("review",postReview )

                params.put("user_id", user_id!!)
               // params.put("actualPassword", Password!!)
                return params
            }
        }
        queue.add(stringRequest)

    }
}