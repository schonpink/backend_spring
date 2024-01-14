package org.obrancova.librify.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.google.common.collect.ImmutableList;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.obrancova.librify.model.Book;
import org.obrancova.librify.model.Borrowed;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Repository
@RequiredArgsConstructor
public class BookRepositoryImpl implements BookRepository {
    @Value("${library.file.path}")
    private String libraryFilePath;

    public ImmutableList<Book> getAllBooks() {
        List<Book> books = loadBooksFromFile(libraryFilePath);
        return ImmutableList.copyOf(books);

    }

    public void createBook(Book newBook) {
        List<Book> books = loadBooksFromFile(libraryFilePath);

        Long newId = generateNewId(books);

        newBook.setId(newId);
        newBook.setName(newBook.getName());
        newBook.setAuthor(newBook.getAuthor());
        newBook.setBorrowed(newBook.getBorrowed());

        books.add(newBook);

        saveBooksToFile(books, libraryFilePath);
    }

    public void updateBook(Book updatedBook) {
        List<Book> books = loadBooksFromFile(libraryFilePath);

        Optional<Book> bookToUpdate = books.stream()
                .filter(book -> book.getId().equals(updatedBook.getId()))
                .findFirst();

        if (bookToUpdate.isPresent()) {
            bookToUpdate.ifPresent(book -> {
                book.setName(updatedBook.getName());
                book.setAuthor(updatedBook.getAuthor());
                book.setBorrowed(updatedBook.getBorrowed());
            });

            saveBooksToFile(books, libraryFilePath);
        } else {
            log.error("Book with ID {} not found", updatedBook.getId());
            throw new IllegalArgumentException("Book with ID " + updatedBook.getId() + " not found");
        }
    }

    public void manageBookTransaction(Long bookId, Borrowed borrowedInfo) {
        List<Book> books = loadBooksFromFile(libraryFilePath);

        boolean[] bookFound = {false};

        List<Book> updatedBooks = books.stream()
                .map(book -> {
                    if (book.getId().equals(bookId)) {
                        if (borrowedInfo != null) {
                            book.setBorrowed(borrowedInfo);
                        } else {
                            book.setBorrowed(new Borrowed());
                        }
                        bookFound[0] = true;
                    }
                    return book;
                })
                .collect(Collectors.toList());

        if (!bookFound[0]) {
            log.error("Book with ID {} not found", bookId);
            throw new IllegalArgumentException("Book with ID " + bookId + " not found");
        }

        saveBooksToFile(updatedBooks, libraryFilePath);
    }

    public void deleteBookById(Long bookId) {
        List<Book> books = loadBooksFromFile(libraryFilePath);

        boolean[] bookFound = {false};

        List<Book> updatedBooks = books.stream()
                .filter(book -> {
                    if (book.getId().equals(bookId)) {
                        bookFound[0] = true;
                        return false;
                    }
                    return true;
                })
                .collect(Collectors.toList());

        if (!bookFound[0]) {
            log.error("Book with ID {} not found", bookId);
            throw new IllegalArgumentException("Book with ID " + bookId + " not found");
        }

        saveBooksToFile(updatedBooks, libraryFilePath);
    }


    private static List<Book> loadBooksFromFile(String fileName) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);

        try {
            File file = new File(fileName);

            if (!file.exists()) {
                file.createNewFile();
                objectMapper.writeValue(file, new ArrayList<Book>());
            }

            CollectionType listType = objectMapper.getTypeFactory().constructCollectionType(List.class, Book.class);
            return objectMapper.readValue(file, listType);
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    private static void saveBooksToFile(List<Book> books, String fileName) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);

        try {
            File file = new File(fileName);
            objectMapper.writeValue(file, books);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Long generateNewId(List<Book> books) {
        return books.stream()
                .mapToLong(Book::getId)
                .max()
                .orElse(0L) + 1;
    }

}