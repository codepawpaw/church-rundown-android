package services

import entity.Organizer
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface OrganizerService {
    @GET("organizer/{name}")
    fun getOrganizer(@Path("name") name: String): Call<List<Organizer>>

    @GET("organizer/province/{name}")
    fun getOrganizerByProvince(@Path("name") name: String): Call<List<Organizer>>

    @GET("organizer/province/name/{province}/{name}")
    fun getOrganizerByProvinceAndName(@Path("province") province: String, @Path("name") name: String): Call<List<Organizer>>
}