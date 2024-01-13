package com.iii.purrpulse.ui.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.iii.purrpulse.gdx_stuff.use_highp

class SettingsViewModel : ViewModel() {

    private val _isHighQualityOn = MutableLiveData<Boolean>()

    val isHighQualityOn: LiveData<Boolean> get() = _isHighQualityOn

    fun setHighQuality(isOn: Boolean) {
        _isHighQualityOn.value = isOn
        use_highp = isOn
    }
}