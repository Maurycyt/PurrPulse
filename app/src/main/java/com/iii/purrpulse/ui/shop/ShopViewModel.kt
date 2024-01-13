package com.iii.purrpulse.ui.shop

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ShopViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is a placeholder for the shop"
    }
    val text: LiveData<String> = _text
}