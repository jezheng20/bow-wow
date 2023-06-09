package com.example.bowwow

import android.content.Context
import android.view.LayoutInflater
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.squareup.picasso.Picasso
import com.android.volley.Request

/**
 * RecyclerView.Adapter that displays a list of dog images.
 */
class BreedImageRecyclerViewAdapter(
    private val values: MutableList<BreedImage>
) : RecyclerView.Adapter<BreedImageRecyclerViewAdapter.BreedImageViewHolder>() {

    private val MAX_SIZE = 15

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BreedImageViewHolder {
        return BreedImageRecyclerViewAdapter.BreedImageViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.breed_image, parent, false)
        )
    }

    /*
     * Sets the viewholder's image URL for future use
     */
    override fun onBindViewHolder(holder: BreedImageViewHolder, position: Int) {
        var cur = values[position]
        holder.apply {
            imgURL = cur.imgURL
            updateUI()
        }
    }

    override fun getItemCount(): Int = values.size

    /*
     * Fetches MAX_SIZE images of dogs of a certain breed.
     * @param queue: the request queue in which to place GET calls
     * @param breedId: the dog breed/sub-breed to populate the view with
     */

    fun populate(queue: RequestQueue, breedId: String) {
        for(i in 0 until MAX_SIZE) {
            //TODO: account for the possibility s.t. there exists less than MAX_SIZE images
            var item = BreedImage(imgURL="")
            addItem(item)
            //Make an API request to get the link
            var url = "https://dog.ceo/api/breed/$breedId/images/random"
            var req = JsonObjectRequest(Request.Method.GET, url,null,
                { res ->
                    item.imgURL = res.getString("message")
                    notifyItemChanged(i)
                    item.loaded = true
                },
                { res ->
                    //TODO: Account for error response
                    println(res)
                })
            queue.add(req)
        }
    }

    /*
     * Adds an image to the list.
     *  @param BreedImage: the image of the specified dog breed
     */
    private fun addItem(item: BreedImage) {
        values.add(item)
        notifyItemInserted(values.size-1)
    }

    /*
     * Inner class representing the viewholder for each image
     */
    class BreedImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var ctx: Context
        var ivDisplayDog: ImageView
        lateinit var imgURL: String
        init {
            ctx = itemView.context
            ivDisplayDog = itemView.findViewById(R.id.ivDisplayDog)
        }
        fun updateUI() {
            if(imgURL.isNotEmpty()) {
                Picasso.with(ctx).load(imgURL).into(ivDisplayDog)
            }
        }
    }

}