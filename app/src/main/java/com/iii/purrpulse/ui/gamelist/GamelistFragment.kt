package com.iii.purrpulse.ui.gamelist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.iii.purrpulse.databinding.FragmentGamelistBinding


class GamelistFragment : Fragment() {

    private var _binding: FragmentGamelistBinding? = null

    companion object {
        private var title : String = ""

        private var gamesList : Array<String> = arrayOf()

        fun setEasyGames() {
            title = "Mindless games"
            gamesList = arrayOf("Mindless game 1", "Mindless game 2")
        }

        fun setMidGames() {
            title = "Semi mindful games"
            gamesList = arrayOf("Mid game 1", "Mid game 2")
        }

        fun setHardGames() {
            title = "Mindful games"
            gamesList = arrayOf("Mindful game 1", "Mindful game 2", "Mindful game 3")
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

        val actionBar = (activity as? AppCompatActivity)?.supportActionBar
        actionBar?.setTitle(title)

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