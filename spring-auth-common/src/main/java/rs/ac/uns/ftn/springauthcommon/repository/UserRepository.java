package rs.ac.uns.ftn.springauthcommon.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import rs.ac.uns.ftn.springauthcommon.entity.User;

public interface UserRepository extends MongoRepository<User, String> {

  Optional<User> findByEmail(String email);
}
