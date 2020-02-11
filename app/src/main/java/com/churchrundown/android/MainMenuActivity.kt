package com.churchrundown.android

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.location.LocationServices
import entity.Organizer
import kotlinx.android.synthetic.main.activity_main_menu.*
import org.json.JSONArray
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import services.OrganizerService
import com.google.android.gms.location.FusedLocationProviderClient
import androidx.core.app.ActivityCompat
import android.content.pm.PackageManager
import android.location.Geocoder
import androidx.core.content.ContextCompat
import java.util.*

class MainMenuActivity: AppCompatActivity() {
    private var PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1
    private var mLocationPermissionGranted: Boolean = false
    private var mFusedLocationProviderClient: FusedLocationProviderClient? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        RetrofitMain.build()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)

        // Construct a FusedLocationProviderClient.
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        getLocationPermission()

        val gpsTracker = GPSTracker(this)
        val latitude = gpsTracker.latitude
        val longitude = gpsTracker.longitude

        val geocoder = Geocoder(this, Locale.getDefault())
        val addresses = geocoder.getFromLocation(latitude, longitude, 1)

        val province = addresses[0].adminArea

        getOrganizer(province)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_scrolling, menu)
        return true
    }

    private fun getLocationPermission() {
        if (ContextCompat.checkSelfPermission(
                this.applicationContext,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mLocationPermissionGranted = true
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION
            )
        }
    }

    fun getOrganizer(province: String) {
        var retrofit = RetrofitMain.retrofit

        val service =
            retrofit!!.create(OrganizerService::class.java)

        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        progressBar.visibility = View.VISIBLE

        val call = service.getOrganizerByProvince(province)

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

                val intent = Intent(applicationContext, ListChurch::class.java)
                val organizers = response.body()

                val listOfOrganizer = JSONArray()
                if(organizers != null && organizers.isNotEmpty()) {
                    organizers!!.forEach {
                        listOfOrganizer.put(it.toJSON())
                    }
                }

                intent.putExtra("organizers", listOfOrganizer.toString())
                intent.putExtra("province", province)
                intent.putExtra("previousActivity", "MainMenu")
                startActivity(intent)
            }
        })
    }
}