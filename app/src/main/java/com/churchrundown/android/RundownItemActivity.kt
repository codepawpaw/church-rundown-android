package com.churchrundown.android

import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import com.google.android.material.card.MaterialCardView
import com.google.gson.GsonBuilder
import entity.Rundown
import entity.RundownItem
import kotlinx.android.synthetic.main.activity_rundown_item.*
import java.io.StringReader
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class RundownItemActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rundown_item)

        val intent = getIntent()
        val rundownItems = intent.getStringExtra("rundownItems")
        val rundownItemContainer = findViewById<LinearLayout>(R.id.rundownItemContainer)

        val gsonBuilder = GsonBuilder().serializeNulls()
        val gson = gsonBuilder.create()
        val jsonArrayOfRundownItems: List<RundownItem> = gson.fromJson(StringReader(rundownItems), Array<RundownItem>::class.java).toList()

        if(jsonArrayOfRundownItems.isEmpty()) {
            val rundownItemTitle = findViewById<TextView>(R.id.rundownItemTitle)
            val rundownItemDescription = findViewById<TextView>(R.id.rundownItemDescription)

            rundownItemTitle.visibility = View.VISIBLE
            rundownItemDescription.visibility = View.VISIBLE

            rundownItemTitle.text = "No data available"
            rundownItemDescription.text = "There are no data available. Maybe choose other rundown to see more results."
        }

        jsonArrayOfRundownItems.forEach {
            val layout = layoutInflater.inflate(R.layout.rundown_item_detail, rundownItemContainer, false)
            val rundownItemDetail = layout.findViewById<MaterialCardView>(R.id.rundownItemDetail)

            rundownItemDetail.findViewById<TextView>(R.id.rundownItemDetailTitle).text = it.title
            rundownItemDetail.findViewById<TextView>(R.id.rundownItemDetailSubtitle).text = it.subtitle
            rundownItemDetail.findViewById<TextView>(R.id.rundownItemDetailContent).text = HtmlCompat.fromHtml(it.text, HtmlCompat.FROM_HTML_MODE_LEGACY)

            rundownItemContainer.addView(rundownItemDetail)
        }
    }
}
