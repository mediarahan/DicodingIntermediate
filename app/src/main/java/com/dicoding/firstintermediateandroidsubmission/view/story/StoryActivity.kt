package com.dicoding.firstintermediateandroidsubmission.view.story

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.bumptech.glide.Glide
import com.dicoding.firstintermediateandroidsubmission.view.ViewModelFactory
import com.dicoding.firstintermediateandroidsubmission.data.Result
import com.dicoding.firstintermediateandroidsubmission.data.remote.response.Story
import com.dicoding.firstintermediateandroidsubmission.databinding.ActivityStoryBinding

class StoryActivity : AppCompatActivity() {

    private val storyViewModel by viewModels<StoryViewModel> { ViewModelFactory.getInstance(this) }
    private lateinit var binding: ActivityStoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val id = intent.getStringExtra("id")

        if (id != null) {
            storyViewModel.getStoryDetail(id).observe(this@StoryActivity) { detailStoryResult ->
                when (detailStoryResult) {
                    is Result.Success -> {
                        showLoading(false)
                        val detailStoryData = detailStoryResult.data
                        val detailedStory = detailStoryData.story
                        if (detailedStory != null) {
                            setDetailedStoryData(detailedStory)
                        }
                    }
                    is Result.Error -> {
                        val message = "Login  was Unsuccessful"
                        showToast(message)
                        showLoading(false)
                    }
                    is Result.Loading -> {
                        showLoading(true)
                    }
                }
            }
        }
    }


    private fun setDetailedStoryData(detailedStoryData: Story) {
        binding.titleText.text = detailedStoryData.name
        binding.subtitleText.text = detailedStoryData.description

        Glide.with(this)
            .load(detailedStoryData.photoUrl)
            .into(binding.imgPoster)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}