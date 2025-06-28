package it.uniroma3.books.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.books.model.Autor;
import it.uniroma3.books.repository.AutorRepository;


@Service
public class AutorService {
	@Autowired
	private AutorRepository autorRepository;
	
	public Autor findById(Long id) {
		return autorRepository.findById(id).get();
	}

	public Iterable<Autor> findAll() {
		return autorRepository.findAll();
	}

	public void save(Autor autor) {
		autorRepository.save(autor);
	}

	
}
