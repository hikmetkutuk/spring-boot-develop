package com.develop.book

import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import org.springframework.data.mongodb.core.mapping.Document
import java.math.BigDecimal
import javax.persistence.*

@Document(collection = "books")
@JsonDeserialize
data class Book
(
    @Id
    val id: String?,
    val name: String,
    val author: String,
    val description: String,
    val price: BigDecimal
)