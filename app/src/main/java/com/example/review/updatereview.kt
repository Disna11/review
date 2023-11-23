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


class updatereview : AppCompatActivity() {
    private var preferenceHelper: preferenceHelper? =null
    private var postReview: EditText?=null
    private  var updateReviewButton: Button?=null

    var url = "http://192.168.0.9/PHPREST/api/updateReview.php?op=1";
    var updateReview = url + "&updateReview=1"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.updatereview)
        preferenceHelper =  preferenceHelper(applicationContext)
        postReview = findViewById(R.id.post_review)
        updateReviewButton= findViewById(R.id.postButton)
        updateReviewButton!!.setOnClickListener {

            updateReviewFunction()

        }
    }

    private fun updateReviewFunction(){
        var upReview = postReview!!.text.toString()
        var user_id= preferenceHelper!!.getString("id", "")
        var review_id= preferenceHelper!!.getString("clickedReviewId", "")

        val queue = Volley.newRequestQueue(this)  // creates a new instance of requestQueue
        //to create a stringrequestobject and add it to vollyrequest queue for execution
        val stringRequest = object : StringRequest(
            com.android.volley.Request.Method.POST,
            updateReview,
            Response.Listener<String>{ response ->      // response variable store the data returned from the server as string

                try {
                    val obj = JSONObject(response)
                    val isError = obj.getBoolean("error")
                    val errorMessage = obj.getString("message")
                    //check if the error is true if not print the  message
                    if(!isError){
                        Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
                        val myIntent = Intent(applicationContext, displayReviews::class.java)
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
                params.put("update_review",upReview )
                params.put("user_id",user_id)
                params.put("review_id",review_id)

                return params
            }
        }
        queue.add(stringRequest)
    }
}