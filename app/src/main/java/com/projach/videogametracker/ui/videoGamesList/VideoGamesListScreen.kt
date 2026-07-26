package com.projach.videogametracker.ui.videoGamesList

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.projach.videogametracker.R
import com.projach.videogametracker.ui.IgdbImageSize
import com.projach.videogametracker.ui.changeIgdbSize
import com.projach.videogametracker.ui.theme.VideoGameTrackerTheme
import com.projach.videogametracker.ui.unixTimeToDateTime
import com.projach.videogametracker.utils.Logger

@Composable
fun VideoGamesListScreen() {
    val viewModel: VideoGamesListViewModel = hiltViewModel()
    val games = viewModel.games.collectAsLazyPagingItems()

    when (games.loadState.refresh) {
        is LoadState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize().background(color = Color.Transparent),
                contentAlignment = Alignment.Center
            ){
                CircularProgressIndicator()
            }
            Logger.d("videoGamesListScreen", "loading games")
        }
        is LoadState.Error -> {
            Logger.d("videoGamesListScreen", "error on games load")
        }
        is LoadState.NotLoading -> {}
    }

    LazyColumn(
        modifier = Modifier.padding(
            start = dimensionResource(R.dimen.padding_12),
            end = dimensionResource(R.dimen.padding_12)
        )
    ) {
        Logger.d("screen", "We have items on screen: ${games.itemCount}")
        items(
            count = games.itemCount,
            key = games.itemKey { it.id }
        ) { index ->
            games[index]?.let {
                VideoGameItem(
                    id = it.id,
                    name = it.name,
                    coverUrl = it.coverUrl,
                    genre = it.genres?.get(0),
                    releaseDate = it.firstReleaseDate?.unixTimeToDateTime()
                        ?: stringResource(R.string.na),
                    onItemClick = viewModel::onItemClick,
                    onAddToFavoritesClick = viewModel::onAddToFavoritesClick
                )
            }
        }
        when(games.loadState.append){
            is LoadState.Error -> {
                Logger.d("videoGamesListScreen", "error on games load")
            }
            is LoadState.Loading -> {
                item{
                    Box(
                        modifier = Modifier.fillMaxSize().background(color = Color.Transparent),
                        contentAlignment = Alignment.Center
                    ){
                        CircularProgressIndicator()
                    }
                }
                Logger.d("videoGamesListScreen", "loading games")
            }
            is LoadState.NotLoading -> {}
        }
    }
}

@Composable
fun VideoGameItem(
    id: Int,
    name: String,
    coverUrl: String?,
    genre: String?,
    releaseDate: String?,
    onItemClick: (Int) -> Unit,
    onAddToFavoritesClick: (Int) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = dimensionResource(R.dimen.padding_12))
            .clickable(onClick = { onItemClick.invoke(id) }),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Max)
        ) {
            coverUrl?.let {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current).data(
                        it.changeIgdbSize(
                            IgdbImageSize.COVER_BIG
                        )
                    ).crossfade(true).memoryCacheKey(it).diskCacheKey(it).build(),
                    contentDescription = "image of video game: $name",
                    modifier = Modifier
                        .fillMaxWidth(0.25f)
                        .aspectRatio(3f / 4f),
                    contentScale = ContentScale.Crop
                )
            } //TODO(show empty image)
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(start = dimensionResource(R.dimen.padding_8))
            ) {
                Text(
                    text = name,
                    modifier = Modifier.padding(bottom = dimensionResource(R.dimen.padding_4)),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                releaseDate?.let { Text(it) }

                Spacer(modifier = Modifier.weight(1f))

                HorizontalDivider(
                    modifier = Modifier.padding(dimensionResource(R.dimen.padding_2)),
                    thickness = dimensionResource(R.dimen.padding_2)
                )

                genre?.let {
                    Card(
                        shape = RoundedCornerShape(dimensionResource(R.dimen.padding_8)),
                        modifier = Modifier.padding(
                            top = dimensionResource(R.dimen.padding_4),
                            bottom = dimensionResource(R.dimen.padding_4)
                        ),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = dimensionResource(R.dimen.padding_8),
                            pressedElevation = dimensionResource(R.dimen.padding_12),
                            hoveredElevation = dimensionResource(R.dimen.padding_10)
                        ),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .height(IntrinsicSize.Min)
                                .fillMaxWidth()
                                .padding(
                                    top = dimensionResource(R.dimen.padding_6),
                                    bottom = dimensionResource(R.dimen.padding_6),
                                    start = dimensionResource(R.dimen.padding_12),
                                    end = dimensionResource(R.dimen.padding_12)
                                )
                        ) {
                            Text(
                                text = it,
                                modifier = Modifier.align(Alignment.CenterVertically)
                            )

                            Spacer(Modifier.weight(1f))

                            Icon(
                                painter = painterResource(R.drawable.ic_heart_24px),
                                contentDescription = "favorite icon",
                                modifier = Modifier
                                    .padding(dimensionResource(R.dimen.padding_4))
                                    .clickable(onClick = { onAddToFavoritesClick })
                            )
                        }
                    }
                }
            }
        }
    }
}

//@PreviewFontScale
//@PreviewScreenSizes
//@Preview(showBackground = true)
//@Composable
//fun VideoGamesContentPreview() {
//    VideoGameTrackerTheme {
//        Content(
//            videoGamesList = listOf(
//                VideoGameModel(
//                    1,
//                    80.0,
//                    "Kingdom come deliverance is a game about",
//                    "Kingdom Come Deliverance 2",
//                    listOf("RPG"),
//                    null,
//                    1768433644034
//                ),
//                VideoGameModel(
//                    2,
//                    60.0,
//                    "GTA is a game about etc",
//                    "GTA V",
//                    listOf("RPG"),
//                    null,
//                    1768433644034
//                )
//            ),
//            onItemClick = {},
//            onEndOfList = {},
//            onAddToFavoritesClick = {},
//        )
//    }
//}

@Preview(showBackground = true)
@Composable
fun VideoGameItemPreview() {
    VideoGameTrackerTheme {
        VideoGameItem(
            id = 123,
            name = "nikos",
            coverUrl = "",
            genre = "rpg",
            releaseDate = "March 20 2024",
            onItemClick = {},
            onAddToFavoritesClick = {}
        )
    }
}