package com.develop.role

import javax.persistence.*

@Entity
@Table(name = "roles")
data class Role
(
    @Id
    @GeneratedValue(strategy =  GenerationType.AUTO)
    val id: Long? = null,
    val name: String
)