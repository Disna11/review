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


class replaymessage : AppCompatActivity() {

    private var preferenceHelper: preferenceHelper? =null
    private var adminMessage: EditText?=null
    private  var sendButton: Button?=null

    var url = "http://192.168.0.9/PHPREST/api/replyMessage.php?op=1";
    var sendResponse = url + "&sendResponse=1"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.replaymessage)

        preferenceHelper =  preferenceHelper(applicationContext)
        adminMessage = findViewById(R.id.admin_message_input)
        sendButton= findViewById(R.id.send_button)
//if send button is clicked go to sendResponsefunction
        sendButton!!.setOnClickListener {

           sendResponsefunction()

        }
    }

    private fun sendResponsefunction(){

        var adminMessage = adminMessage!!.text.toString()
        //retrieved the userid and reviewid of which the admin wnated to send replay
        var user_id= preferenceHelper!!.getString("clickedUserId", "")
        var review_id= preferenceHelper!!.getString("clickedReviewId", "")

        val queue = Volley.newRequestQueue(this)  // creates a new insstance of requestQueue
        //to create a stringrequestobject and add it to vollyrequest queue for execution
        val stringRequest = object : StringRequest(
            com.android.volley.Request.Method.POST,
            sendResponse,
            Response.Listener<String>{ response ->      // response variable store the data returned from the server as string

                try {
                    val obj = JSONObject(response)
                    val isError = obj.getBoolean("error")
                    val errorMessage = obj.getString("message")
                    //if the error is not true
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
                params.put("message",adminMessage )
                params.put("user_id",user_id)
                params.put("review_id",review_id)

                return params
            }
        }
        queue.add(stringRequest)

    }
}
