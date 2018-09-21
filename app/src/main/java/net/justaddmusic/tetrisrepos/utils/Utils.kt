package net.justaddmusic.tetrisrepos.utils

object Utils {

    fun humanReadableByteCount(bytes: Long): String {
        val unit = 1000
        if (bytes < unit) return bytes.toString() + " B"
        val exp = (Math.log(bytes.toDouble()) / Math.log(unit.toDouble())).toInt()
        val pre = ("kMGTPE")[exp - 1]
        return String.format("%.1f %sB", bytes / Math.pow(unit.toDouble(), exp.toDouble()), pre)
    }

}