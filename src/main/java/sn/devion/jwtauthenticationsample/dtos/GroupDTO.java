package sn.devion.jwtauthenticationsample.dtos;

import java.util.Set;
import java.util.UUID;

public record GroupDTO (
    UUID id,
    String name,
    String description,
    Set<String> permissions
){}
