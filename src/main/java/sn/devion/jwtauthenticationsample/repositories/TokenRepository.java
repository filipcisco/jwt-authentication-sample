package sn.devion.jwtauthenticationsample.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import sn.devion.jwtauthenticationsample.entities.Token;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TokenRepository extends JpaRepository<Token, Integer> {

  @Query(value = """
      select t from Token t inner join User u\s
      on t.user.id = u.id\s
      where u.id = :id and (t.expired = false or t.revoked = false)\s
      """)
  List<Token> findAllValidTokenByUser(UUID id);

  List<Token> findAllByExpiredIsFalseAndRevokedIsFalseAndUserId(UUID userId);

  Optional<Token> findByToken(String token);
}
