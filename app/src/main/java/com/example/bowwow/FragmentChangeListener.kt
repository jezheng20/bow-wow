package com.example.bowwow

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction

interface FragmentChangeListener {
    public fun loadFragment(fragment: Fragment, direction: Direction = Direction.FADE)
    public fun back()

    enum class Direction {
        LEFT,
        RIGHT,
        FADE
    }

}