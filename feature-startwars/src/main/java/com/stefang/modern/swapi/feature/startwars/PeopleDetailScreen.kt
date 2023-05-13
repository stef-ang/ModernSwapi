package com.stefang.modern.swapi.feature.startwars

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.stefang.modern.swapi.core.data.ResponseModel
import com.stefang.modern.swapi.core.data.model.PeopleModel
import com.stefang.modern.swapi.core.ui.DeepPurple500
import com.stefang.modern.swapi.core.ui.MyApplicationTheme
import com.stefang.modern.swapi.core.ui.ScaffoldScreen
import com.stefang.modern.swapi.feature.startwars.viewmodel.PeopleDetailViewModel

@Composable
fun PeopleDetailRoute(
    title: String,
    url: String?,
    viewModel: PeopleDetailViewModel = hiltViewModel()
) {
    val peopleModel by viewModel.people.collectAsStateWithLifecycle()
    val filmsModel by viewModel.films.collectAsStateWithLifecycle()
    val speciesModel by viewModel.species.collectAsStateWithLifecycle()
    val starshipsModel by viewModel.starships.collectAsStateWithLifecycle()
    val vehiclesModel by viewModel.vehicles.collectAsStateWithLifecycle()

    LaunchedEffect(url) {
        url?.let(viewModel::fetchDetail)
    }

    LaunchedEffect(peopleModel) {
        (peopleModel as? ResponseModel.Success<*>)?.let {
            viewModel.fetchAttributes(it.data as? PeopleModel)
        }
    }

    ScaffoldScreen(
        title = title
    ) {
        Column(modifier = Modifier.padding(start = 16.dp, top = it.calculateTopPadding(), end = 16.dp)) {
            PeopleDetailScreen(peopleModel, filmsModel, speciesModel, starshipsModel, vehiclesModel)
        }
    }
}

@Composable
fun PeopleDetailScreen(
    peopleModel: ResponseModel<PeopleModel>,
    filmsModel: ResponseModel<String>,
    speciesModel: ResponseModel<String>,
    starshipsModel: ResponseModel<String>,
    vehiclesModel: ResponseModel<String>
) {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        when (peopleModel) {
            is ResponseModel.Success -> {
                val people = peopleModel.data
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    RowField("Name:", people.name)
                    Row {
                        RowField("Height:", people.height)
                        Spacer(modifier = Modifier.size(16.dp))
                        RowField("Mass:", people.mass)
                    }
                    Text(text = "Color")
                    Row {
                        RowField("Hair:", people.hair_color)
                        Spacer(modifier = Modifier.size(16.dp))
                        RowField("Skin:", people.skin_color)
                        Spacer(modifier = Modifier.size(16.dp))
                        RowField("Eye:", people.eye_color)
                    }
                    RowField("Gender:", people.gender)
                    RowField("Birth Year:", people.birth_year)
                    ColumnField("Films:", filmsModel)
                    ColumnField("Species:", speciesModel)
                    ColumnField("Starships:", starshipsModel)
                    ColumnField("Vehicles:", vehiclesModel)
                }
            }
            is ResponseModel.Loading -> CircularProgressIndicator()
            is ResponseModel.Error -> {
                Text(
                    text = "Error: ${peopleModel.message}",
                    color = MaterialTheme.colorScheme.error
                )
            }
            else -> Text(peopleModel.message)
        }
    }
}

@Composable
private fun RowField(label: String, value: String) {
    Row(
        modifier = Modifier.padding(bottom = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label)
        Spacer(modifier = Modifier.size(4.dp))
        Text(
            text = value,
            color = DeepPurple500,
            fontWeight = FontWeight.SemiBold,
        )
    }
}

@Composable
private fun ColumnField(label: String, responseModel: ResponseModel<String>) {
    Column(modifier = Modifier.padding(bottom = 12.dp)) {
        Text(text = label)
        when (responseModel) {
            is ResponseModel.Success -> {
                Text(
                    text = responseModel.data.ifBlank { "-" },
                    color = DeepPurple500,
                    fontWeight = FontWeight.SemiBold,
                )
            }
            is ResponseModel.Loading -> CircularProgressIndicator()
            is ResponseModel.Error -> {
                Text(
                    text = "Error: ${responseModel.message}",
                    color = MaterialTheme.colorScheme.error
                )
            }
            else -> Text(responseModel.message)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DefaultPreview() {
    MyApplicationTheme {
        PeopleDetailScreen(
            ResponseModel.Success(
                PeopleModel(
                    name = "Ki-Adi-Mundi",
                    height = "198",
                    mass = "82",
                    hair_color = "white",
                    skin_color = "pale",
                    eye_color = "yellow",
                    birth_year = "92BBY",
                    gender = "male",
                    homeworld = "https://swapi.dev/api/planets/43/",
                    films = emptyList(),
                    species = emptyList(),
                    vehicles = emptyList(),
                    starships = emptyList(),
                    url = "https://swapi.dev/api/people/52/"
                )
            ),
            ResponseModel.Success("Film 1, Film 2, Film 3"),
            ResponseModel.Success("Species 1, Species 2, Species 3"),
            ResponseModel.Success("Starship 1, Starship 2, Starship 3"),
            ResponseModel.Success("Vehicle 1, Vehicle 2, Vehicle 3"),
        )
    }
}
