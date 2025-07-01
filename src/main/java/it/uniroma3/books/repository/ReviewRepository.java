package it.uniroma3.books.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.books.model.Book;
import it.uniroma3.books.model.Review;
import it.uniroma3.books.model.User;

public interface ReviewRepository extends CrudRepository<Review, Long> {

    // Trova tutte le recensioni di un certo libro
    List<Review> findByBook(Book book);

    // Trova tutte le recensioni fatte da un certo utente
    List<Review> findByUser(User user);

    // Trova una recensione specifica fatta da un utente su un libro
    Optional<Review> findByUserAndBook(User user, Book book);
    //Trova tutte le recensioni di uno specifico libro
    List<Review> findByBookId(Long bookId);
}
