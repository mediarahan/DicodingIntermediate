package com.dicoding.firstintermediateandroidsubmission.view.main

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.firstintermediateandroidsubmission.data.remote.response.ListStoryItem
import com.dicoding.firstintermediateandroidsubmission.databinding.StoryListRvBinding
import com.dicoding.firstintermediateandroidsubmission.view.story.StoryActivity

class StoryListAdapter: PagingDataAdapter<ListStoryItem, StoryListAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = StoryListRvBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val stories = getItem(position)
        if (stories != null) {
            holder.bind(stories)
        }

        holder.itemView.setOnClickListener {
            val id = stories?.id
            val intentDetailStory = Intent(holder.itemView.context, StoryActivity::class.java)
            intentDetailStory.putExtra("id", id)
            holder.itemView.context.startActivity(intentDetailStory)
        }
    }

    class MyViewHolder(private val binding: StoryListRvBinding) : RecyclerView.ViewHolder(binding.root) {
        private val posterImageView: ImageView = binding.imgPoster
        fun bind(stories: ListStoryItem) {
            binding.titleText.text ="${stories.name}"
            binding.subtitleText.text="${stories.description}"
            Glide.with(itemView)
                .load(stories.photoUrl)
                .into(posterImageView)
        }

    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }
        }
    }

}

