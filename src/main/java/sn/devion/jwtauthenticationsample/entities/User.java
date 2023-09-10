package sn.devion.crm.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

/**
 * User - Entity
 * @Data is a convenient shortcut annotation that bundles the features of @ToString, @EqualsAndHashCode, @Getter / @Setter and @RequiredArgsConstructor together
 * @Builder is a good choice if you want to design immutable objects but still want some flexibility without writing dozens of constructors manually
 * @NoArgsConstructor is a constructor generation annotation that generates a constructor with no parameters
 * @AllArgsConstructor is a constructor generation annotation that generates a constructor with 1 parameter for each field in your class
 * @Table is used to provide the details of the table that this entity will be mapped to
 * @Entity annotation specifies that the class is an entity and is mapped to a database table
 * @Index annotation is used to create an index for a column or group of columns
 * @UniqueConstraint annotation is used to specify that a unique constraint is to be included in the generated DDL for a primary or secondary table
 * @EntityListeners annotation is used to configure callback listeners on an entity bean class
 * @JsonIgnoreProperties annotation is used to ignore the specified logical properties in JSON serialization and deserialization
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(value = {"createdAt", "updatedAt", "createdBy", "updatedBy"})
@Entity
@Table( name = "_user",
        indexes = {@Index(columnList = "username"), @Index(columnList = "email")},
        uniqueConstraints = {@UniqueConstraint(columnNames = {"username", "email"})}
)
@EntityListeners(AuditingEntityListener.class)
public class User implements UserDetails {

  @Id
  @GeneratedValue
  private UUID id;

  private String firstname;

  private String lastname;

  private String username;

  private String email;

  private String password;

  private boolean enabled = true;

  private boolean expired = false;

  @ManyToOne
  private Group group;

  @OneToMany(mappedBy = "user")
  private List<Token> tokens = new ArrayList<>();

  @Column(name = "created_at", nullable = false, updatable = false)
  @CreatedDate
  private Date createdAt;

  @Column(name = "updated_at", nullable = false)
  @LastModifiedDate
  private Date updatedAt;

  @CreatedBy
  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  private UUID createdBy;

  @LastModifiedBy
  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  private UUID updatedBy;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return group.getAuthorities();
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return username;
  }

  @Override
  public boolean isAccountNonExpired() {
    return !expired;
  }

  @Override
  public boolean isAccountNonLocked() {
    return !enabled;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return enabled;
  }
}
