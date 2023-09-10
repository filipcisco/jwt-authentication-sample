package sn.devion.jwtauthenticationsample.services.impl;


import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import sn.devion.jwtauthenticationsample.dtos.GroupDTO;
import sn.devion.jwtauthenticationsample.entities.Group;
import sn.devion.jwtauthenticationsample.enums.Permission;
import sn.devion.jwtauthenticationsample.exceptions.PermissionParsingError;
import sn.devion.jwtauthenticationsample.repositories.GroupRepository;
import sn.devion.jwtauthenticationsample.services.GroupService;

import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {

    private final GroupRepository groupRepository;

    /**
     * Find a group by id
     * @param id group id
     * @return   group
     */
    @Override
    public GroupDTO findById(UUID id) {
        return groupRepository.findById(id)
                .map(this::convertToDTO)
                .orElseThrow(() -> new EntityNotFoundException("Group not found."));
    }

    /**
     * Find a group by name
     * @param name group name
     * @return     group
     */
    @Override
    public Group findByName(String name) {
        return groupRepository.findByName(name).orElse(null);
    }

    /**
     * Find a group dto by name
     * @param name group name
     * @return     group
     */
    @Override
    public GroupDTO findDTOByName(String name) {
        return groupRepository.findByName(name)
                .map(this::convertToDTO)
                .orElseThrow(() -> new EntityNotFoundException("Group not found."));
    }

    /**
     * Find all groups
     * @param index    page index
     * @param size     page size
     * @param sort     sort by
     * @param orderBy  order by
     * @return         page of groups
     */
    @Override
    public Page<GroupDTO> findAll(int index, int size, String sort, String orderBy) {
        val pageable = PageRequest.of(index, size, Sort.by(Sort.Direction.valueOf(orderBy), sort));
        val page = groupRepository.findAll(pageable).map(this::convertToDTO);
        return new PageImpl<>(page.getContent(), pageable, page.getTotalElements());
    }

    /**
     * Create a group
     * @param groupDTO group to create
     * @return         created group
     */
    @Override
    public GroupDTO create(GroupDTO groupDTO) {
        val group = convertToEntity(groupDTO);
        return convertToDTO(groupRepository.save(group));
    }

    /**
     * Update a group
     * @param id       group id
     * @param groupDTO group to update
     * @return         updated group
     */
    @Override
    public GroupDTO update(UUID id, GroupDTO groupDTO) {
        val group = groupRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Group not found."));
        group.setName(groupDTO.name());
        group.setDescription(groupDTO.description());
        val permissions = groupDTO.permissions()
                .stream()
                .map(permission -> {
                    try {
                        return Permission.valueOf(permission);
                    } catch (IllegalArgumentException e) {
                        throw new PermissionParsingError("Invalid Permission: " + permission);
                    }
                })
                .collect(Collectors.toSet());
        group.setPermissions(permissions);
        return convertToDTO(groupRepository.save(group));
    }

    /**
     * Delete a group
     * @param id group id
     */
    @Override
    public void delete(UUID id) {
        groupRepository.deleteById(id);
    }

    /**
     * Convert a group to a groupDTO
     * @param group group
     * @return      groupDTO
     */
    private GroupDTO convertToDTO(Group group) {
        return new GroupDTO(group.getId(), group.getName(), group.getDescription(), group.getPermissions().stream().map(Permission::name).collect(Collectors.toSet()));
    }

    /**
     * Convert a groupDTO to a group
     * @param groupDTO groupDTO
     * @return         group
     */
    private Group convertToEntity(GroupDTO groupDTO) {
        val permissions = groupDTO.permissions().stream().map(Permission::valueOf).collect(Collectors.toSet());
        return new Group(groupDTO.name(), groupDTO.description(), permissions);
    }
}
