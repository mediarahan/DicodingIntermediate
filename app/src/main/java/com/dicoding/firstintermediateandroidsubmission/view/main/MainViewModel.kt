package com.dicoding.firstintermediateandroidsubmission.view.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import com.dicoding.firstintermediateandroidsubmission.data.repository.UserRepository
import com.dicoding.firstintermediateandroidsubmission.data.remote.response.ListStoryItem

class MainViewModel(private val repository: UserRepository) : ViewModel() {
    val stories: LiveData<PagingData<ListStoryItem>> = repository.getStoryList()

    suspend fun logout() {
        repository.logout()
    }

}