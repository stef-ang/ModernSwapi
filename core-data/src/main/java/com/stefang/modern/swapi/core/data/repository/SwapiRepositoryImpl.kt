package com.stefang.modern.swapi.core.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.stefang.modern.swapi.core.data.SwapiApi
import com.stefang.modern.swapi.core.data.model.FilmModel
import com.stefang.modern.swapi.core.data.model.PeopleModel
import com.stefang.modern.swapi.core.data.model.SimplePeopleModel
import com.stefang.modern.swapi.core.data.model.SpeciesModel
import com.stefang.modern.swapi.core.data.model.StarshipModel
import com.stefang.modern.swapi.core.data.model.VehicleModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SwapiRepositoryImpl @Inject constructor(
    private val swapiApi: SwapiApi
) : SwapiRepository {

    private var nextPage: String? = SwapiApi.POEPLE_URL

    override fun getPeoplePaging(): Flow<PagingData<SimplePeopleModel>> {
        return Pager(PagingConfig(pageSize = 10)) {
            object : PagingSource<String, SimplePeopleModel>() {
                override fun getRefreshKey(state: PagingState<String, SimplePeopleModel>): String? {
                    return nextPage
                }

                override suspend fun load(params: LoadParams<String>): LoadResult<String, SimplePeopleModel> {
                    return try {
                        val url = params.key ?: nextPage
                        val response = url?.let {
                            swapiApi.getAllPeople(it)
                        } ?: swapiApi.getAllPeople()
                        nextPage = response.next
                        LoadResult.Page(
                            data = response.results ?: emptyList(),
                            prevKey = null,
                            nextKey = response.next // once response return null next, load won't be triggered
                        )
                    } catch (e: Exception) {
                        LoadResult.Error(e)
                    }
                }
            }
        }.flow
    }

    override fun getPeopleDetail(url: String): Flow<PeopleModel> = flow {
        emit(swapiApi.getPeopleDetail(url))
    }

    override fun getFilms(urls: List<String>): Flow<List<FilmModel>> = flow {
        val response = withContext(Dispatchers.IO) {
            val requests = urls.map { async { swapiApi.getFilmDetail(it) } }
            requests.map { it.await() }
        }
        emit(response)
    }

    override fun getSpecies(urls: List<String>): Flow<List<SpeciesModel>> = flow {
        coroutineScope {
            val requests = urls.map { async { swapiApi.getSpeciesDetail(it) } }
            val response = requests.map { it.await() }
            emit(response)
        }
    }.flowOn(Dispatchers.IO)

    override fun getStarships(urls: List<String>): Flow<List<StarshipModel>> = flow {
        val response = withContext(Dispatchers.IO) {
            val requests = urls.map { async { swapiApi.getStarshipDetail(it) } }
            requests.map { it.await() }
        }
        emit(response)
    }

    override fun getVehicles(urls: List<String>): Flow<List<VehicleModel>> = flow {
        val response = withContext(Dispatchers.IO) {
            val requests = urls.map { async { swapiApi.getVehicleDetail(it) } }
            requests.map { it.await() }
        }
        emit(response)
    }
}
