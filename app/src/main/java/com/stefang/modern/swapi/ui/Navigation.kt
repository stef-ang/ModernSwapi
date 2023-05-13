/*
 * Copyright (C) 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.stefang.modern.swapi.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.stefang.modern.swapi.feature.startwars.PeopleDetailRoute
import com.stefang.modern.swapi.feature.startwars.PeopleListRoute
import java.net.URLDecoder
import java.net.URLEncoder

@Composable
fun MainNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = PeopleList.route) {
        composable(PeopleList.route) {
            PeopleListRoute(title = "Start Wars", onClickPeople = {
                val encodedUrl = URLEncoder.encode(it, "UTF-8")
                navController.navigate(PeopleDetail.route + encodedUrl) {
                    restoreState = true
                }
            })
        }
        composable(PeopleDetail.routeWithArg) {
            val url = it.arguments?.getString(PeopleDetail.urlArg)
            val decodedUrl = URLDecoder.decode(url, "UTF-8")
            PeopleDetailRoute(title = "Detail People", url = decodedUrl)
        }
    }
}

object PeopleList {
    const val route = "people_list"
}

object PeopleDetail {
    const val urlArg = "url_arg"
    const val route = "people_detail/"
    const val routeWithArg = "people_detail/{$urlArg}"
}
