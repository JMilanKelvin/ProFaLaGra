package com.example.profalagra.features

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class NavigationViewModel : ViewModel() {
    private val _openImagePicker = MutableLiveData<Boolean>()
    val openImagePicker: LiveData<Boolean> get() = _openImagePicker

    val selectedIndex = MutableLiveData<Int>()


    fun triggerImagePicker() {
        _openImagePicker.value = true
    }

    fun resetTrigger() {
        _openImagePicker.value = false
    }
}
