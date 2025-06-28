package it.uniroma3.books.controller;

import java.io.IOException;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import it.uniroma3.books.model.Autor;
import it.uniroma3.books.repository.AutorRepository;


@Controller
public class AutorController {
	@Autowired
	private AutorRepository autorRepository;
	
	@GetMapping(value="/admin/formNewAutor")
	public String formNewAutor(Model model) {
		model.addAttribute("autor", new Autor());
		return "admin/formNewAutor";
	}
	
	@GetMapping(value="/admin/indexAutor")
	public String indexAutor() {
		return "admin/indexAutor";
	}
	
	
	
	@PostMapping("/admin/autor")
	public String newAutor(@ModelAttribute Autor autor,
	                       @RequestParam("imageFile") MultipartFile imageFile,
	                       Model model) throws IOException {
	    if (!autorRepository.existsByNameAndSurname(autor.getName(), autor.getSurname())) {

	        if (!imageFile.isEmpty()) {
	            autor.setImage(imageFile.getBytes());
	        }

	        autorRepository.save(autor);
	        model.addAttribute("autor", autor);
	        return "redirect:/autor/" + autor.getId();
	    } else {
	        model.addAttribute("messaggioErrore", "Questo autore esiste già");
	        return "admin/formNewAutor";
	    }
	}
	
	@GetMapping("/autor/{id}")
	public String getAutor(@PathVariable("id") Long id, Model model) {
	    Optional<Autor> autorOpt = this.autorRepository.findById(id);
	    if (autorOpt.isPresent()) {
	        model.addAttribute("autor", autorOpt.get());
	        return "autor";  // la pagina dei dettagli autore
	    } else {
	        return "autorNotFound";  // pagina custom che crea l'errore "non trovato"
	    }
	}

	@GetMapping("/autor/image/{id}")
	@ResponseBody
	public ResponseEntity<byte[]> getAutorImage(@PathVariable Long id) {
	    Optional<Autor> autor = autorRepository.findById(id);
	    if (autor.isPresent() && autor.get().getImage() != null) {
	        byte[] image = autor.get().getImage();
	        return ResponseEntity.ok()
	            .header(HttpHeaders.CONTENT_TYPE, "image/jpeg") // o image/png
	            .body(image);
	    }
	    return ResponseEntity.notFound().build();
	}

	@GetMapping("/autor")
	public String getAutor(Model model) {
		model.addAttribute("autor", this.autorRepository.findAll());
		return "autors";
	}
	
	
	
	
	
}
