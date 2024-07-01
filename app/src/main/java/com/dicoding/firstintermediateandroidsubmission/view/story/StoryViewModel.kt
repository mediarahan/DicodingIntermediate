package com.dicoding.firstintermediateandroidsubmission.view.story

import androidx.lifecycle.ViewModel
import com.dicoding.firstintermediateandroidsubmission.data.repository.UserRepository

class StoryViewModel(private val repository: UserRepository) : ViewModel() {
    fun getStoryDetail(id :String) = repository.getDetailStory(id)
}