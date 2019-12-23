package entity

import com.google.gson.JsonObject
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Rundown {
    @SerializedName("ID")
    @Expose
    var id: String? = null

    @SerializedName("Title")
    @Expose
    var title: String

    @SerializedName("Subtitle")
    @Expose
    var subtitle: String

    @SerializedName("showTime")
    @Expose
    var startTime: String

    @SerializedName("endTime")
    @Expose
    var endTime: String

    constructor(title: String, subtitle: String, startTime: String, endTime: String) {
        this.title = title
        this.subtitle = subtitle
        this.startTime = startTime
        this.endTime = endTime
    }

    fun toJSON(): JsonObject {
        val json = JsonObject()
        json.addProperty("id", id)
        json.addProperty("title", title)
        json.addProperty("subtitle", subtitle)
        json.addProperty("startTime", startTime)
        json.addProperty("endTime", endTime)

        return json
    }
}