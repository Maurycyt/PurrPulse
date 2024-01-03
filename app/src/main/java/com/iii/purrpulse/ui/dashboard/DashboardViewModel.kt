package com.iii.purrpulse.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay

class DashboardViewModel : ViewModel() {
    private val _text = MutableLiveData("This is the dashboard Fragment.")

    init {
        startCoroutine()
    }

    private fun startCoroutine() {
        GlobalScope.launch(Dispatchers.Default) {
            var counter = 0
            while (true) {
                delay(1000)
                counter++
                println("Updating text with $counter.")
                _text.postValue("This is the dashboard Fragment ($counter)")
            }
        }
    }

    val text: LiveData<String> = _text
}