package de.ronnyfriedland.model

/** Single training record containing date, weight, completed repetitions, and rounds. */
data class Entry(
    val date: String,
    val weight: Double,
    val reps: Int,
    val rounds: Int = 1
)