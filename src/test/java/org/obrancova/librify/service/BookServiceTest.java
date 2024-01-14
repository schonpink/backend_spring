package org.obrancova.librify.service;

import com.google.common.collect.ImmutableList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.obrancova.librify.model.Book;
import org.obrancova.librify.model.Borrowed;
import org.obrancova.librify.repository.BookRepositoryImpl;
import org.obrancova.librify.service.filter.BorrowedBookFilter;
import org.obrancova.librify.service.filter.FreeBookFilter;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {
    @Mock
    private BookRepositoryImpl bookRepositoryImpl;

    @Mock
    private FreeBookFilter freeBookFilter;

    @Mock
    private BorrowedBookFilter borrowedBookFilter;

    @InjectMocks
    private BookService bookService;

    private Book book;
    private List<Book> books;
    private List<Book> allBooks;

    @BeforeEach
    void setUp() {
        book = Book.builder()
                .id(1L)
                .name("Test Book")
                .author("Test Author")
                .borrowed(Borrowed.builder().build())
                .build();

        books = Arrays.asList(
                Book.builder().id(1L).name("Book 1").author("Author 1").borrowed(Borrowed.builder().build()).build(),
                Book.builder().id(2L).name("Book 2").author("Author 2").borrowed(Borrowed.builder().build()).build()
        );

        allBooks = Arrays.asList(
                Book.builder().id(1L).name("Book 1").borrowed(Borrowed.builder().build()).build(),
                Book.builder().id(2L).name("Book 2").borrowed(Borrowed.builder().firstName("John").lastName("Doe").from("12.12.2023").build()).build(),
                Book.builder().id(3L).name("Book 3").build()
        );

        bookService = new BookService(bookRepositoryImpl, freeBookFilter, borrowedBookFilter);
    }

    @Test
    void testGetAllBooks() {
        ImmutableList<Book> allBooks = ImmutableList.copyOf(books);
        when(bookRepositoryImpl.getAllBooks()).thenReturn(allBooks);

        List<Book> fetchedBooks = bookService.getAllBooks(0, 2);

        assertEquals(allBooks.subList(0, 2), fetchedBooks);
    }

    @Test
    void shouldReturnBooksInRange() {
        int pageNumber = 1;
        int pageSize = 1;

        when(bookRepositoryImpl.getAllBooks()).thenReturn(ImmutableList.copyOf(allBooks));

        List<Book> result = bookService.getAllBooks(pageNumber, pageSize);

        assertEquals(1, result.size());
    }

    @Test
    void testCreateBook() {
        bookService.createBook(book);

        verify(bookRepositoryImpl).createBook(book);
    }

    @Test
    void shouldReturnFreeBooks() {

        when(bookRepositoryImpl.getAllBooks()).thenReturn(ImmutableList.copyOf(allBooks));
        when(freeBookFilter.filter(allBooks)).thenReturn(ImmutableList.copyOf(books));

        List<Book> result = bookService.getFreeBooks();

        assertEquals(2, result.size());
    }

    @Test
    void shouldReturnBorrowedBooks() {

        when(bookRepositoryImpl.getAllBooks()).thenReturn(ImmutableList.copyOf(allBooks));
        when(borrowedBookFilter.filter(allBooks)).thenReturn(ImmutableList.copyOf(books));

        List<Book> result = bookService.getBorrowedBooks();

        assertEquals(2, result.size());
    }

    @Test
    void testUpdateBook() {
        Book updatedBook = Book.builder()
                .id(1L)
                .name("Updated Book")
                .author("Updated Author")
                .borrowed(Borrowed.builder().build())
                .build();

        doNothing().when(bookRepositoryImpl).updateBook(updatedBook);

        bookService.updateBook(updatedBook);

        verify(bookRepositoryImpl, times(1)).updateBook(updatedBook);
    }

    @Test
    void testManageBookTransaction() {
        Long bookId = 1L;
        Borrowed borrowedInfo = Borrowed.builder().firstName("John").lastName("Doe").from("12.12.2023").build();

        bookService.manageBookTransaction(bookId, borrowedInfo);

        verify(bookRepositoryImpl).manageBookTransaction(eq(bookId), eq(borrowedInfo));
    }

    @Test
    void testDeleteBookById() {
        Long bookIdToDelete = 1L;

        doNothing().when(bookRepositoryImpl).deleteBookById(bookIdToDelete);

        bookService.deleteBookById(bookIdToDelete);

        verify(bookRepositoryImpl, times(1)).deleteBookById(bookIdToDelete);
    }

}