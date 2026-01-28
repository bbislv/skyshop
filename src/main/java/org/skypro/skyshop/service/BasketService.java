package org.skypro.skyshop.service;

import org.springframework.stereotype.Service;
import org.skypro.skyshop.dto.BasketItem;
import org.skypro.skyshop.dto.UserBasket;
import org.skypro.skyshop.model.basket.ProductBasket;
import org.skypro.skyshop.model.product.Product;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BasketService {
    private final StorageService storageService;
    private final ProductBasket productBasket;

    public BasketService(StorageService storageService, ProductBasket productBasket) {
        this.storageService = storageService;
        this.productBasket = productBasket;
    }

    public void addProductToBasket(UUID id) {
        storageService.getProductById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));
        productBasket.addProduct(id);
    }

    public UserBasket getUserBasket() {
        Map<UUID, Integer> products = productBasket.getProducts();

        List<BasketItem> items = products.entrySet().stream()
                .map(entry -> {
                    Product product = storageService.getProduct(entry.getKey());
                    return new BasketItem(product, entry.getValue());
                })
                .collect(Collectors.toList());

        return new UserBasket(items);
    }
}
