package it.uniroma3.books.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

public class BookController {
	
	
	
	
	
	
	
	
	
	
	
	@GetMapping("/")
	  public String home(Model model) {
		  return "index.html";
	  }
}
