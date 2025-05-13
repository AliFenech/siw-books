package it.uniroma3.books.repository;

import java.util.Optional;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.books.model.User;
import it.uniroma3.books.model.Role;

public interface UserRepository extends CrudRepository<User, Long> {

    Optional<User> findByUsername(String username);

    List<User> findByRole(Role role);
}
