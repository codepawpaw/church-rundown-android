package entity

import com.google.gson.JsonObject
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Organizer {
    @SerializedName("ID")
    @Expose
    var id: String? = null

    @SerializedName("Name")
    @Expose
    var name: String

    @SerializedName("displayName")
    @Expose
    var displayName: String

    @SerializedName("Description")
    @Expose
    var description: String

    @SerializedName("locationAddress")
    @Expose
    var locationAddress: String

    constructor(name: String, displayName: String, description: String, locationAddress: String) {
        this.name = name
        this.displayName = displayName
        this.description = description
        this.locationAddress = locationAddress
    }

    fun toJSON(): JsonObject {
        val json = JsonObject()
        json.addProperty("id", id)
        json.addProperty("name", name)
        json.addProperty("displayName", displayName)
        json.addProperty("description", description)
        json.addProperty("locationAddress", locationAddress)

        return json
    }
}