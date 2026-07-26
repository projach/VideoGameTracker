package com.projach.videogametracker.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.projach.videogametracker.domain.DataResult
import com.projach.videogametracker.domain.VideoGamesRepository
import com.projach.videogametracker.domain.models.VideoGameModel

class VideoGamesPaging(
    private val repository: VideoGamesRepository
): PagingSource<Int, VideoGameModel>(){
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, VideoGameModel> {
        val page = params.key ?: 1

        return when(val result = repository.fetchVideoGames(page)){
            is DataResult.Error -> LoadResult.Error(Exception("fetching error"))
            is DataResult.Success -> LoadResult.Page(
                data = result.data,
                prevKey = null, //we dont remove previous pages
                nextKey = if (result.data.isEmpty()) null else page + 1
            )
        }
    }

    override fun getRefreshKey(state: PagingState<Int, VideoGameModel>): Int? {
        return state.anchorPosition?.let { anchor ->
            state.closestPageToPosition(anchor)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchor)?.nextKey?.minus(1)
        }
    }
}