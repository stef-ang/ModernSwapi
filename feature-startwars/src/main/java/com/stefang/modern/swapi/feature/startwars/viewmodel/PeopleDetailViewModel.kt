package com.stefang.modern.swapi.feature.startwars.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stefang.modern.swapi.core.data.ResponseModel
import com.stefang.modern.swapi.core.data.model.PeopleModel
import com.stefang.modern.swapi.core.data.repository.SwapiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PeopleDetailViewModel @Inject constructor(
    private val repository: SwapiRepository
) : ViewModel() {

    private val _people = MutableStateFlow<ResponseModel<PeopleModel>>(ResponseModel.Loading())
    val people: StateFlow<ResponseModel<PeopleModel>> get() = _people

    private val _films = MutableStateFlow<ResponseModel<String>>(ResponseModel.Empty("-"))
    val films: StateFlow<ResponseModel<String>> get() = _films

    private val _species = MutableStateFlow<ResponseModel<String>>(ResponseModel.Empty("-"))
    val species: StateFlow<ResponseModel<String>> get() = _species

    private val _starships = MutableStateFlow<ResponseModel<String>>(ResponseModel.Empty("-"))
    val starships: StateFlow<ResponseModel<String>> get() = _starships

    private val _vehicles = MutableStateFlow<ResponseModel<String>>(ResponseModel.Empty("-"))
    val vehicles: StateFlow<ResponseModel<String>> get() = _vehicles

    fun fetchDetail(url: String) = viewModelScope.launch {
        repository.getPeopleDetail(url).onStart {
            _people.emit(ResponseModel.Loading())
        }.catch {
            _people.emit(ResponseModel.Error(it.message ?: "Internal Error"))
        }.collect {
            _people.emit(ResponseModel.Success(it))
        }
    }

    fun fetchAttributes(peopleModel: PeopleModel?) = viewModelScope.launch {
        peopleModel?.let {
            if (it.films.isNotEmpty()) fetchFilms(it.films)
            if (it.species.isNotEmpty()) fetchSpecies(it.species)
            if (it.starships.isNotEmpty()) fetchStarship(it.starships)
            if (it.vehicles.isNotEmpty()) fetchVehicles(it.vehicles)
        }
    }

    private suspend fun fetchFilms(urls: List<String>) {
        repository.getFilms(urls).onStart {
            _films.emit(ResponseModel.Loading())
        }.catch {
            _films.emit(ResponseModel.Error(it.message ?: "Internal Error"))
        }.map { list ->
            list.map { it.title }
        }.collect { list ->
            _films.emit(ResponseModel.Success(list.joinToString(", ")))
        }
    }

    private suspend fun fetchSpecies(urls: List<String>) {
        repository.getSpecies(urls).onStart {
            _species.emit(ResponseModel.Loading())
        }.catch {
            _species.emit(ResponseModel.Error(it.message ?: "Internal Error"))
        }.map { list ->
            list.map { it.name }
        }.collect { list ->
            _species.emit(ResponseModel.Success(list.joinToString(", ")))
        }
    }

    private suspend fun fetchStarship(urls: List<String>) {
        repository.getStarships(urls).onStart {
            _starships.emit(ResponseModel.Loading())
        }.catch {
            _starships.emit(ResponseModel.Error(it.message ?: "Internal Error"))
        }.map { list ->
            list.map { it.name }
        }.collect { list ->
            _starships.emit(ResponseModel.Success(list.joinToString(", ")))
        }
    }

    private suspend fun fetchVehicles(urls: List<String>) {
        repository.getVehicles(urls).onStart {
            _vehicles.emit(ResponseModel.Loading())
        }.catch {
            _vehicles.emit(ResponseModel.Error(it.message ?: "Internal Error"))
        }.map { list ->
            list.map { it.name }
        }.collect { list ->
            _vehicles.emit(ResponseModel.Success(list.joinToString(", ")))
        }
    }
}
