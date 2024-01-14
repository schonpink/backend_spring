package org.obrancova.librify.model.action;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.obrancova.librify.model.Book;
import org.obrancova.librify.model.user.User;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Action {
    private ActionType actionType;
    private LocalDate timestamp;
    private User user;
    private Book book;
}
