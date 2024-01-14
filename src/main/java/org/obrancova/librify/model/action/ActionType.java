package org.obrancova.librify.model.action;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum ActionType {
    FREE("Free"),
    CREATE("Create"),
    UPDATE("Update"),
    BORROW("Borrow"),
    RETURN("Return"),
    DELETE("Delete");

    @Getter
    private final String description;
}