package org.skypro.skyshop.model.article;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.skypro.skyshop.model.search.Searchable;
import java.util.UUID;

public class Article implements Searchable {
    private final UUID id;
    private final String name;
    private final String text;

    public Article(String name, String text) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.text = text;
    }

    @Override
    public String getName() {
        return name;
    }

    public String getText() {
        return text;
    }

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public String toString() {
        return name + "\n" + text;
    }

    @JsonIgnore
    @Override
    public String getSearchTerm() {
        return getName() + " " + getText();
    }

    @JsonIgnore
    @Override
    public String getContentType() {
        return "ARTICLE";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Article article = (Article) o;
        return name.equals(article.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
