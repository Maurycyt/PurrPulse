package com.iii.purrpulse.ui.gamelist

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.iii.purrpulse.Launcher
import com.iii.purrpulse.launcher_mode
import com.iii.purrpulse.LauncherMode
import com.iii.purrpulse.databinding.FragmentGamelistBinding


class GamelistFragment : Fragment() {

    companion object {
        private var title : String = ""

        private var gamesList : Array<GameListing> = arrayOf()

        private val easyGames = arrayOf(
            GameListing("Balls", ::launchlibBalls),
            GameListing("Clicker", ::launchlibClicker),
            GameListing("Tessellation", ::launchlibTesselation),
            GameListing("Light", ::launchlibFlames),
        )

        private val midGames = arrayOf(
            GameListing("Canvas", ::launchlibCanvas),
        )

        private val hardGames = arrayOf(GameListing(),
            GameListing(),
            GameListing(),
            GameListing(),
            GameListing(),
            GameListing(),
            GameListing(),
            GameListing(),
            GameListing(),
            GameListing(),
            GameListing())

        fun launchlibBalls(v : View) {
            launcher_mode = LauncherMode.Balls
            val intent = Intent(v.context, Launcher::class.java)
            v.context.startActivity(intent)
        }
        fun launchlibTesselation(v : View) {
            launcher_mode = LauncherMode.Tesselation
            val intent = Intent(v.context, Launcher::class.java)
            v.context.startActivity(intent)
        }

        fun launchlibCanvas(v : View) {
            launcher_mode = LauncherMode.Canvas
            val intent = Intent(v.context, Launcher::class.java)
            v.context.startActivity(intent)
        }
        fun launchlibFlames(v : View) {
            launcher_mode = LauncherMode.Flames
            val intent = Intent(v.context, Launcher::class.java)
            v.context.startActivity(intent)
        }

        fun launchlibClicker(v : View) {
            launcher_mode = LauncherMode.Clicker
            val intent = Intent(v.context, Launcher::class.java)
            v.context.startActivity(intent)
        }


        fun setEasyGames() {
            title = "Mindless games"
            gamesList = easyGames
        }

        fun setMidGames() {
            title = "Semi mindful games"
            gamesList = midGames
        }

        fun setHardGames() {
            title = "Mindful games"
            gamesList = hardGames
        }

        fun getTitle() : String {
            return title;
        }
    }

    class GameListing {
        var name : String = "PLACEHOLDER";
        var runner : (View) -> Unit = fun (v : View){
            Toast.makeText(v.context, "This is a placeholder for a game", Toast.LENGTH_SHORT).show()
        }

        constructor(){}
        constructor(name : String, runner : (View) -> Unit) {
            this.name = name;
            this.runner = runner;
        }
    }

    private var _binding: FragmentGamelistBinding? = null

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
            tv.text = game.name
            tv.setOnClickListener {
                game.runner.invoke(it)
            }
            ll.addView(tv)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}