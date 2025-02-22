package com.example.avrioctask.data.useCase

import com.example.avrioctask.data.model.DataState
import com.example.avrioctask.data.model.MediaItem
import com.example.avrioctask.data.repository.MediaRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMediaItemsUseCase @Inject constructor(
    private val repository: MediaRepository
) {
    suspend operator fun invoke(albumId: String): Flow<DataState<List<MediaItem>>> {
        return repository.getMediaItems(albumId = albumId)
    }
}