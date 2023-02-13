package com.develop.role;

import java.util.List;

public interface RoleService
{
    Role saveRole(RoleCreateRequest roleCreateRequest);
    List<Role> getRoles();
}
