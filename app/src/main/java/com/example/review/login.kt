package com.example.review

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import org.json.JSONObject

class login : AppCompatActivity() {

    //declare variables
    var edtusername: EditText? = null
    var edtpassword: EditText?= null
    var btnlogin: Button?=null
    var signup: TextView?=null
    //declare the url
    var url = "http://192.168.0.9/PHPREST/api/login.php?op=1";
    var login = url + "&userLogin=1"
    var preferenceHelper: preferenceHelper? =null // declare a variable for shared preference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)

        // initialising shared preference for storing data
       preferenceHelper = preferenceHelper(applicationContext)

//get the values from the xml file
        edtusername= findViewById(R.id.username)
        edtpassword= findViewById(R.id.password)
        btnlogin= findViewById(R.id.log_button)
        signup=findViewById(R.id.signup)
//when the login button is clicked
        btnlogin!!.setOnClickListener {
            if (edtusername?.text.toString().isNullOrBlank()) {
                Toast.makeText(this, "Username  required", Toast.LENGTH_SHORT).show()
            }  else if(edtpassword?.text.toString().isNullOrBlank()){
                Toast.makeText(this, "password  required", Toast.LENGTH_SHORT).show()
            } else{
                userLogin()  //go to function userlogin
            }

        }
        // when the sign_up button is clicked go to registeActivity
        signup!!.setOnClickListener{
            val myIntent= Intent(this, registerActivity::class.java)
            startActivity(myIntent)
        }
    }
    //function for userLogin

    private fun userLogin(){
        //get the values of username and password in a string format

       var edtusername = edtusername!!.text.toString()
        var edtpassword= edtpassword!!.text.toString()

        val queue = Volley.newRequestQueue(this)  // creates a new insstance of requestQueue
        //to create a stringrequestobject and add it to vollyrequest queue for execution
        val stringRequest = object : StringRequest(
            com.android.volley.Request.Method.POST,
            login,
            Response.Listener<String>{ response ->      // response variable store the data returned from the server as string

                try {
                    //if the response is empty then print error message
                    if(JSONObject(response).getBoolean("error")){
                        val message= JSONObject(response).getString("message")
                        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
                    } // get the data sended from the server and store it in a jasonarray called array

                    else {
                        val jsonObject =  JSONObject(response)
                        val data = jsonObject.getJSONObject("data")
                        val message= jsonObject.getString("message")
                        val isError= jsonObject.getBoolean("error")
                        if (!isError ){


                            val userId = data.getString("id")
                            val fullName = data.getString("full_name")
                            val email = data.getString("email")
                            val phoneNo = data.getString("phone_no")
                            val userName = data.getString("username")
                            val password = data.getString("password")
//
                            preferenceHelper?.saveString("globalUserName", userName)
                            preferenceHelper?.saveString("globalPassword", password)
                            preferenceHelper?.saveString("id", userId)
                            preferenceHelper?.saveString("fullName", fullName)
                            preferenceHelper?.saveString("email", email)
                            preferenceHelper?.saveString("phoneno", phoneNo)


                        //after successful login  print login successfull message

                            Toast.makeText(this, message, Toast.LENGTH_LONG).show()

                            // go to Activitymain3
                        val myIntent = Intent(applicationContext, homepage::class.java)
                        startActivity(myIntent)
                        }else{
                            Toast.makeText(this, message, Toast.LENGTH_LONG).show()
                        }
                    }}catch (e: JSONException){
                    e.printStackTrace()
                }
            },
            // to handle any errors that may occur during network request
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

                //add the values into the hashmap to pass iti to API
                val params = HashMap<String, String>()
                params.put("username", edtusername)
                params.put("password", edtpassword)
                return params
            }
        }
        // theobject called StringRequest is Added to the requestqueue
        queue.add(stringRequest)
    }

}















