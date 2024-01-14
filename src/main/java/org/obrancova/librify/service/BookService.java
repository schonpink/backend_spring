package org.obrancova.librify.service;

import com.google.common.collect.ImmutableList;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.obrancova.librify.model.Book;
import org.obrancova.librify.model.Borrowed;
import org.obrancova.librify.repository.BookRepositoryImpl;
import org.obrancova.librify.service.filter.BorrowedBookFilter;
import org.obrancova.librify.service.filter.FreeBookFilter;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepositoryImpl bookRepositoryImpl;
    private final FreeBookFilter freeBookFilter;
    private final BorrowedBookFilter borrowedBookFilter;

    public List<Book> getAllBooks(int pageNumber, int pageSize) {
        ImmutableList<Book> allBooks = bookRepositoryImpl.getAllBooks();

        int fromIndex = pageNumber * pageSize;
        int toIndex = Math.min(fromIndex + pageSize, allBooks.size());

        log.info("Returning {} books", toIndex - fromIndex);
        return allBooks.subList(fromIndex, toIndex);
    }

    public ImmutableList<Book> getFreeBooks() {
        log.info("Getting list of free books");
        return freeBookFilter.filter(bookRepositoryImpl.getAllBooks());
    }

    public ImmutableList<Book> getBorrowedBooks() {
        log.info("Getting list of borrowed books");
        return borrowedBookFilter.filter(bookRepositoryImpl.getAllBooks());
    }

    public void createBook(Book newBook) {
        log.info("Creating a new book: {}", newBook);
        bookRepositoryImpl.createBook(newBook);
        log.info("New book created successfully");
    }

    public void updateBook(Book updatedBook) {
        log.info("Updating book with ID: {}", updatedBook.getId());
        bookRepositoryImpl.updateBook(updatedBook);
        log.info("Book with ID {} has been updated successfully", updatedBook.getId());
    }

    public void manageBookTransaction(Long bookId, Borrowed borrowedInfo) {
        log.info("Managing book transaction with ID: {}", bookId);
        bookRepositoryImpl.manageBookTransaction(bookId, borrowedInfo);
        log.info("Book transaction with ID {} has been completed successfully", bookId);
    }

    public void deleteBookById(Long bookId) {
        log.info("Deleting book with ID: {}", bookId);
        bookRepositoryImpl.deleteBookById(bookId);
        log.info("Book with ID {} has been deleted successfully", bookId);
    }
}