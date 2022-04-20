package site.siredvin.lib.util

import java.util.function.BiFunction
import java.util.function.Function

class Pair<T, V>(t: T, v: V) {
    private val _left: T
    private val _right: V

    init {
        _left = t
        _right = v
    }

    val left: T
        get() = _left

    val right: V
        get() = _right

    fun leftPresent(): Boolean {
        return _left != null
    }

    fun rightPresent(): Boolean {
        return _right != null
    }

    fun <T1> mapLeft(mapFunc: Function<T?, T1>): Pair<T1, V?> {
        return Pair(mapFunc.apply(_left), _right)
    }

    fun <V1> mapRight(mapFunc: Function<V?, V1>): Pair<T?, V1> {
        return Pair(_left, mapFunc.apply(_right))
    }

    fun <T1, V1> mapBoth(mapFunc: BiFunction<T?, V?, Pair<T1, V1>?>): Pair<T1, V1>? {
        return mapFunc.apply(_left, _right)
    }

    fun <T1> ignoreLeft(): Pair<T1?, V?> {
        return Pair(null, _right)
    }

    fun <V1> ignoreRight(): Pair<T?, V1?> {
        return Pair(_left, null)
    }

    fun <R> reduce(reduceFunc: BiFunction<T?, V?, R>): R {
        return reduceFunc.apply(_left, _right)
    }

    companion object {
        fun <T, V> onlyRight(v: V): Pair<T?, V> {
            return Pair(null, v)
        }

        fun <T, V> onlyLeft(t: T): Pair<T, V?> {
            return Pair(t, null)
        }

        fun <T, V> of(t: T, v: V): Pair<T, V> {
            return Pair(t, v)
        }
    }
}