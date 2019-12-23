package entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import org.json.JSONArray

class Data {
    @SerializedName("data")
    @Expose
    var data: String

    @SerializedName("errorMessage")
    @Expose
    var errorMessage: String

    @SerializedName("status")
    @Expose
    var status: String

    constructor(data: String, errorMessage: String, status: String) {
        this.data = data
        this.errorMessage = errorMessage
        this.status = status
    }
}