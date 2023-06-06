package com.example.bowwow

/*
 * Data class representing the various properties of a dog profile, to be displayed as a card
 */

data class DogProfile (
    var breedLabel: String,
    var breedId: String,
    var imgURL: String = "",
    var isFavorite: Boolean,
    var isVisible: Boolean = true
)
