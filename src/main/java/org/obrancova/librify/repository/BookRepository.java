package org.obrancova.librify.repository;

import com.google.common.collect.ImmutableList;
import org.obrancova.librify.model.Book;
import org.obrancova.librify.model.Borrowed;
import org.springframework.stereotype.Repository;

import java.io.IOException;

@Repository
public interface BookRepository {

    ImmutableList<Book> getAllBooks();

    void createBook(Book newBook);

    void updateBook(Book updatedBook);

    void manageBookTransaction(Long bookId, Borrowed borrowedInfo);

    void deleteBookById(Long bookId);
}