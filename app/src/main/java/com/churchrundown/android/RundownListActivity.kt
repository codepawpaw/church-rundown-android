package com.churchrundown.android

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
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
import java.io.StringReader
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import android.app.DatePickerDialog
import services.RundownService
import utility.DisplayUtil
import java.text.DateFormatSymbols

class RundownListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rundown_list)

        val intent = getIntent()
        val rundowns = intent.getStringExtra("rundowns")

        val gsonBuilder = GsonBuilder().serializeNulls()
        val gson = gsonBuilder.create()
        val jsonArrayOfRundowns: List<Rundown> = gson.fromJson(StringReader(rundowns), Array<Rundown>::class.java).toList()

        val showTimeEditText = findViewById<EditText>(R.id.showTime)
        val organizerId = intent.getStringExtra("organizerId").toInt()

        showTimeEditText.setOnClickListener(View.OnClickListener {
            showDatePicker(showTimeEditText, organizerId)
        })

        renderRundowns(jsonArrayOfRundowns)
    }

    fun searchRundown(day: String, month: String, year: String, organizerId: Int) {
        var startDate = year + "-" + month + "-" + day + "T00:00:00Z"
        var endDate = year + "-" + month + "-" + day + "T24:00:00Z"

        var retrofit = RetrofitMain.retrofit

        val service =
            retrofit!!.create(RundownService::class.java)

        val call = service.getByOrganizerId(organizerId, startDate, endDate)

        call.enqueue(object : Callback<Data> {
            override fun onFailure(call: Call<Data>, t: Throwable) {
                println("LOG MESSAGE = " + t.message)
            }

            override fun onResponse(
                call: Call<Data>,
                response: Response<Data>
            ) {
                val rundowns = JSONArray(response.body()!!.data).toString()

                val gsonBuilder = GsonBuilder().serializeNulls()
                val gson = gsonBuilder.create()
                val jsonArrayOfRundowns: List<Rundown> = gson.fromJson(StringReader(rundowns), Array<Rundown>::class.java).toList()

                renderRundowns(jsonArrayOfRundowns)
            }
        })
    }

    fun renderRundowns(jsonArrayOfRundowns: List<Rundown>) {
        val rundownItemContainer = findViewById<LinearLayout>(R.id.rundownItemContainer)

        jsonArrayOfRundowns.forEach {
            val layout = layoutInflater.inflate(R.layout.rundown_item, rundownItemContainer, false)
            val rundownItem = layout.findViewById<MaterialCardView>(R.id.rundownItem)

            val dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")

            val showTime = LocalDateTime.parse(it.startTime, dateFormat)
            val endTime = LocalDateTime.parse(it.endTime, dateFormat)


            val displayedShowTime = "Dimulai ${DisplayUtil.getDisplayedFormatTime(showTime.hour)}:${DisplayUtil.getDisplayedFormatTime(showTime.minute)}"
            val displayedEndTime = "Selesai ${DisplayUtil.getDisplayedFormatTime(endTime.hour)}:${DisplayUtil.getDisplayedFormatTime(endTime.minute)}"

            rundownItem.findViewById<TextView>(R.id.rundownItemTitle).text = it.title
            rundownItem.findViewById<TextView>(R.id.rundownItemSubtitle).text = it.subtitle
            rundownItem.findViewById<TextView>(R.id.rundownItemStartTime).text = displayedShowTime
            rundownItem.findViewById<TextView>(R.id.rundownItemEndTime).text = displayedEndTime

            rundownItem.id = it.id!!.toInt()

            rundownItem.setOnClickListener(View.OnClickListener {
                clickHandlerOnRundown(it)
            })

            rundownItemContainer.addView(rundownItem)
        }
    }

    fun showDatePicker(showTimeEditText: EditText, organizerId: Int) {
        val cldr = Calendar.getInstance()
        val day = cldr.get(Calendar.DAY_OF_MONTH)
        val month = cldr.get(Calendar.MONDAY)
        val year = cldr.get(Calendar.YEAR)

        val picker = DatePickerDialog(this,
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                val displayedDate = dayOfMonth.toString() + " " + DateFormatSymbols().months[monthOfYear] + " " + year
                showTimeEditText.setText(displayedDate)

                val rundownItemContainer = findViewById<LinearLayout>(R.id.rundownItemContainer)
                val rundownListTitle = findViewById<TextView>(R.id.rundownListTitle)

                rundownItemContainer.removeAllViews()
                rundownListTitle.setText("Rundown For $displayedDate")

                searchRundown(DisplayUtil.getDisplayedFormatTime(dayOfMonth), DisplayUtil.getDisplayedFormatTime(monthOfYear + 1), year.toString(), organizerId)
            }, year, month, day
        )
        picker.show()
    }

    fun clickHandlerOnRundown(view: View) {
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
