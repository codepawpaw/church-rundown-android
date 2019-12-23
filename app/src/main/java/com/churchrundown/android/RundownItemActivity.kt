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

        jsonArrayOfRundownItems.forEach {
            val layout = LayoutInflater.from(applicationContext).inflate(R.layout.rundown_item_detail, null)

            layout.findViewById<TextView>(R.id.rundownItemDetailTitle).text = it.title
            layout.findViewById<TextView>(R.id.rundownItemDetailSubtitle).text = it.subtitle
            layout.findViewById<TextView>(R.id.rundownItemDetailContent).text = HtmlCompat.fromHtml(it.text, HtmlCompat.FROM_HTML_MODE_LEGACY)

            rundownItemContainer.addView(layout)
        }
    }
}
