package com.iii.purrpulse.ui.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.iii.purrpulse.R
import com.iii.purrpulse.databinding.FragmentHomeBinding
import com.iii.purrpulse.gdx_stuff.use_highp
import com.iii.purrpulse.ui.gamelist.GamelistFragment

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private var homeViewModel : HomeViewModel? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Read preferences.
        val sharedPreferences = requireActivity().getPreferences(Context.MODE_PRIVATE)
        use_highp = sharedPreferences.getBoolean("isHighQualityOn", false)

        /*val textView: TextView = binding.textHome
        homeViewModel!!.text.observe(viewLifecycleOwner) {
            textView.text = it
        }*/
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.findViewById<Button>(R.id.easy_games).setOnClickListener {
            GamelistFragment.setEasyGames()
            view.findNavController().navigate(R.id.action_home_to_gamelist)
        }

        view.findViewById<Button>(R.id.mid_games).setOnClickListener {
            GamelistFragment.setMidGames()
            view.findNavController().navigate(R.id.action_home_to_gamelist)
        }

        view.findViewById<Button>(R.id.hard_games).setOnClickListener {
            GamelistFragment.setHardGames()
            view.findNavController().navigate(R.id.action_home_to_gamelist)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}