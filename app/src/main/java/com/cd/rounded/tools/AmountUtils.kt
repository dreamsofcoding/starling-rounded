package com.cd.rounded.tools

import java.math.BigDecimal
import kotlin.math.ceil

class AmountUtils {
    companion object {
        /**
         * Converts Minor Units recieved from Server into a Big Decimal,
         * with the dp in the correct place
         * @param minorUnits Int, minor units to be converted
         */
        fun convertMinorUnitsToDecimal(minorUnits : Int): BigDecimal {
            return minorUnits.toBigDecimal().movePointLeft(2)
        }

        /**
         * Converts a Minor Big Decimal into a Double
         * @param minorDecimal BigDecimal, minor units to be converted
         */
        fun convertMinorDecimalToDouble(minorDecimal :BigDecimal): Double {
            return minorDecimal.toDouble()
        }

        /**
         * Converts minor units to a rounded amount, so that we can calculate
         * the difference between the minor units and the rounded up amount.
         * @param minorDouble Double, minor units as a double to be round up
         */
        fun convertMinorUnitsToRoundedAmount(minorDouble: Double): Double {
            return ceil(minorDouble)
        }

        /**
         * Converts an amount to minor units for sending to the server
         * @param amount Double, amount to be expressed as minor units
         */
        fun convertDoubleToMinorDecimal(amount :Double): BigDecimal? {
            return amount.toBigDecimal().movePointRight(2)
        }
    }
}