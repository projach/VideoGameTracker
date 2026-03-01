package com.projach.videogametracker.ui.videoGamesList

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.projach.videogametracker.domain.onError
import com.projach.videogametracker.domain.onSuccess
import com.projach.videogametracker.domain.useCases.GetVideoGamesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
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
            null,
            false
        )
    )
    val uiState: StateFlow<VideoGamesListUiState> = _uiState

    init {
        fetchVideoGames()
    }

    fun fetchVideoGames(){
        when(_uiState.value.isLoading){
            true -> Unit
            false -> {
                _uiState.tryEmit(
                    _uiState.value.copy(
                        isLoading = true
                    )
                )
                viewModelScope.launch(Dispatchers.IO) {
                    getVideoGamesUseCase.invoke().onSuccess {
                        _uiState.tryEmit(
                            _uiState.value.copy(
                                videoGames = it,
                                isLoading = false
                            )
                        )
                    }.onError {
                        _uiState.tryEmit(
                            _uiState.value.copy(
                                isLoading = false,
                                errorMessage = "Could not fetch items"
                            )
                        )
                    }
                }
            }
        }
    }

    fun onItemClick(id: Int){
        //TODO(navigate to item)
        Log.d("item click", "with id: $id")
    }

    fun onAddToFavoritesClick(id: Int){
        //TODO(navigate to item)
        Log.d("item click", "with id: $id")
    }
}