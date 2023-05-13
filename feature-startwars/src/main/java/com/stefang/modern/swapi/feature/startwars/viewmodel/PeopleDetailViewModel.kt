package com.stefang.modern.swapi.feature.startwars.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stefang.modern.swapi.core.data.model.PeopleModel
import com.stefang.modern.swapi.core.data.repository.SwapiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PeopleDetailViewModel @Inject constructor(
    private val repository: SwapiRepository
) : ViewModel() {

    private val _people = MutableStateFlow<PeopleModel?>(null)
    val people: StateFlow<PeopleModel?> get() = _people

    private val _films = MutableStateFlow("")
    val films: StateFlow<String> get() = _films

    private val _species = MutableStateFlow("")
    val species: StateFlow<String> get() = _species

    private val _starships = MutableStateFlow("")
    val starships: StateFlow<String> get() = _starships

    private val _vehicles = MutableStateFlow("")
    val vehicles: StateFlow<String> get() = _vehicles

    fun fetchDetail(url: String) = viewModelScope.launch {
        repository.getPeopleDetail(url)
            .collect {
                _people.emit(it)
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
        repository.getFilms(urls)
            .map { list ->
                list.map { it.title }
            }.collect { list ->
                _films.emit(list.joinToString(", "))
            }
    }

    private suspend fun fetchSpecies(urls: List<String>) {
        repository.getSpecies(urls)
            .map { list ->
                list.map { it.name }
            }.collect { list ->
                _species.emit(list.joinToString(", "))
            }
    }

    private suspend fun fetchStarship(urls: List<String>) {
        repository.getStarships(urls)
            .map { list ->
                list.map { it.name }
            }.collect { list ->
                _starships.emit(list.joinToString(", "))
            }
    }

    private suspend fun fetchVehicles(urls: List<String>) {
        repository.getVehicles(urls)
            .map { list ->
                list.map { it.name }
            }.collect { list ->
                _vehicles.emit(list.joinToString(", "))
            }
    }
}
