package com.dicoding.firstintermediateandroidsubmission.data.remote.response

import com.google.gson.annotations.SerializedName

data class FileUploadResponse(
    @field:SerializedName("message")
    val message: String
)