package com.iii.purrpulse.ui.shop

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.iii.purrpulse.databinding.FragmentShopBinding

class ShopFragment : Fragment() {

    private var _binding: FragmentShopBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val shopViewModel =
            ViewModelProvider(this).get(ShopViewModel::class.java)

        _binding = FragmentShopBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textShop
        shopViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}