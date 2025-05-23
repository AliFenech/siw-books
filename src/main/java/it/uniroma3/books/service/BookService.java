package it.uniroma3.books.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.books.model.Book;
import it.uniroma3.books.repository.BookRepository;

@Service
public class BookService {
	
	@Autowired
    private BookRepository bookRepository;
	
	public Book findById(Long id) {
		return bookRepository.findById(id).get();
	}

	public Iterable<Book> findAll() {
		return bookRepository.findAll();
	}

	public void save(Book book) {
		bookRepository.save(book);
	}

	public Iterable<Book> findByYear(int year) {
		return bookRepository.findByYear(year);
	}

	public boolean existsByTitleAndYear(String title, Integer year) {
		return bookRepository.existsByTitleAndYear(title, year);
	}

}