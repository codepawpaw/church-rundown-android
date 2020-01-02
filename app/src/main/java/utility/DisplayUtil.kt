package utility

class DisplayUtil {
    companion object {
        fun getDisplayedFormatTime(time: Int): String {
            return if (time < 10) "0${time}" else time.toString()
        }
    }
}