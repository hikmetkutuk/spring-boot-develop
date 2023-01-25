package com.develop.user

import com.develop.role.Role
import javax.persistence.*

@Entity
@Table(name = "users")
data class User
(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long? = null,
    val mail: String,
    val username: String,
    @Lob
    val password: String,
    @ManyToMany(cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    var roles: Collection<Role>? = ArrayList()
)
