package com.example.simpleyelp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val TAG = "MainActivity"
private const val BASE_URL = "https://api.yelp.com/v3/"
private const val API_KEY = "KYGzERHgZ0bG0bkHQWq3M9OvdRow_eE7NgsPLepgzLg_mb8P3aDIDnFPXgSj5aKUCLFxDh7uT51IzSzJnnNKLT85q9SRa6aAv4Y2938CBmFpSJzj5IqYcqEuRhqOYXYx"
class MainActivity : AppCompatActivity() {
    private lateinit var rvRestaurants: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val restaurants = mutableListOf<YelpRestaurant>()
        val adapter = RestaurantsAdapter(this, restaurants)

        rvRestaurants = findViewById<RecyclerView>(R.id.rvRestaurants)
        rvRestaurants.adapter = adapter
        rvRestaurants.layoutManager = LinearLayoutManager(this)

        val retrofit = Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build()
        val yelpService = retrofit.create(YelpService::class.java)
        yelpService.searchRestaurants("Bearer $API_KEY","Avocado Toast", "New York").enqueue(object : Callback<YelpSearchResult> {
            override fun onResponse(call: Call<YelpSearchResult>, response: Response<YelpSearchResult>) {
                Log.i(TAG, "onResponse $response")
                val body = response.body()
                if(body == null)
                {
                    Log.w(TAG, "did not receive valid response body from Yelp API...exiting")
                    return
                }
                restaurants.addAll(body.restaurants)
                adapter.notifyDataSetChanged()
            }

            override fun onFailure(call: Call<YelpSearchResult>, t: Throwable) {
                Log.i(TAG, "onFailure $t")
            }

        })


    }
}