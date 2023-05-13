package com.stefang.modern.swapi.core.data.repository

import androidx.paging.PagingData
import com.stefang.modern.swapi.core.data.model.FilmModel
import com.stefang.modern.swapi.core.data.model.PeopleModel
import com.stefang.modern.swapi.core.data.model.SimplePeopleModel
import com.stefang.modern.swapi.core.data.model.SpeciesModel
import com.stefang.modern.swapi.core.data.model.StarshipModel
import com.stefang.modern.swapi.core.data.model.VehicleModel
import kotlinx.coroutines.flow.Flow

interface SwapiRepository {

    fun getPeoplePaging(): Flow<PagingData<SimplePeopleModel>>

    fun getPeopleDetail(url: String): Flow<PeopleModel>

    fun getFilms(urls: List<String>): Flow<List<FilmModel>>

    fun getSpecies(urls: List<String>): Flow<List<SpeciesModel>>

    fun getStarships(urls: List<String>): Flow<List<StarshipModel>>

    fun getVehicles(urls: List<String>): Flow<List<VehicleModel>>
}
