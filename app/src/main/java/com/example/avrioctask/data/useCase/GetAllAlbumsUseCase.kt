package com.example.avrioctask.data.useCase

import com.example.avrioctask.data.model.Album
import com.example.avrioctask.data.model.DataState
import com.example.avrioctask.data.repository.MediaRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllAlbumsUseCase @Inject constructor(
    private val repository: MediaRepository
) {
    suspend operator fun invoke(): Flow<DataState<List<Album>>> {
        return repository.getAllAlbums()
    }
}