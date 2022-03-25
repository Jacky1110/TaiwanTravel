package com.jotangi.nickyen

import android.text.format.DateFormat

/**
 * Created by N!ck Yen on Date: 2022/1/2
 */
object KTUtils {
    /**
     * @param time
     * @return yy-MM-dd HH:mm格式时间
     */
    open fun conversionTime(time: Long, format: String = "yyyy-MM-dd HH:mm:ss"): String {
        return DateFormat.format(format, time).toString()
    }
}