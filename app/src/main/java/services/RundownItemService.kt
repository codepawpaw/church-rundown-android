package services

import entity.Data
import entity.Rundown
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface RundownItemService {
    @GET("rundown_item/{rundownId}")
    fun getByRundownId(@Path("rundownId") rundownId: Int): Call<Data>

}