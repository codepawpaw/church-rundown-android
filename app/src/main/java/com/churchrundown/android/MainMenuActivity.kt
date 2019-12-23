package com.churchrundown.android

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import entity.Organizer
import kotlinx.android.synthetic.main.activity_main_menu.*
import org.json.JSONArray
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import services.OrganizerService

class MainMenuActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        RetrofitMain.build()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_scrolling, menu)
        return true
    }

    fun clickHandler(view : View) {
        var churchName = churchName.text

        var retrofit = RetrofitMain.retrofit

        val service =
            retrofit!!.create(OrganizerService::class.java)

        val call = service.getOrganizer(churchName.toString())

        call.enqueue(object : Callback<List<Organizer>> {
            override fun onFailure(call: Call<List<Organizer>>, t: Throwable) {
                println("LOG MESSAGE = " + t.message)
            }

            override fun onResponse(
                call: Call<List<Organizer>>,
                response: Response<List<Organizer>>
            ) {
                val intent = Intent(applicationContext, ListChurch::class.java)
                val organizers = response.body()

                val listOfOrganizer = JSONArray()
                organizers!!.forEach {
                    listOfOrganizer.put(it.toJSON())
                }

                intent.putExtra("organizers", listOfOrganizer.toString())
                startActivity(intent)
            }
        })
    }
}