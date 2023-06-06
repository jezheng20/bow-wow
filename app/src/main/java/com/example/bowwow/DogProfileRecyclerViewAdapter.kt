package com.example.bowwow

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.squareup.picasso.Picasso

/**
 * [RecyclerView.Adapter] that can display a [PlaceholderItem].
 * TODO: Replace the implementation with code for your data type.
 */
class DogProfileRecyclerViewAdapter(
    private var dogProfiles: MutableList<DogProfile>,
    private var listener: FragmentChangeListener,
    private var prefs: SharedPreferences
) : RecyclerView.Adapter<DogProfileRecyclerViewAdapter.DogProfileViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DogProfileViewHolder {
        return DogProfileViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.dog_profile, parent, false)
        )
    }

    override fun onBindViewHolder(holder: DogProfileViewHolder, position: Int) {
        val cur = dogProfiles[position]
        holder.apply {
            //Check to see if the item is marked as favorite in preferences
            cur.isFavorite = prefs.getBoolean(cur.breedId, false)
            cbIsFavorite.isChecked = cur.isFavorite
            tvBreedName.text = cur.breedLabel
            imgURL = cur.imgURL
            cbIsFavorite.setOnCheckedChangeListener {_, _ ->
                cur.isFavorite = !cur.isFavorite
                prefs.edit().apply {
                    putBoolean(cur.breedId, cur.isFavorite)
                    apply()
                }
            }
            // Load the fragment representing the 15 breed images
            tvBreedName.setOnClickListener {
                var newFrag = BreedImageFragment()
                var args = Bundle().apply {
                    putString("breedLabel", cur.breedLabel)
                    putString("breedId", cur.breedId)
                }
                newFrag.arguments = args
                listener.loadFragment(newFrag, FragmentChangeListener.Direction.RIGHT)
            }
            if (!cur.isVisible) itemView.findViewById<CardView>(R.id.cvMain).visibility = View.GONE
            else itemView.findViewById<CardView>(R.id.cvMain).visibility = View.VISIBLE
            updateUI()
        }
    }
    override fun getItemCount(): Int = dogProfiles.size

    fun unfilter() {
        for (i: DogProfile in dogProfiles) i.isVisible = true
        notifyDataSetChanged()
    }

    fun filter(sequence: String) {
        for (i: DogProfile in dogProfiles) {
            if (!i.breedLabel.contains(sequence, ignoreCase = true)) i.isVisible = false
            else i.isVisible = true
        }
        notifyDataSetChanged()
    }

    fun setProperImgURLs(queue: RequestQueue) {
        //TODO: Migrate this to dog api class
        for (i in 0 until dogProfiles.size) {
            var dp = dogProfiles[i]
            var breedURL = "https://dog.ceo/api/breed/${dp.breedId}/images/random"
            val req = JsonObjectRequest(
                Request.Method.GET, breedURL, null,
                {res ->
                    dp.imgURL = res.getString("message")
                    //println("${dp.breedId} changed image to ${dp.imgURL}")
                    notifyItemChanged(i)
                },
                {res ->
                    println(res.toString())
                }
            )
            queue.add(req)
        }
    }

    fun addDogProfile(dog: DogProfile) {
        dogProfiles.add(dog)
        notifyItemInserted(dogProfiles.size-1)
    }

    class DogProfileViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var cbIsFavorite: CheckBox
        var tvBreedName: TextView
        lateinit var imgURL: String
        private var pbProfileImg: ProgressBar
        private var ivProfileImg: ImageView
        private var ctx: Context
        init {
            cbIsFavorite = itemView.findViewById(R.id.cbIsFavorite)
            tvBreedName = itemView.findViewById(R.id.tvBreedName)
            ivProfileImg = itemView.findViewById(R.id.ivProfileImg)
            pbProfileImg = itemView.findViewById(R.id.pbProfileImg)
            ctx = itemView.context
        }
        fun updateUI() {
            if (imgURL.isNotEmpty()) Picasso.with(ctx).load(imgURL).into(ivProfileImg)
        }
    }

}