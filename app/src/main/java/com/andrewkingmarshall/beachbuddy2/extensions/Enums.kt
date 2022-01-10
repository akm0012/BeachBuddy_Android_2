package com.andrewkingmarshall.beachbuddy2.extensions

inline fun <reified T: Enum<T>> T.nextValue(): T {
    val values = enumValues<T>()
    val nextOrdinal = (ordinal + 1) % values.size
    return values[nextOrdinal]
}