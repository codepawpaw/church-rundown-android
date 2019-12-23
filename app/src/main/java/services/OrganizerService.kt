package services

import entity.Organizer
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface OrganizerService {
    @GET("organizer/{name}")
    fun getOrganizer(@Path("name") name: String): Call<List<Organizer>>
}