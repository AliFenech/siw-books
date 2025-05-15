package it.uniroma3.books.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.books.model.Book;

public interface BookRepository extends CrudRepository<Book, Long> {

    // Esempi di query utili
    List<Book> findByTitle(String title);

    List<Book> findByYear(Integer year);
    

    
  
}
