package com.example.asteroidradar.main

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.asteroidradar.R
import com.example.asteroidradar.databinding.AsteroidItemBinding
import com.example.asteroidradar.Asteroid

class AsteroidAdapter(private val context: Context, private val listener: (Asteroid) -> Unit) :
    RecyclerView.Adapter<AsteroidAdapter.ViewHolder>() {

    private var asteroidList = listOf<Asteroid>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val itemBinding: AsteroidItemBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(context),
                R.layout.asteroid_item,
                parent,
                false
            )

        return ViewHolder(itemBinding)
    }

    override fun getItemCount(): Int = asteroidList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val asteroid = asteroidList[position]
        holder.itemBinding.asteroid = asteroid
        holder.itemView.setOnClickListener { listener(asteroid) }
    }

    fun setAsteroidList(newList: List<Asteroid>) {
        asteroidList = newList
        notifyDataSetChanged()
    }

    class ViewHolder(view: AsteroidItemBinding) : RecyclerView.ViewHolder(view.root) {
        val itemBinding: AsteroidItemBinding = view
    }
}