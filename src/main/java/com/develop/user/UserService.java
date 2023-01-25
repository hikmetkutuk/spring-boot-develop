package com.develop.user;

import java.util.List;

public interface UserService
{
    User saveUser(UserRegisterRequest userRegisterRequest);
    User getUser(String username);
    List<User> getUsers();
    void addRoleToUser(String username, String roleName);
}
