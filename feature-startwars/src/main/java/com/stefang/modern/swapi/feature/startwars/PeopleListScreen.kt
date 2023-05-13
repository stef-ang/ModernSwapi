package com.stefang.modern.swapi.feature.startwars

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.stefang.modern.swapi.core.data.model.SimplePeopleModel
import com.stefang.modern.swapi.core.ui.ScaffoldScreen
import com.stefang.modern.swapi.feature.startwars.viewmodel.PeopleListViewModel

@Composable
fun PeopleListRoute(
    title: String,
    onClickPeople: (String?) -> Unit,
    viewModel: PeopleListViewModel = hiltViewModel()
) {
    val peoplePagingItems = viewModel.people.collectAsLazyPagingItems()
    ScaffoldScreen(
        title = title
    ) {
        Column(modifier = Modifier.padding(start = 16.dp, top = it.calculateTopPadding(), end = 16.dp)) {
            PeopleList(peoplePagingItems, onClickPeople)
        }
    }
}

@Composable
fun PeopleList(
    pagingItems: LazyPagingItems<SimplePeopleModel>,
    onClickPeople: (String?) -> Unit
) {
    val scrollState = pagingItems.rememberLazyListState()

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth(),
        contentPadding = PaddingValues(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        state = scrollState
    ) {
        items(
            count = pagingItems.itemCount
        ) { index ->
            ElevatedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onClickPeople(pagingItems[index]?.url) }
            ) {
                Text(
                    modifier = Modifier.padding(8.dp),
                    text = pagingItems[index]?.name ?: ""
                )
            }
        }

        pagingItems.apply {
            when {
                loadState.refresh is LoadState.Loading ||
                    loadState.append is LoadState.Loading -> {
                    item { CircularProgressIndicator() }
                }
                loadState.refresh is LoadState.Error ||
                    loadState.append is LoadState.Error -> {
                    val e = pagingItems.loadState.append as LoadState.Error
                    item {
                        Text("Error: ${e.error.localizedMessage}")
                        Button(onClick = { pagingItems.retry() }) {
                            Text("Retry")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun <T : Any> LazyPagingItems<T>.rememberLazyListState(): LazyListState {
    // After recreation, LazyPagingItems first return 0 items, then the cached items.
    // This behavior/issue is resetting the LazyListState scroll position.
    // Below is a workaround. More info: https://issuetracker.google.com/issues/177245496.
    return when (itemCount) {
        // Return a different LazyListState instance.
        0 -> remember(this) { LazyListState(0, 0) }
        // Return rememberLazyListState (normal case).
        else -> androidx.compose.foundation.lazy.rememberLazyListState()
    }
}
