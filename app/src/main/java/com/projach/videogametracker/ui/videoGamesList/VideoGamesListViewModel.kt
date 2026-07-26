package com.projach.videogametracker.ui.videoGamesList

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.cachedIn
import com.projach.videogametracker.domain.models.VideoGameModel
import com.projach.videogametracker.domain.onError
import com.projach.videogametracker.domain.onSuccess
import com.projach.videogametracker.domain.useCases.GetVideoGamesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VideoGamesListViewModel @Inject constructor(
    private val getVideoGamesUseCase: GetVideoGamesUseCase
): ViewModel() {
    private val _uiState = MutableStateFlow(
        VideoGamesListUiState(
            null,
            false
        )
    )
    val uiState: StateFlow<VideoGamesListUiState> = _uiState

    val games: Flow<PagingData<VideoGameModel>> =
        getVideoGamesUseCase.invoke().cachedIn(viewModelScope)

//    private var canFetch = true

//    init {
//        fetchVideoGames()
//    }

//    fun fetchVideoGames(){
//        when(canFetch){
//            false -> Unit
//            true -> {
//                canFetch = false
//                _uiState.tryEmit(
//                    _uiState.value.copy(
//                        isLoading = true
//                    )
//                )
//                viewModelScope.launch(Dispatchers.IO) {
//                    getVideoGamesUseCase.invoke().onSuccess {
//                        _uiState.tryEmit(
//                            _uiState.value.copy(
//                                videoGames = it,
//                                isLoading = false
//                            )
//                        )
//                    }.onError {
//                        _uiState.tryEmit(
//                            _uiState.value.copy(
//                                isLoading = false,
//                                errorMessage = "Could not fetch items"
//                            )
//                        )
//                    }
//                    canFetch = true
//                }
//            }
//        }
//    }

    fun onItemClick(id: Int){
        //TODO(navigate to item)
        Log.d("item click", "with id: $id")
    }

    fun onAddToFavoritesClick(id: Int){
        //TODO(navigate to item)
        Log.d("item click", "with id: $id")
    }
}