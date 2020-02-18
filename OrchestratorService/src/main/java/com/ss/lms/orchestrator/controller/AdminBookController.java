package com.ss.lms.orchestrator.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.ss.lms.orchestrator.entity.Book;
import com.ss.lms.orchestrator.entity.Genre;

@RestController
@RequestMapping("/admin/book")
public class AdminBookController extends AdminController<Book> {
	
	@Autowired
	RestTemplate restTemplate;
	
	private String ADMIN_BOOK_URI = "http://localhost:8080/lms/books/";

	@Override
	public ResponseEntity<Book[]> getAll() {
		return restTemplate.getForEntity(ADMIN_BOOK_URI, Book[].class);
	}

	@Override
	public ResponseEntity<Book> getById(@PathVariable Integer id) {
		return restTemplate.getForEntity(ADMIN_BOOK_URI + id, Book.class);
	}

	@Override
	public ResponseEntity<Book> create(@RequestBody Book t) {
		return restTemplate.postForEntity(ADMIN_BOOK_URI, t, Book.class);
	}

	@Override
	public void update(@RequestBody Book t) {
		System.out.println("in orch update: " + t);
		restTemplate.put(ADMIN_BOOK_URI + t.getBookId(), t);
		
	}

	@Override
	public void delete(@PathVariable Integer id) {
		// TODO Auto-generated method stub
		restTemplate.delete(ADMIN_BOOK_URI + id);
		
	}
	
	@PutMapping("/{bookId}/authors")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void updateBookAuthors(Book book) {
		restTemplate.put(ADMIN_BOOK_URI + book.getBookId() + "/authors", book);
	}
	
	@PutMapping("/{bookId}/genres")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void updateBookGenres(Book book) {
		restTemplate.put(ADMIN_BOOK_URI + book.getBookId() + "/authors", book);
	}

}
