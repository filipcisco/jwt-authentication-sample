package sn.devion.jwtauthenticationsample.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sn.devion.jwtauthenticationsample.entities.Group;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface GroupRepository extends JpaRepository<Group, UUID> {
    Optional<Group> findByName(String name);
}
