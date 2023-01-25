package com.develop.user

import com.develop.role.Role

data class UserRegisterRequest
(
    val mail: String,
    val username: String,
    var password: String,
    val roles: Collection<Role> = ArrayList()
)
