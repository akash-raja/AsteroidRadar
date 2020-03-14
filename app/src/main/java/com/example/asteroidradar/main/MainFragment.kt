package com.example.asteroidradar.main

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.asteroidradar.R
import com.example.asteroidradar.databinding.FragmentMainBinding
import kotlinx.android.synthetic.main.fragment_main.*

class MainFragment : Fragment() {

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        asteroid_recycler.visibility = View.GONE
        status_loading_wheel.visibility = View.VISIBLE

        getWeeksAsteroidList()

        val asteroidAdapter = AsteroidAdapter(context!!) {
            val actionShowDetail = MainFragmentDirections.actionShowDetail(it)
            findNavController().navigate(actionShowDetail)
        }

        asteroid_recycler.adapter = asteroidAdapter
        viewModel.asteroidListLiveData.observe(viewLifecycleOwner, Observer {
            if (it.isNotEmpty()) {
                asteroidAdapter.setAsteroidList(it)
                asteroid_recycler.visibility = View.VISIBLE
                status_loading_wheel.visibility = View.GONE
            }
        })
    }

    private fun getWeeksAsteroidList() {
        viewModel.loadWeeksAsteroidList().observe(viewLifecycleOwner, Observer {
            viewModel.loadAsteroidList(it)
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.show_all_asteroid -> {
                viewModel.loadAllAsteroidList().observe(
                    viewLifecycleOwner,
                    Observer { viewModel.loadAsteroidList(it) })
                true
            }

            R.id.show_today_asteroid -> {
                viewModel.loadTodayAsteroidList().observe(
                    viewLifecycleOwner,
                    Observer { viewModel.loadAsteroidList(it) })
                true
            }
            R.id.show_week_asteroid -> {
                getWeeksAsteroidList()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
