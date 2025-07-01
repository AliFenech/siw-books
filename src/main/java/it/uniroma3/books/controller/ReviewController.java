package it.uniroma3.books.controller;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import it.uniroma3.books.model.Book;
import it.uniroma3.books.model.Credentials;
import it.uniroma3.books.model.Review;
import it.uniroma3.books.model.User;
import it.uniroma3.books.repository.BookRepository;
import it.uniroma3.books.repository.CredentialsRepository;
import it.uniroma3.books.repository.ReviewRepository;
import it.uniroma3.books.repository.UserRepository;

@Controller
public class ReviewController {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private CredentialsRepository credentialsRepository;

    @GetMapping("/scriviRecensione/{id}")
    public String mostraFormRecensione(@PathVariable("id") Long bookId, Model model, Principal principal, RedirectAttributes redirectAttributes) {
        Optional<Book> optBook = bookRepository.findById(bookId);
        if (optBook.isEmpty()) return "redirect:/books";

        Book book = optBook.get();
        User user = userRepository.findByName(principal.getName());

        if (reviewRepository.findByUserAndBook(user, book).isPresent()) {
            redirectAttributes.addFlashAttribute("messaggioErrore", "Hai già scritto una recensione per questo libro.");
            return "redirect:/books";
        }

        model.addAttribute("book", book);
        model.addAttribute("review", new Review());
        return "scriviRecensione";
    }


    // Salva la recensione
    @PostMapping("/scriviRecensione/{id}")
    public String salvaRecensione(@PathVariable("id") Long bookId,
                                  @RequestParam("title") String title,
                                  @RequestParam("content") String content,
                                  @RequestParam("vote") Integer vote,
                                  Principal principal) {

        Optional<Book> optBook = bookRepository.findById(bookId);
        if (optBook.isEmpty()) return "redirect:/books";

        Book book = optBook.get();
        User user = userRepository.findByName(principal.getName());

        if (reviewRepository.findByUserAndBook(user, book).isPresent())
            return "redirect:/books";

        Review review = new Review();
        review.setBook(book);
        review.setUser(user);
        review.setTitle(title);
        review.setContent(content);
        review.setVote(vote);

        reviewRepository.save(review);

        return "redirect:/books";
    }
    @Transactional(readOnly = true)
    @GetMapping("/mieRecensioni")
    public String mostraRecensioniUtente(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication instanceof AnonymousAuthenticationToken) {
            // Se non loggato, reindirizza o mostra la pagina index
            return "index";
        }

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userRepository.findByName(userDetails.getUsername());

        List<Review> recensioni = reviewRepository.findByUser(user);

        model.addAttribute("recensioni", recensioni);
        return "mieRecensioni";
    }

    @GetMapping("/recensioneDetails/{id}")
    public String recensioneDetails(@PathVariable Long id, Model model, Principal principal) {
        Optional<Review> recensioneOpt = reviewRepository.findById(id);

        if (recensioneOpt.isEmpty()) {
            return "redirect:/mieRecensioni"; 
        }

        Review recensione = recensioneOpt.get();
        model.addAttribute("recensione", recensione);

        if (principal != null) {
            Optional<Credentials> credOpt = credentialsRepository.findByUsername(principal.getName());
            if (credOpt.isPresent()) {
                model.addAttribute("userRole", credOpt.get().getRole());  // Es: "ADMIN"
            }
        }

        return "recensioneDetails";  
    }



    
 // Mostra form modifica recensione (solo titolo, voto, content)
    @GetMapping("/recensioni/modifica/{id}")
    public String mostraFormModifica(@PathVariable Long id, Model model) {
        Optional<Review> optReview = reviewRepository.findById(id);
        if (optReview.isEmpty()) {
            return "redirect:/mieRecensioni";  // se non trovato, torna indietro
        }
        model.addAttribute("recensione", optReview.get());
        return "modificaRecensione";  // nome della pagina Thymeleaf
    }

    // Salva modifiche recensione (POST)
    @PostMapping("/recensioni/modifica/{id}")
    public String salvaModifica(@PathVariable Long id, 
                                @ModelAttribute("recensione") Review recensioneModificata) {
        Optional<Review> optReview = reviewRepository.findById(id);
        if (optReview.isEmpty()) {
            return "redirect:/mieRecensioni";
        }
        Review review = optReview.get();

        // Aggiorna i campi modificabili
        review.setTitle(recensioneModificata.getTitle());
        review.setVote(recensioneModificata.getVote());
        review.setContent(recensioneModificata.getContent());

        reviewRepository.save(review);

        return "redirect:/recensioneDetails/" + id;
    }

    // Elimina recensione (POST)
    @PostMapping("/recensioni/elimina/{id}")
    public String eliminaRecensione(@PathVariable Long id) {
        reviewRepository.deleteById(id);
        return "redirect:/mieRecensioni";
    }
    
    @GetMapping("/admin/indexReview")
    public String getAllReview(Model model) {
    	model.addAttribute("recensioni", this.reviewRepository.findAll());
    	return "/admin/indexReview";
    	
    }
    @Transactional(readOnly = true)
    @GetMapping("/recensioniLibro/{id}")
    public String recensioniLibro(@PathVariable("id") Long bookId, Model model) {
        List<Review> recensioni = reviewRepository.findByBookId(bookId);
        model.addAttribute("recensioni", recensioni);
        model.addAttribute("bookId", bookId);
        return "recensioniLibro"; 
        
    }


}
