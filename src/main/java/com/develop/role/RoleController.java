package com.develop.role;

import io.swagger.annotations.Api;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/role")
@Api(tags = "Role Controller")
public class RoleController
{
    private final RoleService roleService;

    public RoleController(RoleService roleService)
    {
        this.roleService = roleService;
    }

    @PostMapping("/create")
    public ResponseEntity<Role> saveRole(@RequestBody RoleCreateRequest roleCreateRequest)
    {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/role/create").toUriString());
        return ResponseEntity.created(uri).body(roleService.saveRole(roleCreateRequest));
    }
}
