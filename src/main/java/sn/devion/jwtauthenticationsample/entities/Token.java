package sn.devion.crm.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sn.devion.crm.enums.TokenType;


/**
 * This class represents a token mostly jwt used for authorization
 * A token can be used to access a protected resource
 * A token can be revoked
 * A token can be expired
 *
 * @Data is a convenient shortcut annotation that bundles the features of @ToString, @EqualsAndHashCode, @Getter / @Setter and @RequiredArgsConstructor together
 * @Builder is a good choice if you want to design immutable objects but still want some flexibility without writing dozens of constructors manually
 * @NoArgsConstructor is a constructor generation annotation that generates a constructor with no parameters
 * @AllArgsConstructor is a constructor generation annotation that generates a constructor with 1 parameter for each field in your class
 * @Table is used to provide the details of the table that this entity will be mapped to
 * @Entity annotation specifies that the class is an entity and is mapped to a database table
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "_token")
@Entity
public class Token {

  @Id
  @GeneratedValue
  public Integer id;

  @Column(unique = true)
  public String token;

  @Enumerated(EnumType.STRING)
  public TokenType tokenType = TokenType.BEARER;

  public boolean revoked;

  public boolean expired;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  public User user;
}
