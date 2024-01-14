package org.obrancova.librify.controller;

import lombok.RequiredArgsConstructor;
import org.obrancova.librify.model.Book;
import org.obrancova.librify.model.Borrowed;
import org.obrancova.librify.service.BookService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "${cors.allowedOrigins}")
@RequestMapping("/api/books")
public class BookController {
    private final BookService bookService;

    @GetMapping
    public List<Book> getAllBooks(@RequestParam(defaultValue = "${page.default-number}") int page,
                                  @RequestParam(defaultValue = "${pagination.default-page-size}") int size) {
        return bookService.getAllBooks(page, size);
    }

    @GetMapping("/free")
    public List<Book> getFreeBooks() {
        return bookService.getFreeBooks();
    }

    @GetMapping("/borrowed")
    public List<Book> getBorrowedBooks() {
        return bookService.getBorrowedBooks();
    }

    @PostMapping("/create")
    public void createBook(@RequestBody Book newBook) {
        bookService.createBook(newBook);
    }

    @PutMapping("/update/{bookId}")
    public void updateBook(@PathVariable("bookId") Long bookId, @RequestBody Book updatedBook) {
        updatedBook.setId(bookId);

        bookService.updateBook(updatedBook);
    }

    @PutMapping("/transaction/{bookId}")
    public void manageBookTransaction(@PathVariable("bookId") Long bookId, @RequestBody Borrowed borrowedInfo) {
        bookService.manageBookTransaction(bookId, borrowedInfo);
    }

    @DeleteMapping("/delete/{bookId}")
    public void deleteBook(@PathVariable("bookId") Long bookId) {
        bookService.deleteBookById(bookId);
    }
}