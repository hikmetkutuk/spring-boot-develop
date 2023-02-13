package com.develop.book

import java.math.BigDecimal

data class BookCreateRequest
(
    val name: String,
    val author: String,
    val description: String,
    val price: BigDecimal
)