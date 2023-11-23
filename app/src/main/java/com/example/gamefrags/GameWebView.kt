package com.example.gamefrags

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.example.gamefrags.Repository.GameRepo
import com.example.gamefrags.RoomDB.GameDatabase
import com.example.gamefrags.ViewModelProviders.ViewModelProviderFactory
import com.example.gamefrags.databinding.FragmentGameWebViewBinding
import com.example.gamefrags.ui.dashboard.DashboardViewModel
import com.google.android.material.snackbar.Snackbar


class GameWebView : Fragment() {

    lateinit var dashboardViewModel:DashboardViewModel
    val args: GameWebViewArgs by navArgs()
    private var _binding: FragmentGameWebViewBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGameWebViewBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val repository = GameRepo(GameDatabase(requireActivity()))
        val viewModelProviderFactory = ViewModelProviderFactory(requireActivity().application,repository)
        dashboardViewModel = ViewModelProvider(this,viewModelProviderFactory).get(DashboardViewModel::class.java)

        val game = args.game
        binding.webView.apply {
            webViewClient = WebViewClient()
            loadUrl(game.game_url)
        }

        binding.fab.setOnClickListener {
            dashboardViewModel.saveGame(game)
            Snackbar.make(view,"Game Saved",Snackbar.LENGTH_SHORT).setBackgroundTint(Color.GREEN).show()
        }
    }


}