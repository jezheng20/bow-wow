package com.example.bowwow

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.android.volley.Request
import org.json.JSONArray
import org.json.JSONObject

class DogProfileListFragment : Fragment() {

    private lateinit var dogAdapter: DogProfileRecyclerViewAdapter
    private lateinit var rvDogBreedList: RecyclerView
    private val BREEDS_LIST_URL = "https://dog.ceo/api/breeds/list/all"
    private lateinit var queue: RequestQueue
    private lateinit var dogListJson: JSONObject
    private val PREFS_NAME: String = "favorite_dogs"
    private lateinit var svDogSearch: SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dogAdapter = DogProfileRecyclerViewAdapter(mutableListOf(),
            activity as FragmentChangeListener,
            (activity as Context).getSharedPreferences(PREFS_NAME, 0)
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_dog_profile_list, container, false)
        queue = Volley.newRequestQueue(view.context)
        rvDogBreedList = view.findViewById(R.id.rvFavDogList)
        rvDogBreedList.adapter = dogAdapter
        rvDogBreedList.layoutManager = LinearLayoutManager(this.context)
        rvDogBreedList.setItemViewCacheSize(500)
        svDogSearch = view.findViewById(R.id.svDogSearch)

        if(dogAdapter.itemCount == 0) {
            refreshList()
        }
        svDogSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query.isNullOrBlank()) { dogAdapter.unfilter() }
                else { dogAdapter.filter(query) }
                return true
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrBlank()) { dogAdapter.unfilter() }
                return false
            }
        })

        return view
    }

    private fun String.capitalize() = replaceFirstChar(Char::titlecase)

    private fun generateDog(breed: String, subBreed: String=""): DogProfile {
        var breedLabel: String
        var breedId: String
        if (subBreed == "") {
            breedLabel = "${breed.capitalize()}"
            breedId = "${breed}"
        } else {
            breedLabel = "${subBreed.capitalize()} ${breed.capitalize()}"
            breedId = "$breed/$subBreed"
        }
        //TODO: Check within files if the dog is in favorites
        var imgURL = ""
        return DogProfile(breedLabel, breedId, imgURL = imgURL, isFavorite = false, isVisible = true)
    }

    private fun refreshList() {
        //Make a GET request to the dog API
        val req = JsonObjectRequest(Request.Method.GET, BREEDS_LIST_URL, null,
            {res ->
                dogListJson = res.getJSONObject("message")
                for(breed in dogListJson.keys()) {
                    var subBreeds: JSONArray = dogListJson.getJSONArray(breed)
                    // TODO: some values in the dog API are backwards, e.g. terrier australian instead of australian terrier.
                    if (subBreeds.length() == 0) dogAdapter.addDogProfile(generateDog(breed))
                    for(i in 0 until subBreeds.length()) {
                        dogAdapter.addDogProfile(generateDog(breed, subBreeds[i].toString()))
                    }
                }
                dogAdapter.setProperImgURLs(queue)
                println("Finished populating list. ")
            },
            {res ->
                println(res.toString())
            }
        )
        queue.add(req)
    }


}