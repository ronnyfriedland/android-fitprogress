package de.ronnyfriedland.model

/** Single training record containing date, weight, and completed repetitions. */
data class Entry(
    val date: String,
    val weight: Double,
    val reps: Int
)