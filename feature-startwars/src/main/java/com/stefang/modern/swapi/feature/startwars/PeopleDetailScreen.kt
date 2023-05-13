package com.stefang.modern.swapi.feature.startwars

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
    val films by viewModel.films.collectAsStateWithLifecycle()
    val species by viewModel.species.collectAsStateWithLifecycle()
    val starships by viewModel.starships.collectAsStateWithLifecycle()
    val vehicles by viewModel.vehicles.collectAsStateWithLifecycle()

    LaunchedEffect(url) {
        url?.let(viewModel::fetchDetail)
    }

    LaunchedEffect(peopleModel) {
        viewModel.fetchAttributes(peopleModel)
    }

    peopleModel?.let { people ->
        ScaffoldScreen(
            title = title
        ) {
            Column(modifier = Modifier.padding(start = 16.dp, top = it.calculateTopPadding(), end = 16.dp)) {
                PeopleDetailScreen(people, films, species, starships, vehicles)
            }
        }
    }
}

@Composable
fun PeopleDetailScreen(
    peopleModel: PeopleModel,
    films: String,
    species: String,
    starships: String,
    vehicles: String
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        RowField("Name:", peopleModel.name)
        Row {
            RowField("Height:", peopleModel.height)
            Spacer(modifier = Modifier.size(16.dp))
            RowField("Mass:", peopleModel.mass)
        }
        Text(text = "Color")
        Row {
            RowField("Hair:", peopleModel.hair_color)
            Spacer(modifier = Modifier.size(16.dp))
            RowField("Skin:", peopleModel.skin_color)
            Spacer(modifier = Modifier.size(16.dp))
            RowField("Eye:", peopleModel.eye_color)
        }
        RowField("Gender:", peopleModel.gender)
        RowField("Birth Year:", peopleModel.birth_year)
        ColumnField("Films:", films)
        ColumnField("Species:", species)
        ColumnField("Starships:", starships)
        ColumnField("Vehicles:", vehicles)
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
private fun ColumnField(label: String, value: String) {
    Column(modifier = Modifier.padding(bottom = 12.dp)) {
        Text(text = label)
        Text(
            text = value.ifBlank { "-" },
            color = DeepPurple500,
            fontWeight = FontWeight.SemiBold,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DefaultPreview() {
    MyApplicationTheme {
        PeopleDetailScreen(
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
            ),
            "Film 1, Film 2, Film 3",
            "Species 1, Species 2, Species 3",
            "Starship 1, Starship 2, Starship 3",
            "Vehicle 1, Vehicle 2, Vehicle 3",
        )
    }
}
