package it.uniroma3.books.controller;



import java.io.IOException;
import java.security.Principal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import it.uniroma3.books.model.Autor;
import it.uniroma3.books.model.Book;
import it.uniroma3.books.model.Credentials;
import it.uniroma3.books.repository.AutorRepository;
import it.uniroma3.books.repository.BookRepository;
import it.uniroma3.books.repository.CredentialsRepository;


@Controller
public class BookController {

    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private AutorRepository autorRepository;
    @Autowired
    private CredentialsRepository credentialsRepository;

    @Transactional
    @GetMapping("admin/indexBook")
    public String listBooks(Model model) {
        model.addAttribute("books", this.bookRepository.findAll()); 
        return "admin/indexBook";
    }
    
    @GetMapping("/books")
    public String books(Model model, Principal principal) {
        model.addAttribute("books", bookRepository.findAll());

        if (principal != null) {
            model.addAttribute("username", principal.getName());
        }

        return "/books";
    }


    
    

    @GetMapping("/book/image/{id}")
    @ResponseBody
    public ResponseEntity<byte[]> getBookImage(@PathVariable Long id) {
        Optional<Book> book = bookRepository.findById(id);
        if (book.isPresent() && book.get().getImage() != null) {
            byte[] image = book.get().getImage();
            return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, "image/jpeg") // o "image/png" se serve
                .body(image);
        }
        return ResponseEntity.notFound().build();
    }

    @Transactional(readOnly = true)
    @GetMapping("/book/{id}")
	public String getBook(@PathVariable("id") Long id, Model model) {
	    Optional<Book> bookOpt = this.bookRepository.findById(id);
	    if (bookOpt.isPresent()) {
	        model.addAttribute("book", bookOpt.get());
	        return "book";  // la pagina dei dettagli libro
	    } else {
	        return "bookNotFound";  // pagina custom che crea l'errore "non trovato"
	    }
	}
    
    @GetMapping(value="admin/newBook")
    public String NewBook(Model model) {
    	model.addAttribute("book", new Book());
    	model.addAttribute("autori", autorRepository.findAll());
    	return "admin/newBook";
    }	
    @Transactional
    @PostMapping("/admin/book")
    public String newBook(@ModelAttribute Book book, 
                          @RequestParam("imageFile") MultipartFile imageFile,
                          Model model) throws IOException {
        // Recupera autore dal DB a partire dall'id settato da Thymeleaf
        if (book.getAutore() != null && book.getAutore().getId() != null) {
            Optional<Autor> autoreOpt = autorRepository.findById(book.getAutore().getId());
            if (autoreOpt.isPresent()) {
                book.setAutore(autoreOpt.get());
            } else {
                model.addAttribute("messaggioErrore", "Autore non valido");
                model.addAttribute("autori", autorRepository.findAll());
                return "admin/newBook";
            }
        } else {
            model.addAttribute("messaggioErrore", "Seleziona un autore");
            model.addAttribute("autori", autorRepository.findAll());
            return "admin/newBook";
        }

        if(!bookRepository.existsByTitleAndYear(book.getTitle(),book.getYear())) {
            if(!imageFile.isEmpty()) {
                book.setImage(imageFile.getBytes());
            }
            bookRepository.save(book);
            model.addAttribute("book", book);
            return "redirect:/book/" + book.getId();
        } else {
            model.addAttribute("messaggioErrore","Questo libro esiste già");
            model.addAttribute("autori", autorRepository.findAll());
            return "admin/newBook";
        }
    }

    
    @PostMapping("/admin/deleteBook/{id}")
    public String deleteBook(@PathVariable Long id) {
    	bookRepository.deleteById(id);
    	return "redirect:/admin/indexBook";
    }
    @Transactional
    @PostMapping("/admin/updateBook")
    public String updateBook(@ModelAttribute("book") Book updatedBook,
                             @RequestParam("file") MultipartFile file,
                             Model model) {

        Optional<Book> optionalExistingBook = bookRepository.findById(updatedBook.getId());

        if (optionalExistingBook.isPresent()) {
            Book existingBook = optionalExistingBook.get();

            // Mantieni l'immagine esistente se non è stato caricato un nuovo file
            if (!file.isEmpty()) {
                try {
                    existingBook.setImage(file.getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            // Aggiorna gli altri campi
            existingBook.setTitle(updatedBook.getTitle());
            existingBook.setYear(updatedBook.getYear());
            existingBook.setAutore(updatedBook.getAutore());

            bookRepository.save(existingBook);
        }

        return "redirect:/admin/indexBook";
    }

    
    @GetMapping("/admin/updateBook/{id}")
	public String updateBook(@PathVariable("id") Long id, Model model) {
    	model.addAttribute("autori", autorRepository.findAll());
	    Book book = bookRepository.findById(id)
	        .orElseThrow(() -> new IllegalArgumentException("ID libro non valido: " + id));
	    model.addAttribute("book", book);
	    return "admin/updateBook";  
	}
    
    

}
