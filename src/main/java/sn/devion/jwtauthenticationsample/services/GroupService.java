package sn.devion.crm.services;


import org.springframework.data.domain.Page;
import sn.devion.crm.dtos.GroupDTO;
import sn.devion.crm.entities.Group;

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
