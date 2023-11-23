package com.example.gamefrags.ui.home

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gamefrags.Adapter.GameAdapter
import com.example.gamefrags.R
import com.example.gamefrags.Repository.GameRepo
import com.example.gamefrags.RoomDB.GameDatabase
import com.example.gamefrags.ViewModelProviders.HomeViewModelProviderFactory
import com.example.gamefrags.ViewModelProviders.ViewModelProviderFactory
import com.example.gamefrags.databinding.FragmentHomeBinding
import com.google.android.material.snackbar.Snackbar

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    lateinit var homeViewModel:HomeViewModel
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var gameAdapter: GameAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val repository = GameRepo(GameDatabase(requireActivity()))
        val viewModelProviderFactory = HomeViewModelProviderFactory(repository)
        homeViewModel = ViewModelProvider(this,viewModelProviderFactory).get(HomeViewModel::class.java)

        setUpRecyclerView()
        homeViewModel.getSavedGames().observe(viewLifecycleOwner, Observer {
            gameAdapter.differ.submitList(it)
        })

        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val article = gameAdapter.differ.currentList[position]
                homeViewModel.deleteGame(article)
                Snackbar.make(view, "Game deleted ", Snackbar.LENGTH_SHORT).apply {
                    setAction("Undo") {
                        homeViewModel.saveGame(article)
                    }.setBackgroundTint(Color.RED)
                    show()
                }
            }
        }

        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(binding.rvGames)
        }

        gameAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("game",it)
            }
            findNavController().navigate(
                R.id.action_navigation_home_to_gameWebView,
                bundle
            )
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    private fun setUpRecyclerView(){
        gameAdapter = GameAdapter()
        binding.rvGames.apply {
            adapter = gameAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }
}