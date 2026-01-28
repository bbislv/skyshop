package org.skypro.skyshop.model.search;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.UUID;

public interface Searchable {
    String getSearchTerm();
    String getContentType();
    String getName();
    UUID getId();

    @JsonIgnore
    default String getStringRepresentation() {
        return "\"" + getName() + " — " + getContentType() + "\"";
    }
}
