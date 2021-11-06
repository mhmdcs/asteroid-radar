package com.udacity.asteroidradar.main

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

class AsteroidClickListener(val clickListener: (Asteroid) -> Unit) {
    fun onClick(asteroid: Asteroid) = clickListener(asteroid)
}


