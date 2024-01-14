package org.obrancova.librify.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "Name",
        "Author",
        "Borrowed"
})
@JsonRootName("Book")
public class Book {
    @JsonProperty("id")
    private Long id;

    @JsonProperty("Name")
    private String name;

    @JsonProperty("Author")
    private String author;

    @JsonProperty("Borrowed")
    private Borrowed borrowed;
}