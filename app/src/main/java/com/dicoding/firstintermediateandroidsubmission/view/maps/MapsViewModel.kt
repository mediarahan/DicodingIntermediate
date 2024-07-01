package com.dicoding.firstintermediateandroidsubmission.view.maps

import androidx.lifecycle.ViewModel
import com.dicoding.firstintermediateandroidsubmission.data.repository.UserRepository

class MapsViewModel(private val repository: UserRepository) : ViewModel() {
    fun getStoryLocation() = repository.getLocationStory()
}