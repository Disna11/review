package com.example.review

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import org.json.JSONObject


class adapter(private val context: Context,
              private val preferenceHelper: preferenceHelper,
              private val itemList: List <item1>) : RecyclerView.Adapter<adapter.myviewholder>(){

//define the url and values to be passed with the url when each function called

    var url = "http://192.168.0.9/PHPREST/api/recyclerFunctions.php?op=1"
    var like_button = url + "&likeButtonClicked=1"
    var dislike_button = url + "&dislikeButtonClicked=1"
    var delete_button = url + "&deleteButtonClicked=1"

// function for crating layout file and inflating it
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myviewholder {
        val itemView= LayoutInflater.from(parent.context).inflate(R.layout.item_layout,parent,false)
        return myviewholder(itemView)
    }
//it will return the number of items
    override fun getItemCount(): Int {
        return itemList.size
    }

    //it will populate the views with data

    override fun onBindViewHolder(holder: myviewholder, position: Int) {

       val userId= preferenceHelper!!.getString("id","")// get the userid stored in shared preference

        val currentItem = itemList[position]        //retrieve the item from item list and set it at correct position
        holder.username.text = currentItem.userId
        holder.Review.text = currentItem.review
        holder.like_count.text= currentItem.reviewLike.toString()
        holder.dislike_count.text= currentItem.reviewDislike.toString()
        holder.admin_replay.text= currentItem.replyMessage.toString()


    //when like button clicked in recyclerview

        holder.like_button?.setOnClickListener(){
            //retrieve the review id
            var review_id= currentItem.reviewId.toString()

            val queue = Volley.newRequestQueue(context)
            val stringRequest = object : StringRequest(
                com.android.volley.Request.Method.POST,
                like_button,
                Response.Listener<String>{ response ->


                        try {
                            //if the response is empty then print error message
                            if(JSONObject(response).getBoolean("error")){
                                val message= JSONObject(response).getString("message")
                                Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                            } else {

                                val jsonObject = JSONObject(response)
                                val data = jsonObject.getString("data")
                                val message = jsonObject.getString("message")
                                val isError = jsonObject.getBoolean("error")
                                if (!isError) {


                                    holder.like_count.text = data
                                }

                            }


                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }

                },object : Response.ErrorListener{
                    override fun onErrorResponse(error: VolleyError?) {
                        if (error != null) {
                            Toast.makeText(context.applicationContext, error.message, Toast.LENGTH_LONG).show()
                        };
                    }
                })

            {
                @Throws(AuthFailureError::class)
                override fun getParams(): Map<String, String> {
                    val params = HashMap<String, String>()
                    //here we pass rewiew_id to the server using post method
                    params.put("review_id",review_id )

                    return params
                }
            }
            queue.add(stringRequest)
        }

    //when dislike is clicked
        holder.dislike_button?.setOnClickListener(){
        //review_id is stored
            var review_id= currentItem.reviewId.toString() //get the review id


            val queue = Volley.newRequestQueue(context)
            val stringRequest = object : StringRequest(
                com.android.volley.Request.Method.POST,
                dislike_button,
                Response.Listener<String>{ response ->



                        try {
                            if(JSONObject(response).getBoolean("error")){
                                val message= JSONObject(response).getString("message")
                                Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                            } else {

                                val jsonObject = JSONObject(response)
                                val data = jsonObject.getString("data")
                                val message = jsonObject.getString("message")
                                val isError = jsonObject.getBoolean("error")
                                if (!isError) {


                                    holder.dislike_count.text = data
                                }

                            }


                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }

                },object : Response.ErrorListener{
                    override fun onErrorResponse(error: VolleyError?) {
                        if (error != null) {
                            Toast.makeText(context.applicationContext, error.message, Toast.LENGTH_LONG).show()
                        };
                    }
                })

            {
                @Throws(AuthFailureError::class)
                override fun getParams(): Map<String, String> {
                    val params = HashMap<String, String>()
                    //review id is passed using post method
                    params.put("review_id",review_id )

                    return params
                }
            }
            queue.add(stringRequest)
        }

        holder.review_delete?.setOnClickListener(){


            var review_id= currentItem.reviewId.toString() //get the review id
            //retrieved userid is stored in a variable
            var user_id=userId


            val queue = Volley.newRequestQueue(context)
            val stringRequest = object : StringRequest(
                com.android.volley.Request.Method.POST,
                delete_button,
                Response.Listener<String>{ response ->
                    try {
                        //check the response sended from server and print apprpriate messages
                        val obj = JSONObject(response)
                        val isError = obj.getBoolean("error")
                        val errorMessage = obj.getString("message")
                        if (isError) {
                            Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
                        } else {
                            Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
                        }
                    }
                    catch (e: JSONException){
                        e.printStackTrace()
                    }
                },object : Response.ErrorListener{
                    override fun onErrorResponse(error: VolleyError?) {
                        if (error != null) {
                            Toast.makeText(context.applicationContext, error.message, Toast.LENGTH_LONG).show()
                        };
                    }
                })

            {
                @Throws(AuthFailureError::class)
                override fun getParams(): Map<String, String> {
                    val params = HashMap<String, String>()
                //here we pass review id and user id so that only the review creator can delete review
                    params.put("review_id",review_id )
                   params.put("user_id",user_id !!)
                    return params
                }
            }
            queue.add(stringRequest)
        }

        //when update button clicked in recyclerview
        holder.review_update?.setOnClickListener {

            //review id is retrieved and stored in shared preference
            //also userid is retrieved from sharedpreference
            var review_id= currentItem.reviewId.toString()

            preferenceHelper?.saveString("clickedReviewId", review_id)
            val userId= preferenceHelper!!.getString("id","")
            //check if the user id of the review isequalto the logged in users id if yes navigate to activitymain 9
            if(userId ==  currentItem.userId.toString()){

                val myIntent = Intent(context, updatereview::class.java)
                myIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
                context.startActivity(myIntent)

            }
            else{
                Toast.makeText(context, "you have no permission to update", Toast.LENGTH_LONG).show()
            }

        }

        //when reply button clicked
        holder.review_message?.setOnClickListener {
            //userid and reviewid of the review is retrieved and stored in shared preference
            var review_id= currentItem.reviewId.toString()
            var user_id=currentItem.userId.toString()
            preferenceHelper?.saveString("clickedReviewId", review_id)
            preferenceHelper?.saveString("clickedUserId", user_id)
            //also username and password of logged in users will be retrieved from the shared preference
            var userName= preferenceHelper!!.getString("globalUserName", "")
            var Password= preferenceHelper!!.getString("globalPassword","")

            //only admin have the permission to send message
            if(userName=="admin" && Password=="admin"){
                val myIntent = Intent(context, replaymessage::class.java)
                myIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
                context.startActivity(myIntent)
            }
            else{
                Toast.makeText(context, "only admin can send message", Toast.LENGTH_LONG).show()
            }

        }


    }

    //this class holds the references to the items in the item_layout.xml file

    class myviewholder(itemView : View) : RecyclerView.ViewHolder(itemView){

        val username: TextView = itemView.findViewById(R.id.username_recyclerview)
        val Review: TextView = itemView.findViewById(R.id.Review)
        val like_count: TextView = itemView.findViewById(R.id.like_count)
        val dislike_count: TextView = itemView.findViewById(R.id.dislike_count)
        val like_button: ImageButton = itemView.findViewById(R.id.like_button)
        val dislike_button: ImageButton = itemView.findViewById(R.id.dislike_button)
        val review_delete: Button =itemView.findViewById(R.id.review_delete)
        val review_update: Button =itemView.findViewById(R.id.review_update)
        val review_message:Button=itemView.findViewById(R.id.send_message)
        val admin_replay: TextView = itemView.findViewById(R.id.ad_message)


    }


}