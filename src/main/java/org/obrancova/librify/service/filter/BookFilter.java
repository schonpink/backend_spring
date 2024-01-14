package org.obrancova.librify.service.filter;

import com.google.common.collect.ImmutableList;
import org.obrancova.librify.model.Book;
import org.obrancova.librify.model.Borrowed;

import java.util.List;

public interface BookFilter {
    ImmutableList<Book> filter(List<Book> books);

    default boolean isBookFree(Book book) {
        Borrowed borrowed = book.getBorrowed();
        return borrowed == null || (borrowed.getFirstName() == null || borrowed.getFirstName().isEmpty()) &&
                (borrowed.getLastName() == null || borrowed.getLastName().isEmpty()) &&
                (borrowed.getFrom() == null || borrowed.getFrom().isEmpty());
    }

}