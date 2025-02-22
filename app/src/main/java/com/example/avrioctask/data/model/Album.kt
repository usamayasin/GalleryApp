package com.example.avrioctask.data.model

data class Album(
    val id: String = "0",
    val name: String = "",
    var mediaCount: Int = 0,
    val mediaItems: MutableList<MediaItem> = mutableListOf()
)