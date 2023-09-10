package sn.devion.crm.dtos;

import java.util.UUID;

public record UserDTO(
        UUID id,
        String firstname,
        String lastname,
        String username,
        String email,
        String password,
        boolean enabled,
        boolean expired,
        String groupName,
        String groupAuthorities
) {}
