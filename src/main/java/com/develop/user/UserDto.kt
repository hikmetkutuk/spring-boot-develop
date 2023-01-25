package com.develop.user

data class UserDto
(
      val mail: String,
      val username: String,
      val roles: Collection<String>
)
