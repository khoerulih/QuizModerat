package com.khoerulih.quizmoderat.ui.onboarding

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.khoerulih.quizmoderat.SettingPreferences
import kotlinx.coroutines.launch

class OnboardingViewModel(private val pref: SettingPreferences): ViewModel() {
    fun getOnboardingStatusKey(): LiveData<Boolean> {
        return pref.getOnboardingStatusKey().asLiveData()
    }

    fun saveOnboardingStatusKey(status: Boolean) {
        viewModelScope.launch {
            pref.saveOnboardingStatusKey(status)
        }
    }
}