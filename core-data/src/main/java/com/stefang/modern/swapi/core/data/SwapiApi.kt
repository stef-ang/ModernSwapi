package com.stefang.modern.swapi.core.data

import com.stefang.modern.swapi.core.data.model.FilmModel
import com.stefang.modern.swapi.core.data.model.PaginationResponse
import com.stefang.modern.swapi.core.data.model.PeopleModel
import com.stefang.modern.swapi.core.data.model.SimplePeopleModel
import com.stefang.modern.swapi.core.data.model.SpeciesModel
import com.stefang.modern.swapi.core.data.model.StarshipModel
import com.stefang.modern.swapi.core.data.model.VehicleModel
import retrofit2.http.GET
import retrofit2.http.Url

interface SwapiApi {

    @GET
    suspend fun getAllPeople(@Url url: String = POEPLE_URL): PaginationResponse<SimplePeopleModel>

    @GET
    suspend fun getPeopleDetail(@Url url: String): PeopleModel

    @GET
    suspend fun getFilmDetail(@Url url: String): FilmModel

    @GET
    suspend fun getStarshipDetail(@Url url: String): StarshipModel

    @GET
    suspend fun getVehicleDetail(@Url url: String): VehicleModel

    @GET
    suspend fun getSpeciesDetail(@Url url: String): SpeciesModel

    companion object {
        const val POEPLE_URL = "https://swapi.dev/api/people/"
    }
}
