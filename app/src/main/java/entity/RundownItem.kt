package entity

import com.google.gson.JsonObject
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RundownItem {
    @SerializedName("ID")
    @Expose
    var id: String? = null

    @SerializedName("Title")
    @Expose
    var title: String

    @SerializedName("Subtitle")
    @Expose
    var subtitle: String

    @SerializedName("Text")
    @Expose
    var text: String

    @SerializedName("rundownId")
    @Expose
    var rundownId: Int

    constructor(title: String, subtitle: String, text: String, rundownId: Int) {
        this.title = title
        this.subtitle = subtitle
        this.text = text
        this.rundownId = rundownId
    }

    fun toJSON(): JsonObject {
        val json = JsonObject()
        json.addProperty("id", id)
        json.addProperty("title", title)
        json.addProperty("subtitle", subtitle)
        json.addProperty("text", text)
        json.addProperty("rundownId", rundownId)

        return json
    }
}