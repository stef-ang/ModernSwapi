package com.stefang.modern.swapi.feature.startwars.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.stefang.modern.swapi.core.data.repository.SwapiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PeopleListViewModel @Inject constructor(
    repository: SwapiRepository
) : ViewModel() {

    val people = repository.getPeoplePaging().cachedIn(viewModelScope)
}
