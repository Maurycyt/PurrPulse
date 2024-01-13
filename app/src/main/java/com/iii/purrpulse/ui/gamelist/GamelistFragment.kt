package com.iii.purrpulse.ui.gamelist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.iii.purrpulse.databinding.FragmentGamelistBinding


class GamelistFragment : Fragment() {

    private var _binding: FragmentGamelistBinding? = null

    companion object {
        private var gamesList : Array<String> = arrayOf()

        fun setEasyGames() {
            gamesList = arrayOf("Easy game 1", "Easy game 2")
        }

        fun setHardGames() {
            gamesList = arrayOf("Hard game 1", "Hard game 2", "Hard game 3")
        }
    }

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentGamelistBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val ll = binding.gamesList

        for (game in gamesList) {
            val tv = Button(inflater.context)
            tv.height = 200
            tv.text = game
            ll.addView(tv)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}