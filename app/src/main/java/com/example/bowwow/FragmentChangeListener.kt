package com.example.bowwow

import androidx.fragment.app.Fragment

/*
 * Simple fragment change listener
 */

interface FragmentChangeListener {
    /*
     * Load the specified fragment
     */
    public fun loadFragment(fragment: Fragment, direction: Direction = Direction.FADE)

    /*
     * Pop the last fragment from backstack
     */
    public fun back()

    enum class Direction {
        LEFT,
        RIGHT,
        FADE
    }

}