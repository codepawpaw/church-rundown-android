package com.churchrundown.android

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import entity.Data
import entity.Organizer
import entity.Rundown
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import services.OrganizerService
import services.RundownService

class ListChurch : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_church)

        val intent = getIntent()
        val organizers = intent.getStringExtra("organizers")
        val listOfChurch = findViewById<LinearLayout>(R.id.listOfChurch)

        val jsonArrayOfOrganizers = JSONArray(organizers)

        if(jsonArrayOfOrganizers.length() <= 0) {
            val churchResultElement = findViewById<LinearLayout>(R.id.church_result)

            val notFoundLayout = LayoutInflater.from(applicationContext).inflate(R.layout.not_found, null)
            churchResultElement.addView(notFoundLayout)
        }

        for (i in 0 until jsonArrayOfOrganizers.length()) {
            val organizerObject = JSONObject(jsonArrayOfOrganizers[i].toString())
            var churchId = organizerObject.getString("id")
            val churchName = organizerObject.getString("name")
            val churchDescription = organizerObject.getString("description")

            val layout = LayoutInflater.from(applicationContext).inflate(R.layout.church_item, null)
            layout.findViewById<TextView>(R.id.churchName).text = churchName
            layout.findViewById<TextView>(R.id.churchDesc).text = churchDescription
            layout.id = churchId.toInt()
            layout.setOnClickListener(View.OnClickListener {
                clickHandler(it)
            })
            listOfChurch.addView(layout)
        }
    }

    fun clickHandler(view: View) {
        val churchId = view.id

        var retrofit = RetrofitMain.retrofit

        val service =
            retrofit!!.create(RundownService::class.java)

        val call = service.getByOrganizerId(churchId)

        call.enqueue(object : Callback<Data> {
            override fun onFailure(call: Call<Data>, t: Throwable) {
                println("LOG MESSAGE = " + t.message)
            }

            override fun onResponse(
                call: Call<Data>,
                response: Response<Data>
            ) {
                println("LOG MESSAGE " + response.body())
                val intent = Intent(applicationContext, RundownListActivity::class.java)
                val rundowns = JSONArray(response.body()!!.data)

                intent.putExtra("rundowns", rundowns.toString())
                startActivity(intent)
            }
        })
    }
}