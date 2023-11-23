package com.example.review

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import org.json.JSONObject


class displayReviews : AppCompatActivity() {
//declare variables
    private lateinit var  rvList: RecyclerView
    var backbutton: Button?=null
    private lateinit var newArrayList: ArrayList<item1>
    var url = "http://192.168.0.9/PHPREST/api/getFullData.php?op=1";
    var fullData = url + "&fullData=1"

    private var preferenceHelper: preferenceHelper? =null
    var UserName: String? = null
    var Password: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.displayreviews)


        rvList= findViewById(R.id.review_list)//initialise rvlist with recyclerview
        backbutton= findViewById(R.id.review_back)
        rvList.layoutManager= LinearLayoutManager(this)//for ensuring the items in the recyclerview is displayed in a
        // linear vertical manner
//      //get the username and password stord in sharedpreference
        preferenceHelper =  preferenceHelper(applicationContext)
        UserName= preferenceHelper!!.getString("globalUserName","")
        Password= preferenceHelper!!.getString("globalPassword", "")
//to store data to array list with the item1 data class
        newArrayList = arrayListOf()
        newArrayList.add(item1())



        val queue = Volley.newRequestQueue(this)

        val stringRequest = object : StringRequest(
            com.android.volley.Request.Method.POST,
            fullData,
            {
                    response ->
                try {

                    val jsonObject =  JSONObject(response)
                    val isError = jsonObject.getBoolean("error")
                    val errorMessage = jsonObject.getString("message")
                    if(isError){
                        //failed to display review
                        Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
                    } else{
                        val dataArray = jsonObject.getJSONArray("data")
                        val itemlist1= mutableListOf< item1 >()

                        //here the data passed from the server is retrieved and passed to the adapter for populating the recyclerview


                        for (i in 0  until  dataArray.length() ){


                            val jsonObject= dataArray.getJSONObject(i)

                            val userId= jsonObject.getString("user_id")
                            val reviewId= jsonObject.getString("reviewid")
                            val review= jsonObject.getString("review")
                            val reviewLike= jsonObject.getString("like")
                            val reviewDislike= jsonObject.getString("dislike")
                            val replyMessage= jsonObject.getString("messages")
                            val item= item1(userId,reviewId,review,reviewLike,reviewDislike,replyMessage)
                            itemlist1.add(item)


                        }

                        //recyclerview is connected to adapter
                        rvList.adapter =  adapter(this, preferenceHelper!!,itemlist1)



                    }


                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },object : Response.ErrorListener {
                override fun onErrorResponse(volleyError: VolleyError) {
                    Toast.makeText(applicationContext, volleyError.message, Toast.LENGTH_LONG).show()
                }
            })
        {


        }

        queue.add(stringRequest)

        backbutton!!.setOnClickListener{
            val myIntent = Intent(applicationContext, homepage::class.java)
            startActivity(myIntent)
        }

    }



}






