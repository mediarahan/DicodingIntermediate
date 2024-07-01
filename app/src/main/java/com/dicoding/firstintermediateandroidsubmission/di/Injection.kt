package com.dicoding.firstintermediateandroidsubmission.di

import android.content.Context
import com.dicoding.firstintermediateandroidsubmission.data.repository.UserRepository
import com.dicoding.firstintermediateandroidsubmission.data.remote.retrofit.ApiConfig
import com.dicoding.firstintermediateandroidsubmission.data.pref.UserPreference
import com.dicoding.firstintermediateandroidsubmission.data.pref.dataStore

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val apiService = ApiConfig.getApiService()
        return UserRepository.getInstance(apiService, pref)
    }
}
