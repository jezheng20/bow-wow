package com.example.bowwow

data class DogProfile (
    var breedLabel: String,
    var breedId: String,
    var imgURL: String = "",
    var isFavorite: Boolean,
    var isVisible: Boolean = true
)
