package com.example.bowwow

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONObject

class FavoritesFragment : Fragment() {

    private lateinit var dogAdapter: DogProfileRecyclerViewAdapter
    private lateinit var rvFavDogList: RecyclerView
    private lateinit var queue: RequestQueue
    private lateinit var dogListJson: JSONObject
    private val PREFS_NAME: String = "favorite_dogs"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    /*
     * On view creation, we will always re-refresh the favorites revyvlerview
     * and replace with a message if empty.
     */

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_favorites, container, false)

        dogAdapter = DogProfileRecyclerViewAdapter(mutableListOf(),
            activity as FragmentChangeListener,
            (activity as Context).getSharedPreferences(PREFS_NAME, 0)
        )
        queue = Volley.newRequestQueue(view.context)
        rvFavDogList = view.findViewById(R.id.rvFavDogList)
        rvFavDogList.adapter = dogAdapter
        rvFavDogList.layoutManager = LinearLayoutManager(this.context)
        rvFavDogList.setItemViewCacheSize(900)

        refreshList()
        if (dogAdapter.itemCount == 0) {
            view.findViewById<TextView>(R.id.tvBlankFavoritesMsg).visibility = View.VISIBLE
        } else {
            view.findViewById<TextView>(R.id.tvBlankFavoritesMsg).visibility = View.INVISIBLE
        }

        return view
    }

    private fun String.capitalize() = replaceFirstChar(Char::titlecase)

    /*
     * Helper function to convert internal ID to the dog's name
     * @param breedId: the id to be converted
     */
    private fun dogLabelFromId(breedId: String): String {
        var names: List<String> = breedId.split('/')
        if (names.size > 1 && names[1].isNotEmpty()) return "${names[1].capitalize()} ${names[0].capitalize()}"
        return names[0].capitalize()
    }

    /*
     * Called every time the view is reloaded.
     * Populates the adapter with dogs marked as favorite (listed in SharedPreferences)
     */
    private fun refreshList() {
        //Check SharedPreferences
        var prefs = (activity as Context).getSharedPreferences(PREFS_NAME, 0)
        for (i in prefs.all.keys) {
            if (prefs.getBoolean(i, false)) {
                dogAdapter.addDogProfile(
                    DogProfile(breedLabel = dogLabelFromId(i), breedId = i, imgURL = "", isFavorite = true)
                )
            }
        }
        dogAdapter.setProperImgURLs(queue)
        println("Finished populating list. ")
    }

}