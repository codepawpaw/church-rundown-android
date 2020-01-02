package services

import entity.Data
import entity.Rundown
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RundownService {
    @GET("rundown/{organizerId}")
    fun getByOrganizerId(@Path("organizerId") organizerId: Int, @Query("startDate") startDate: String, @Query("endDate") endDate: String): Call<Data>

    @GET("rundown/{organizerId}/{startDate}/{endDate}")
    fun getByOrganizerIdAndDate(@Path("organizerId") organizerId: Int, @Path("startDate") startDate: String, @Path("endDate") endDate: String): Call<List<Rundown>>
}