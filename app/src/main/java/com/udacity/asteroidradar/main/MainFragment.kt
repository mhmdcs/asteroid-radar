package com.udacity.asteroidradar.main

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHost
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.FragmentMainBinding
import com.udacity.asteroidradar.utils.SortFilter

class MainFragment : Fragment() {

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        binding.asteroidRecycler.adapter = AsteroidsListAdapter(AsteroidClickListener {
                 viewModel.asteroidClicked(it)
        })


        viewModel.navigateToDetailsFragment.observe(viewLifecycleOwner, Observer{
            it?.let {
                val action = MainFragmentDirections.actionShowDetail(it)
                NavHostFragment.findNavController(this).navigate(action) //or replace this line with extension function version this.findNavController().navigate(action)
                viewModel.doneNavigation()
            }
            Log.i("LogMainTest","Just testing!")
        })

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.show_cached -> viewModel.onChangeSortFilter(SortFilter.CACHED)
            R.id.show_weekly -> viewModel.onChangeSortFilter(SortFilter.WEEKLY)
            R.id.show_daily -> viewModel.onChangeSortFilter(SortFilter.TODAY)
        }
        return true
    }
}
