package site.siredvin.lib.util

import kotlin.math.pow
import kotlin.math.sqrt


object XPUtil {
    private const val REQUIRED_FOR_16 = 352;
    private const val REQUIRED_FOR_31 = 1507

    fun xpToLevels(xp: Double): Int {
        if (xp > REQUIRED_FOR_31)
            return (18.056 + sqrt(0.222 * (xp - 752.986))).toInt()
        if (xp > REQUIRED_FOR_16)
            return (8.1 + sqrt(0.4 * (xp - 195.975))).toInt()
        return (sqrt(xp + 9) - 3).toInt()
    }

    fun levelsToXP(level: Int): Double {
        if (level > 31)
            return 4.5 * level.toDouble().pow(2.0) - 162.5 * level + 2220
        if (level > 16)
            return 2.5 * level.toDouble().pow(2.0) - 40.5 * level + 360
        return level.toDouble().pow(2.0) + 6 * level
    }

    fun levelReductionToXp(xp: Double, amount: Int): Double {
        return xp - levelsToXP(xpToLevels(xp) - amount)
    }

    fun subtractLevels(xp: Double, amount: Int): Double {
        return levelsToXP(xpToLevels(xp) - amount)
    }
}