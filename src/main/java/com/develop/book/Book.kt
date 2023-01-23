package com.develop.book

import javax.persistence.*

@Entity
@Table(name = "books")
data class Book
(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long? = null,

    val title: String,

    val author: String
)