package com.churchrundown.android

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.card.MaterialCardView
import com.google.gson.GsonBuilder
import entity.Data
import entity.Rundown
import org.json.JSONArray
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import services.RundownItemService
import services.RundownService
import java.io.StringReader
import java.time.LocalDate
import java.time.format.DateTimeFormatter


class RundownListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rundown_list)

        val intent = getIntent()
        val rundowns = intent.getStringExtra("rundowns")
        val rundownItemContainer = findViewById<LinearLayout>(R.id.rundownItemContainer)

        val gsonBuilder = GsonBuilder().serializeNulls()
        val gson = gsonBuilder.create()
        val jsonArrayOfRundowns: List<Rundown> = gson.fromJson(StringReader(rundowns), Array<Rundown>::class.java).toList()

        jsonArrayOfRundowns.forEach {
            val layout = layoutInflater.inflate(R.layout.rundown_item, rundownItemContainer, false)
            val rundownItem = layout.findViewById<MaterialCardView>(R.id.rundownItem)

            val dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
            rundownItem.findViewById<TextView>(R.id.rundownItemTitle).text = it.title
            rundownItem.findViewById<TextView>(R.id.rundownItemSubtitle).text = it.subtitle
            rundownItem.findViewById<TextView>(R.id.rundownItemStartTime).text = "Dimulai " + LocalDate.parse(it.startTime, dateFormat).toString()
            rundownItem.findViewById<TextView>(R.id.rundownItemEndTime).text = "Selesai " + LocalDate.parse(it.endTime, dateFormat).toString()

            rundownItem.id = it.id!!.toInt()

            rundownItem.setOnClickListener(View.OnClickListener {
                clickHandler(it)
            })

            rundownItemContainer.addView(rundownItem)
        }
    }

    fun clickHandler(view: View) {
        val rundownItemId = view.id

        var retrofit = RetrofitMain.retrofit

        val service =
            retrofit!!.create(RundownItemService::class.java)

        val call = service.getByRundownId(rundownItemId)

        call.enqueue(object : Callback<Data> {
            override fun onFailure(call: Call<Data>, t: Throwable) {
                println("LOG MESSAGE = " + t.message)
            }

            override fun onResponse(
                call: Call<Data>,
                response: Response<Data>
            ) {
                println("LOG MESSAGE " + response.body())
                val intent = Intent(applicationContext, RundownItemActivity::class.java)
                val rundownItems = JSONArray(response.body()!!.data)

                intent.putExtra("rundownItems", rundownItems.toString())
                startActivity(intent)
            }
        })
    }
}
