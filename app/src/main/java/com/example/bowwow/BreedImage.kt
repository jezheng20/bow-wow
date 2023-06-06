package com.example.bowwow

/*
 * Data class representing a singular image
 */

data class BreedImage(
    var imgURL: String,
    var loaded: Boolean = false
)