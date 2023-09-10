package sn.devion.crm.controllers;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sn.devion.crm.dtos.GroupDTO;
import sn.devion.crm.services.GroupService;

import java.util.UUID;

@RestController
@RequestMapping("/v1/groups")
@RequiredArgsConstructor
public class GroupController {

    private final GroupService groupService;

    @GetMapping("/{id}")
    public ResponseEntity<GroupDTO> getGroup(@PathVariable UUID id) {
        return ResponseEntity.ok(groupService.findById(id));
    }

    /**
     * Get all groups
     * @param index    page index
     * @param size     page size
     * @param sort     sort by
     * @param orderBy  order by
     * @return         page of groups
     */
    @GetMapping
    public ResponseEntity<Page<GroupDTO>> getAllGroups(@RequestParam(defaultValue = "0") int index,
                                                       @RequestParam(defaultValue = "15") int size,
                                                       @RequestParam(defaultValue = "createdAt" ) String sort,
                                                       @RequestParam(defaultValue = "DESC") String orderBy) {
        return ResponseEntity.ok(groupService.findAll(index, size, sort, orderBy));
    }

    /**
     * Create a group
     * @param groupDTO group to create
     * @return         created group
     */
    @PostMapping
    public ResponseEntity<GroupDTO> createGroup(@RequestBody GroupDTO groupDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(groupService.create(groupDTO));
    }

    /**
     * Update a group
     * @param id       group id
     * @param groupDTO group to update
     * @return         updated group
     */
    @PutMapping("/{id}")
    public ResponseEntity<GroupDTO> updateGroup(@PathVariable UUID id, @RequestBody GroupDTO groupDTO) {
        return ResponseEntity.ok(groupService.update(id, groupDTO));
    }

    /**
     * Delete a group
     * @param id group id
     * @return   no content
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGroup(@PathVariable UUID id) {
        groupService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
