package it.uniroma3.books.repository;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;




import org.springframework.data.repository.CrudRepository;

import it.uniroma3.books.model.Autor;


public interface AutorRepository extends CrudRepository<Autor, Long> {

    // Esempi di query utili (Spring Data crea automaticamente l'implementazione)
    
    List<Autor> findByName(String name);

    List<Autor> findBySurname(String surname);

    List<Autor> findByNationality(String nationality);

    List<Autor> findByDateOfBirth(LocalDate dateOfBirth);

    List<Autor> findByDateOfDeath(LocalDate dateOfDeath);
    
    public boolean existsByNameAndSurname(String name, String surname);	

	

}
