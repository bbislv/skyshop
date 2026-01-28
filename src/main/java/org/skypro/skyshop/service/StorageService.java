package org.skypro.skyshop.service;

import org.skypro.skyshop.model.product.DiscountedProduct;
import org.skypro.skyshop.model.product.SimpleProduct;
import org.springframework.stereotype.Service;
import org.skypro.skyshop.model.article.Article;
import org.skypro.skyshop.model.product.Product;
import org.skypro.skyshop.model.search.Searchable;
import java.util.*;

@Service
public class StorageService {
    private Map<UUID, Product> products = new HashMap<>();
    private Map<UUID, Article> articles = new HashMap<>();

    public StorageService() {

        SimpleProduct pr1 = new SimpleProduct("Книга", 500);
        DiscountedProduct pr2 = new DiscountedProduct("Футболка", 2000, 25);
        this.products.put(pr1.getId(), pr1);
        this.products.put(pr2.getId(), pr2);

        Article ar1 = new Article("Книга и ее поиск", "Выбирайте книги по жанру и автору.");
        Article ar2 = new Article("Уход за одеждой", "Стирайте футболки при 30 градусах.");
        this.articles.put(ar1.getId(), ar1);
        this.articles.put(ar2.getId(), ar2);
    }

    public Collection<Product> getAllProducts() {
        return products.values();
    }

    public Collection<Article> getAllArticles() {
        return articles.values();
    }

    public Product getProduct(UUID id) {
        return products.get(id);
    }

    public Article getArticle(UUID id) {
        return articles.get(id);
    }

    public void addProduct(Product product) {
        products.put(product.getId(), product);
    }

    public void addArticle(Article article) {
        articles.put(article.getId(), article);
    }

    public Collection<Searchable> getAllSearchable() {
        List<Searchable> all = new ArrayList<>();
        all.addAll(products.values());
        all.addAll(articles.values());
        return all;
    }

    public Optional<Product> getProductById(UUID id) {
        return Optional.ofNullable(products.get(id));
    }
}
