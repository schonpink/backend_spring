package org.obrancova.librify.service.filter;

import com.google.common.collect.ImmutableList;
import org.obrancova.librify.model.Book;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class BorrowedBookFilter implements BookFilter {

    @Override
    public ImmutableList<Book> filter(List<Book> books) {
        List<Book> borrowedBooks = books.stream()
                .filter(book -> book.getBorrowed() != null && !isBookFree(book))
                .collect(Collectors.toList());
        return ImmutableList.copyOf(borrowedBooks);
    }

}