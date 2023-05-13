package com.stefang.modern.swapi.core.data.model

data class PaginationResponse<T>(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<T>?
)
