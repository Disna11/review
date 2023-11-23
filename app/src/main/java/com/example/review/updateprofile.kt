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

class updateprofile : AppCompatActivity() {

    var updateFullName: EditText? = null
    var updateUserName: EditText? = null
    var updatePhone: EditText? = null
    var updatePassword: EditText? = null
    var updateBtn: Button? = null

    var preferenceHelper: preferenceHelper? =null
    var preUserName: String? = null
    var prePassword: String? = null
    var userid: String? = null

    //initializing shared preference
    val defaultValue = ""

    var url = "http://192.168.0.9/PHPREST/api/update.php?op=1";
    var updateProfile = url + "&updateProfile=1"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.updateprofile)

        updateFullName= findViewById(R.id.update_fullname)
        updateUserName=findViewById(R.id.update_username)
        updatePhone=findViewById(R.id.update_phone)
        updatePassword=findViewById(R.id.update_password)
        updateBtn=findViewById(R.id.update_save)

        //retrieve the data stored in recyclerview
        preferenceHelper = preferenceHelper(applicationContext)
         preUserName= preferenceHelper!!.getString("globalUserName", defaultValue)
        prePassword= preferenceHelper!!.getString("globalPassword", defaultValue)
        userid= preferenceHelper!!.getString("id", defaultValue)
        //when update button clicked go to  update userprofile function
        updateBtn!!.setOnClickListener {
            if (updateFullName?.text.toString().isNullOrBlank()) {
                Toast.makeText(this, "fullname is required", Toast.LENGTH_SHORT).show()
            }
            else if(updateUserName?.text.toString().isNullOrBlank()){
                Toast.makeText(this, "username is required", Toast.LENGTH_SHORT).show()
            }
            else if (updatePhone?.text.toString().isNullOrBlank()) {
            Toast.makeText(this, "phone number is required", Toast.LENGTH_SHORT).show()
             }
            else if(updatePassword?.text.toString().isNullOrBlank()){
            Toast.makeText(this, "password is required", Toast.LENGTH_SHORT).show()
        } else {
                updateUserProfile()
            }
        }

    }

    private fun updateUserProfile(){
    //data is converted to string
        var updateFullName = updateFullName!!.text.toString()
        var updateUserName =updateUserName!!.text.toString()
        var updatePhone = updatePhone!!.text.toString()
        var updatePassword= updatePassword!!.text.toString()


        val queue = Volley.newRequestQueue(this)

        val stringRequest = object : StringRequest(
            com.android.volley.Request.Method.POST,
            updateProfile,
            Response.Listener<String>{ response ->
                try {
                    val obj = JSONObject(response)
                    val error = obj.getBoolean("error")
                    val errorMessage = obj.getString("message")

                    if (error) {
                        // updation failed
                        Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()

                    } else {

                        // updation successful
                        // update shared preferenec
                        preferenceHelper?.saveString("globalUserName", updateUserName)
                        preferenceHelper?.saveString("globalPassword", updatePassword)

                        Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()

                        //go to homepage activity

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
                //data is passed to server using post method
                params.put("full_name",updateFullName )
                params.put("user_name", updateUserName)
                params.put("phone_no", updatePhone)
                params.put("password", updatePassword)
                params.put("userid",userid!!)
                //params.put("actualPassword", prePassword!!)
                return params
            }
        }

        queue.add(stringRequest)

    }
}