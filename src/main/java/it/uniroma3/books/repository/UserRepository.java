package it.uniroma3.books.repository;

import java.util.Optional;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.books.model.User;


public interface UserRepository extends CrudRepository<User, Long> {

    User findByName(String name);
    User findByEmail(String email);
   


    
}
