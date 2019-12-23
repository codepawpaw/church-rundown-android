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

    @SerializedName("Description")
    @Expose
    var description: String

    constructor(name: String, description: String) {
        this.name = name
        this.description = description
    }

    fun toJSON(): JsonObject {
        val json = JsonObject()
        json.addProperty("id", id)
        json.addProperty("name", name)
        json.addProperty("description", description)

        return json
    }
}