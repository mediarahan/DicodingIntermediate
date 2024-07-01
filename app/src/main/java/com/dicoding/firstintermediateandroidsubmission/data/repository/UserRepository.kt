package com.dicoding.firstintermediateandroidsubmission.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.dicoding.firstintermediateandroidsubmission.data.Result
import com.dicoding.firstintermediateandroidsubmission.data.StoryPagingSource
import com.dicoding.firstintermediateandroidsubmission.data.remote.retrofit.ApiService
import com.dicoding.firstintermediateandroidsubmission.data.pref.UserModel
import com.dicoding.firstintermediateandroidsubmission.data.pref.UserPreference
import com.dicoding.firstintermediateandroidsubmission.data.remote.response.DetailStoryResponse
import com.dicoding.firstintermediateandroidsubmission.data.remote.response.FileUploadResponse
import com.dicoding.firstintermediateandroidsubmission.data.remote.response.ListStoryItem
import com.dicoding.firstintermediateandroidsubmission.data.remote.response.LoginResponse
import com.dicoding.firstintermediateandroidsubmission.data.remote.response.RegisterResponse
import com.dicoding.firstintermediateandroidsubmission.data.remote.response.StoryListResponse
import com.google.gson.Gson
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import java.io.File

class UserRepository(
    private val apiService: ApiService, private val userPreference: UserPreference
) {
    fun register(name: String, email: String, password: String): LiveData<Result<RegisterResponse>> = liveData {
        emit(Result.Loading)
        try {
            //tambahkan if kalau responsenya berhasil / ngga
            val response = apiService.register(name, email, password)
            if (response.error!!) {
                response.message?.let { Result.Error(it) }?.let { emit(it) }
            } else {
                emit(Result.Success(response))
            }

        } catch (e: Exception) {
            Log.d("UserRepository", "register:${e.message.toString()}")
            emit(Result.Error(e.message.toString()))
        }
    }

    fun login(email: String, password: String): LiveData<Result<LoginResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.login(email, password)
            if (response.error!!) {
                response.message?.let { Result.Error(it) }?.let { emit(it) }
            } else {
                val token = response.loginResult?.token

                val userModel = UserModel(email, token, true)
                userPreference.saveSession(userModel)

                emit(Result.Success(response))
            }
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getStoryList(): LiveData<PagingData<ListStoryItem>> {
        return Pager(
            config = PagingConfig (
                pageSize = 5
            ),
            pagingSourceFactory = {
                StoryPagingSource(apiService,userPreference)
            }
        ).liveData
    }

    fun getDetailStory(id: String): LiveData<Result<DetailStoryResponse>> = liveData {
        emit(Result.Loading)
        val userModel = userPreference.getSession().first()
        val token = userModel.token
        try {
            val response = apiService.getStoryDetail("Bearer $token", id)

            if (response.error == false) {
                emit(Result.Success(response))
            } else {
                response.message?.let { Result.Error(it) }?.let { emit(it) }
            }

        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    fun uploadStory(imageFile: File, description: String): LiveData<Result<FileUploadResponse>> =
        liveData {
            emit(Result.Loading)
            val requestBody = description.toRequestBody("text/plain".toMediaType())
            val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
            val multipartBody = MultipartBody.Part.createFormData(
                "photo", imageFile.name, requestImageFile
            )
            try {
                val userModel = userPreference.getSession().first()
                val token = userModel.token

                val successResponse =
                    apiService.uploadImage("Bearer $token", multipartBody, requestBody)
                emit(Result.Success(successResponse))
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                val errorResponse = Gson().fromJson(errorBody, FileUploadResponse::class.java)
                emit(Result.Error(errorResponse.message))
            }
        }

    fun getLocationStory(): LiveData<Result<StoryListResponse>> = liveData {
        emit(Result.Loading)
        val userModel = userPreference.getSession().first()
        val token = userModel.token
        try {
            val response = apiService.getStories("Bearer $token", 1,20,1)
            if (response.error == false) {
                val listStory = response.listStory
                // Add a log statement here to check if listStory contains values
                if (listStory.isNotEmpty()) {
                    Log.d("UserRepository", "listStory (Location) contains values: ${listStory.size} items")
                } else {
                    Log.d("UserRepository", "listStory (Location) is empty")
                }

                emit(Result.Success(response))
            } else {
                emit(Result.Error("Error getting data and stuff"))
            }
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))

        }
    }

    suspend fun logout() {
        userPreference.logout()
    }

    fun observeUserLoginStatus(): LiveData<Boolean?> {
        return userPreference.getSession().map { userModel ->
            userModel.isLogin
        }.asLiveData()
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            apiService: ApiService, pref: UserPreference
        ): UserRepository = instance ?: synchronized(this) {
            instance ?: UserRepository(apiService, pref)
        }.also { instance = it }
    }
}