package sn.devion.crm.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import sn.devion.crm.enums.Permission;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;


/**
 * Group - Entity
 * @Data is a convenient shortcut annotation that bundles the features of @ToString, @EqualsAndHashCode, @Getter / @Setter and @RequiredArgsConstructor together
 * @Builder is a good choice if you want to design immutable objects but still want some flexibility without writing dozens of constructors manually
 * @NoArgsConstructor is a constructor generation annotation that generates a constructor with no parameters
 * @AllArgsConstructor is a constructor generation annotation that generates a constructor with 1 parameter for each field in your class
 * @Table is used to provide the details of the table that this entity will be mapped to
 * @Entity annotation specifies that the class is an entity and is mapped to a database table
 * @Index annotation is used to create an index for a column or group of columns
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table( name = "_group",
        indexes = {@Index(columnList = "name")},
        uniqueConstraints = {@UniqueConstraint(columnNames = {"name"})}
)
public class Group {

    @Id
    @GeneratedValue
    private UUID id;

    private String name;

    private String description;

    @Enumerated(EnumType.STRING)
    private Set<Permission> permissions;

    public Group(String name, String description, Set<Permission> permissions) {
        this.name = name;
        this.description = description;
        this.permissions = permissions;
    }

    public List<SimpleGrantedAuthority> getAuthorities() {
        var authorities = getPermissions()
                .stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toList());
        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name));
        return authorities;
    }
}
