package com.dicoding.firstintermediateandroidsubmission.view.addstory

import androidx.lifecycle.ViewModel
import com.dicoding.firstintermediateandroidsubmission.data.repository.UserRepository
import java.io.File

class AddStoryViewModel(private val repository: UserRepository) : ViewModel() {
    fun uploadStory(file: File, description: String) = repository.uploadStory(file, description)
}