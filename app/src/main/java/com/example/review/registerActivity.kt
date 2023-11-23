package com.example.review

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import org.json.JSONObject

class registerActivity : AppCompatActivity() {
    //declare variables
    var fname: EditText? = null
    var email: EditText? = null
    var username: EditText?= null
    var phone_no: EditText? = null
    var password: EditText? = null
    var register: Button? = null
//declare the url
    var url = "http://192.168.0.9/PHPREST/api/register.php?op=1"
    var userregister = url + "&userregister=1"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register)
//initialise the variables
        fname = findViewById(R.id.fname)
        email= findViewById(R.id.email)
        phone_no=findViewById(R.id.phone)
        password=findViewById(R.id.password)
        username= findViewById(R.id.username_signup)
        register=findViewById(R.id.register)

        //if register button is clicked goto registration function
        register!!.setOnClickListener {
            if (fname?.text.toString().isNullOrBlank()) {
                Toast.makeText(this, "fullname is required", Toast.LENGTH_SHORT).show()
            }  else if(email?.text.toString().isNullOrBlank()){
                Toast.makeText(this, "email is required", Toast.LENGTH_SHORT).show()
            } else if(phone_no?.text.toString().isNullOrBlank()){
                Toast.makeText(this, "phone number is required", Toast.LENGTH_SHORT).show()
            } else if(password?.text.toString().isNullOrBlank()){
                Toast.makeText(this, "password is required", Toast.LENGTH_SHORT).show()
            } else if(username?.text.toString().isNullOrBlank()){
                Toast.makeText(this, "username is required", Toast.LENGTH_SHORT).show()
            } else{
                registration()
            }
        }

    }

    private fun registration() {
//convert the values to string and store it in anothe variable
        var fullname = fname!!.text.toString()
        var email = email!!.text.toString()
        var phonenumber = phone_no!!.text.toString()
        var username = username!!.text.toString()
        var password = password!!.text.toString()
        if (fullname.isNotEmpty() && email.isNotEmpty() && phonenumber.isNotEmpty() && username.isNotEmpty() && password.isNotEmpty()) {
            val queue = Volley.newRequestQueue(this)// creates a new insstance of requestQueue
            //to create a stringrequestobject and add it to vollyrequest queue for execution
            val stringRequest = object : StringRequest(
                com.android.volley.Request.Method.POST,
                userregister,
                Response.Listener<String> { response ->  // response variable store the data returned from the server as string
                    try {
                        val obj = JSONObject(response)
                        val error = obj.getBoolean("error")
                        val errorMessage = obj.getString("message")
                        if (error) {
                            // Login failed, show an error message

                            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
                        } else {
                            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
                            // registration successful, go to the next activity
                            val myIntent = Intent(this, login::class.java)
                            startActivity(myIntent)

                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                },
                object : Response.ErrorListener {
                    override fun onErrorResponse(error: VolleyError?) {
                        if (error != null) {
                            Toast.makeText(applicationContext, error.message, Toast.LENGTH_LONG).show()
                        }
                    }
                }) {

                @Throws(AuthFailureError::class)
                override fun getParams(): Map<String, String> {
                    val params = HashMap<String, String>()
                    //send datas to server using post method
                    params.put("fullname", fullname)
                    params.put("email", email)
                    params.put("phonenumber", phonenumber)
                    params.put("username", username)
                    params.put("password", password)


                    return params
                }

            }
            queue.add(stringRequest)
        }else{
            Toast.makeText(applicationContext, "missing fields", Toast.LENGTH_LONG).show()
        }


    }
}