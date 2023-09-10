package sn.devion.jwtauthenticationsample.services;


import org.springframework.data.domain.Page;
import sn.devion.jwtauthenticationsample.dtos.GroupDTO;
import sn.devion.jwtauthenticationsample.entities.Group;

import java.util.UUID;

public interface GroupService {
    GroupDTO findById(UUID id);
    Group findByName(String name);
    GroupDTO findDTOByName(String name);
    Page<GroupDTO> findAll(int index, int size, String sort, String orderBy);
    GroupDTO create(GroupDTO groupDTO);
    GroupDTO update(UUID id, GroupDTO groupDTO);
    void delete(UUID id);
}
