package com.example.bowwow

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.ActionBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : FragmentChangeListener, AppCompatActivity() {
    private lateinit var mainContentFragmentView: FragmentContainerView
    private lateinit var bottomNavBar: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mainContentFragmentView = findViewById(R.id.fcv1)
        bottomNavBar = findViewById(R.id.bnv1)

        loadFragment(DogProfileListFragment())
        bottomNavBar.selectedItemId = R.id.itHome

        bottomNavBar.setOnItemSelectedListener { it ->
            when (it.itemId) {
                bottomNavBar.selectedItemId -> {
                    false
                }
                R.id.itHome -> {
                    loadFragment(DogProfileListFragment())
                    true
                }

                R.id.itFavorites -> {
                    loadFragment(FavoritesFragment())
                    true
                }
                else -> {
                    false
                }
            }
        }
    }
    override fun loadFragment(fragment: Fragment, direction: FragmentChangeListener.Direction) {
        val transaction = supportFragmentManager.beginTransaction()
        when (direction) {
            FragmentChangeListener.Direction.LEFT -> {
                transaction.setCustomAnimations(R.anim.exit_to_right, R.anim.enter_from_left, R.anim.exit_to_left, R.anim.enter_from_right)
            }
            FragmentChangeListener.Direction.RIGHT -> {
                transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
            }
            else -> {
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            }
        }
        transaction.replace(R.id.fcv1,fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
    override fun back() {
        supportFragmentManager.popBackStack()
    }

}