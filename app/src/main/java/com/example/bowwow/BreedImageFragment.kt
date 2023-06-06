package com.example.bowwow

import android.app.DownloadManager.Request
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.InputQueue
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.google.android.material.floatingactionbutton.FloatingActionButton

/**
 * This fragment fetches and displays the images of a specific dog breed.
 * breedId: the id of the dog breed
 */
class BreedImageFragment : Fragment() {
    private val columnCount = 3
    private lateinit var breedId: String
    private var breedLabel = ""
    private lateinit var rvDogBreedImages: RecyclerView
    private lateinit var breedAdapter: BreedImageRecyclerViewAdapter
    private lateinit var fabReturnList: FloatingActionButton
    private lateinit var queue: RequestQueue
    private val specialTags: Map<String, String> = hashMapOf(
        "whippet" to "#ffd9d9", "greyhound/italian" to "#aa06cf"
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_breed_image_list, container, false)

        var args: Bundle? = arguments
        if (args != null) {
            //TODO: Make this smoother
            var bid: String? = args.getString("breedId")
            var bl: String? = args.getString("breedLabel")
            if (bid != null) {breedId = bid } else {throw Exception("BreedImageFragment must take in breedId as an argument!!")}
            if (bl != null) {breedLabel = bl} else {throw Exception("BreedImageFragment must take in breedId as an argument!!")}
        } else { throw Exception("BreedImageFragment must take in breedId as an argument!!") }

        queue = Volley.newRequestQueue(view.context)
        rvDogBreedImages = view.findViewById(R.id.rvDogBreedImageList)
        breedAdapter = BreedImageRecyclerViewAdapter(mutableListOf())
        fabReturnList = view.findViewById(R.id.fabReturnList)

        rvDogBreedImages.apply {
            layoutManager = StaggeredGridLayoutManager(3, LinearLayoutManager.VERTICAL )
            adapter = breedAdapter
        }

        view.findViewById<TextView>(R.id.tvTitle).text = breedLabel

        if (specialTags.containsKey(breedId)) {
            var specialColorString = specialTags[breedId]
            rvDogBreedImages.setBackgroundColor(Color.parseColor(specialColorString))
        }

        fabReturnList.setOnClickListener {
            (activity as FragmentChangeListener).back()
        }

        breedAdapter.populate(queue, breedId)

        return view
    }

}