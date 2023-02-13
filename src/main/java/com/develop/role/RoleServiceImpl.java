package com.develop.role;

import com.develop.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class RoleServiceImpl implements RoleService
{
    private final RoleRepository roleRepository;

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    public RoleServiceImpl(RoleRepository roleRepository)
    {
        this.roleRepository = roleRepository;
    }

    @Override
    public Role saveRole(RoleCreateRequest roleCreateRequest)
    {
        Role role = new Role
        (
            null,
            roleCreateRequest.getName()
        );

        logger.info("Saving new role {} to the database", roleCreateRequest.getName());
        return roleRepository.save(role);
    }

    @Override
    @Cacheable(cacheNames = "roles")
    public List<Role> getRoles()
    {
        logger.info("Fetching all roles");
        return roleRepository.findAll();
    }
}
