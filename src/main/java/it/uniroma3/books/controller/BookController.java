package it.uniroma3.books.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import it.uniroma3.books.service.BookService;	

@Controller
public class BookController {
	
	@Autowired 
	private BookService bookService;
		
	
	@GetMapping("/book")
	public String showBooks(Model model) {
		model.addAttribute("books", this.bookService.findAll());
		return "books.html";
	}
	
	
	
	
	
	
	
	
	
	


}
