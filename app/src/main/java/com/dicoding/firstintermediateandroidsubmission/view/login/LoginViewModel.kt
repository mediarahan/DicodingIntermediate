package com.dicoding.firstintermediateandroidsubmission.view.login

import androidx.lifecycle.ViewModel
import com.dicoding.firstintermediateandroidsubmission.data.repository.UserRepository

class LoginViewModel(private val repository: UserRepository) : ViewModel() {
    fun login(email: String, password: String) = repository.login(email,password)
}