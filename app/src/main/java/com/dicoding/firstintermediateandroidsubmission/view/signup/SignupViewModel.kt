package com.dicoding.firstintermediateandroidsubmission.view.signup

import androidx.lifecycle.ViewModel
import com.dicoding.firstintermediateandroidsubmission.data.repository.UserRepository

class SignupViewModel(private val repository: UserRepository) : ViewModel() {
     fun register(name: String, email: String, password: String) = repository.register(name,email,password)
}