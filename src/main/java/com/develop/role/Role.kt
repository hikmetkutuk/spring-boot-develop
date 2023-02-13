package com.develop.role

import javax.persistence.*

@Entity
@Table(name = "roles")
data class Role
(
    @Id
    @GeneratedValue(strategy =  GenerationType.AUTO)
    @Column(name = "id", nullable = true)
    val id: Long? = null,

    @Column(name = "name", nullable = false)
    val name: String
)
