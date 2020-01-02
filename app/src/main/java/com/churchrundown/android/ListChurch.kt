package com.churchrundown.android

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import entity.Data
import entity.Organizer
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import services.OrganizerService
import services.RundownService
import utility.DisplayUtil
import java.util.*

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

        val searchTextBox = findViewById<EditText>(R.id.searchTextBox)
        searchTextBox.setOnKeyListener { _, keyCode, event ->
            if (event.action === KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                searchChurch(searchTextBox.text.toString())
                true
            }

            false
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

    fun searchChurch(churchName: String) {
        var retrofit = RetrofitMain.retrofit

        val service =
            retrofit!!.create(OrganizerService::class.java)

        val call = service.getOrganizer(churchName)

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

    fun clickHandler(view: View) {
        val churchId = view.id

        var retrofit = RetrofitMain.retrofit

        val service =
            retrofit!!.create(RundownService::class.java)

        val calendar = Calendar.getInstance()

        val currentDay = DisplayUtil.getDisplayedFormatTime(calendar.get(Calendar.DAY_OF_MONTH))
        val currentMonth = DisplayUtil.getDisplayedFormatTime(calendar.get(Calendar.MONDAY) + 1)
        val currentYear = calendar.get(Calendar.YEAR).toString()


        val startDate = currentYear + "-" + currentMonth + "-" + currentDay + "T00:00:00Z"
        val endDate = currentYear + "-" + currentMonth + "-" + currentDay + "T24:00:00Z"

        val call = service.getByOrganizerId(churchId, startDate, endDate)

        call.enqueue(object : Callback<Data> {
            override fun onFailure(call: Call<Data>, t: Throwable) {
                println("LOG MESSAGE = " + t.message)
            }

            override fun onResponse(
                call: Call<Data>,
                response: Response<Data>
            ) {
                val intent = Intent(applicationContext, RundownListActivity::class.java)
                val rundowns = JSONArray(response.body()!!.data)

                intent.putExtra("rundowns", rundowns.toString())
                intent.putExtra("organizerId", "4")
                startActivity(intent)
            }
        })
    }
}