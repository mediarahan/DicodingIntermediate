package com.dicoding.firstintermediateandroidsubmission.view.welcome

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.dicoding.firstintermediateandroidsubmission.data.repository.UserRepository

class WelcomeViewModel(repository: UserRepository) : ViewModel() {
    val isUserLoggedIn: LiveData<Boolean?> = repository.observeUserLoginStatus()
}