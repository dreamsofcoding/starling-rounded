package com.cd.rounded.tools

import java.text.SimpleDateFormat
import java.time.OffsetDateTime
import java.time.temporal.ChronoUnit
import java.util.Date

class DateUtils {
    companion object {
        /**
         * Function to get the min and max timestamps needed to query any given week period,
         * returns MinMaxTime Object
         * @param week Int, week expressed as 0 = current, 1 = last week, 2 = 2 weeks ago, etc
         */
        fun getMinMaxTimesFromWeek(week : Int = 0 ): MinMaxTime {
            val currentDate = OffsetDateTime.now()
            val maxDate = currentDate.minusWeeks(week.toLong())
            val minDate = maxDate.minusWeeks(1)
            return MinMaxTime(
                minDate,
                maxDate
            )
        }
    }
}

/**
 * MinMaxTime data class, mapping the output Min and Max offsetDateTimes
 */
data class MinMaxTime(val min: OffsetDateTime, val max: OffsetDateTime)