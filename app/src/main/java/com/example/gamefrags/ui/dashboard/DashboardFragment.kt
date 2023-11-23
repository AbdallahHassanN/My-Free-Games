package com.example.gamefrags.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gamefrags.Adapter.GameAdapter
import com.example.gamefrags.R
import com.example.gamefrags.Repository.GameRepo
import com.example.gamefrags.Response.Resource
import com.example.gamefrags.RoomDB.GameDatabase
import com.example.gamefrags.ViewModelProviders.ViewModelProviderFactory
import com.example.gamefrags.databinding.FragmentDashboardBinding

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    lateinit var dashboardViewModel:DashboardViewModel
    private lateinit var gameAdapter: GameAdapter
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val repository = GameRepo(GameDatabase(requireActivity()))
        val viewModelProviderFactory = ViewModelProviderFactory(requireActivity().application,repository)
        dashboardViewModel = ViewModelProvider(this,viewModelProviderFactory).get(DashboardViewModel::class.java)

        setUpRecyclerView()

        gameAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("game",it)
            }
            findNavController().navigate(
                R.id.action_navigation_dashboard_to_gameWebView,
                bundle
            )
        }

        observeGames()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun observeGames(){
        dashboardViewModel.allGames.observe(viewLifecycleOwner, Observer { response ->
            when(response){
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let {
                        gameAdapter.differ.submitList(it)
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let{ message ->
                        Toast.makeText(activity,"An error occurred: $message",Toast.LENGTH_SHORT).show()
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })
    }
    private fun hideProgressBar() {
        binding.paginationProgressBar.visibility = View.INVISIBLE
    }
    private fun showProgressBar() {
        binding.paginationProgressBar.visibility = View.VISIBLE
    }
    private fun setUpRecyclerView(){
        gameAdapter = GameAdapter()
        binding.rvGames.apply {
            adapter = gameAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }
}