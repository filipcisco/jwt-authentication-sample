package sn.devion.jwtauthenticationsample.controllers;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sn.devion.jwtauthenticationsample.dtos.UserDTO;
import sn.devion.jwtauthenticationsample.services.UserService;

import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * Get a user by id
     * @param id user id
     * @return   user
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable UUID id) {
        UserDTO userDTO = userService.findById(id);
        return ResponseEntity.ok(userDTO);
    }

    /**
     * Get all users
     * @param page  page index
     * @param size  page size
     * @param sort  sort by
     * @param order order by
     * @return      page of users
     */
    @GetMapping
    public ResponseEntity<Page<UserDTO>> getAllUsers(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "sort", defaultValue = "id") String sort,
            @RequestParam(name = "order", defaultValue = "asc") String order) {
        return ResponseEntity.ok(userService.findAll(page, size, sort, order));
    }

    /**
     * Create a user
     * @param userDTO user
     * @return        created user
     */
    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(userDTO));
    }

    /**
     * Update a user
     * @param id      user id
     * @param userDTO user
     * @return        updated user
     */
    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable UUID id, @RequestBody UserDTO userDTO) {
        return ResponseEntity.ok(userService.updateUser(id, userDTO));
    }

    /**
     * Delete a user
     * @param id user id
     * @return   void
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
