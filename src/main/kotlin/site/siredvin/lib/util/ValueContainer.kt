package site.siredvin.lib.util

import java.util.*
import java.util.function.Function

class ValueContainer<T>(var value: T) {

    val notNull: T
        get() = Objects.requireNonNull(value)
    val isEmpty: Boolean
        get() = value == null
    val isPresent: Boolean
        get() = !isEmpty

    fun mutate(mutateFunc: Function<T, T>) {
        value = mutateFunc.apply(value)
    }

    companion object {
        fun <T> of(t: T): ValueContainer<T> {
            return ValueContainer(t)
        }
    }
}