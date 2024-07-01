package com.dicoding.firstintermediateandroidsubmission.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.dicoding.firstintermediateandroidsubmission.data.pref.UserPreference
import com.dicoding.firstintermediateandroidsubmission.data.remote.response.ListStoryItem
import com.dicoding.firstintermediateandroidsubmission.data.remote.retrofit.ApiService
import kotlinx.coroutines.flow.first

class StoryPagingSource(
    private val apiService: ApiService,
    private val userPreference: UserPreference
) : PagingSource<Int, ListStoryItem>() {
    override fun getRefreshKey(state: PagingState<Int, ListStoryItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ListStoryItem> {
        return try {
            val position = params.key ?: INITIAL_PAGE_INDEX
            val userModel = userPreference.getSession().first()
            val token = userModel.token
            val responseData = apiService.getStories("Bearer $token", position, params.loadSize, 0)
            LoadResult.Page(
                data = responseData.listStory,
                prevKey = if (position == INITIAL_PAGE_INDEX) null else position - 1,
                nextKey = if (responseData.listStory.isEmpty()) null else position + 1
            )
        } catch (exception: Exception) {
            return LoadResult.Error(exception)
        }
    }

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }
}