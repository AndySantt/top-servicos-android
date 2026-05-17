package com.top.example.feed

import android.content.Context
import android.net.Uri
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import com.top.example.net.ApiService
import com.top.example.net.contentUriToSmartPart

suspend fun addPost(
    api: ApiService,
    ctx: Context,
    imageUri: Uri,
    caption: String
): Post {
    val imagePart = contentUriToSmartPart(
        resolver = ctx.contentResolver,
        uri = imageUri,
        fieldName = "image",
        compressThresholdBytes = 400 * 1024L,
        maxSizePx = 1280,
        quality = 82
    )

    val captionBody = caption.toRequestBody(MultipartBody.FORM)
    val created = api.createPost(imagePart, captionBody)


    val imageUrl = created.imageUrl ?: ""

    return Post(
        id = created.id,
        imageUrl = imageUrl,
        caption = caption,
        starCount = 0,
        starred = false,
        createdAt = ""
    )
}

suspend fun togglePostStar(
    api: ApiService,
    post: Post
): Post {
    val res = api.toggleStar(post.id)
    return post.copy(starred = res.starred, starCount = res.starCount)
}
