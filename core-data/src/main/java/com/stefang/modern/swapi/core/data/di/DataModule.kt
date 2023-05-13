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

package com.stefang.modern.swapi.core.data.di

import com.stefang.modern.swapi.core.data.BuildConfig
import com.stefang.modern.swapi.core.data.SwapiApi
import com.stefang.modern.swapi.core.data.repository.SwapiRepository
import com.stefang.modern.swapi.core.data.repository.SwapiRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    @Singleton
    @Binds
    fun bindsSwapiRepository(repository: SwapiRepositoryImpl): SwapiRepository

    companion object {
        private const val BASE_URL = "https://swapi.dev/api/"

        @Provides
        @Singleton
        fun okHttpCallFactory(): Call.Factory = OkHttpClient.Builder()
            .addInterceptor(
                HttpLoggingInterceptor()
                    .apply {
                        if (BuildConfig.DEBUG) {
                            setLevel(HttpLoggingInterceptor.Level.BODY)
                        }
                    },
            )
            .build()

        @Provides
        @Singleton
        fun provideRetrofit(okhttpCallFactory: Call.Factory): Retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .callFactory(okhttpCallFactory)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        @Provides
        @Singleton
        fun provideSwapiApi(retrofit: Retrofit) = retrofit.create(SwapiApi::class.java)
    }
}
