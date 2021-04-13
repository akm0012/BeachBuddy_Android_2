package com.andrewkingmarshall.beachbuddy2.extensions

fun String.capitalizeWords(): String =
    split(" ").joinToString(" ") { it.toLowerCase().capitalize() }