package com.churchrundown.android

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.card.MaterialCardView
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
        val jsonArrayOfOrganizers = JSONArray(organizers)

        render(jsonArrayOfOrganizers)
    }

    fun render(jsonArrayOfOrganizers: JSONArray) {
        val listOfChurch = findViewById<LinearLayout>(R.id.listOfChurch)

        if(jsonArrayOfOrganizers.length() <= 0) {
            val churchListTitle = findViewById<TextView>(R.id.churchListTitle)
            churchListTitle.visibility = View.GONE

            val churchListNotFound = findViewById<LinearLayout>(R.id.churchListNotFound)
            churchListNotFound.visibility = View.VISIBLE
        } else {
            val churchListTitle = findViewById<TextView>(R.id.churchListTitle)
            churchListTitle.visibility = View.VISIBLE
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

            val layout = layoutInflater.inflate(R.layout.church_item, listOfChurch, false)

            val churchItem = layout.findViewById<MaterialCardView>(R.id.churchItem)

            churchItem.findViewById<TextView>(R.id.churchName).text = churchName
            churchItem.findViewById<TextView>(R.id.churchDesc).text = churchDescription
            churchItem.id = churchId.toInt()
            churchItem.setOnClickListener(View.OnClickListener {
                clickHandler(churchId.toInt(), churchName)
            })

            listOfChurch.addView(churchItem)
        }
    }

    fun resetView() {
        val churchListNotFound = findViewById<LinearLayout>(R.id.churchListNotFound)
        churchListNotFound.visibility = View.GONE

        val listOfChurchContainer = findViewById<LinearLayout>(R.id.listOfChurch)
        listOfChurchContainer.removeAllViews()
    }

    fun searchChurch(churchName: String) {
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        progressBar.visibility = View.VISIBLE

        var retrofit = RetrofitMain.retrofit

        val service =
            retrofit!!.create(OrganizerService::class.java)

        val call = service.getOrganizer(churchName)

        call.enqueue(object : Callback<List<Organizer>> {
            override fun onFailure(call: Call<List<Organizer>>, t: Throwable) {
                println("LOG MESSAGE = " + t.message)
                progressBar.visibility = View.GONE
            }

            override fun onResponse(
                call: Call<List<Organizer>>,
                response: Response<List<Organizer>>
            ) {
                progressBar.visibility = View.GONE
                //val intent = Intent(applicationContext, ListChurch::class.java)
                val organizers = response.body()

                val listOfOrganizer = JSONArray()
                organizers!!.forEach {
                    listOfOrganizer.put(it.toJSON())
                }

                resetView()
                render(listOfOrganizer)

                //intent.putExtra("organizers", listOfOrganizer.toString())
                //startActivity(intent)
            }
        })
    }

    fun clickHandler(churchId: Int, churchName: String) {
        val churchId = churchId

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
                intent.putExtra("organizerId", churchId.toString())
                intent.putExtra("churchName", churchName)
                startActivity(intent)
            }
        })
    }
}