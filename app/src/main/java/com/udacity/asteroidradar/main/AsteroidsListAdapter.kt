package com.udacity.asteroidradar.main

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.databinding.AsteroidItemBinding
import com.udacity.asteroidradar.domain.Asteroid

class AsteroidsListAdapter(private val clickListener: AsteroidClickListener):
    ListAdapter<Asteroid, AsteroidViewHolder>(DiffCallback){

    companion object DiffCallback: DiffUtil.ItemCallback<Asteroid>() {
        override fun areItemsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AsteroidViewHolder {
        return AsteroidViewHolder(AsteroidItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: AsteroidViewHolder, position: Int) {
        val asteroid = getItem(position)
        holder.bind(asteroid, clickListener)
    }
}


class AsteroidViewHolder(private val binding: AsteroidItemBinding): RecyclerView.ViewHolder(binding.root) {
    fun bind(asteroid: Asteroid, clickListener: AsteroidClickListener){
        binding.asteroid = asteroid
        binding.clickListener = clickListener
        binding.executePendingBindings()
    }
}

//Custom ClickListener that handles clicks on [RecyclerView] items.  Passes an Asteroid object
//associates the current item to the [onClick] function. ClickListener lambda that will be called with the current Asteroid object
class AsteroidClickListener(val clickListener: (Asteroid) -> Unit) {
    fun onClick(asteroid: Asteroid) = clickListener(asteroid)

}

//use fun interface for setting interfaces like this next time, read https://stackoverflow.com/questions/66541938/kotlin-custom-onclicklistener-interface

//code above can be read this way:

//    class AsteroidOnClickListener {
//        val clickListener: (asteroid: Asteroid) -> Unit //this is a "function type" variable which accepts parameter of Asteroid type
//
//        constructor(_clickListener: (Asteroid) -> Unit) {//constructor of class
//            clickListener = _clickListener
//        }
//
//        fun onClick(asteroid: Asteroid) {
//            clickListener(asteroid)//calling of clickListener with parameter of Asteroid
//        }
//    }

