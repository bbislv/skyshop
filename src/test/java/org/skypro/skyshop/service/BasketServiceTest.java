package org.skypro.skyshop.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.skypro.skyshop.dto.BasketItem;
import org.skypro.skyshop.dto.UserBasket;
import org.skypro.skyshop.exception.NoSuchProductException;
import org.skypro.skyshop.model.basket.ProductBasket;
import org.skypro.skyshop.model.product.Product;
import org.skypro.skyshop.model.product.SimpleProduct;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Тесты для BasketService")
class BasketServiceTest {

    @Mock
    private StorageService storageService;

    @Mock
    private ProductBasket productBasket;

    @InjectMocks
    private BasketService basketService;

    private UUID productId;
    private Product testProduct;

    @BeforeEach
    void setUp() {
        productId = UUID.randomUUID();
        testProduct = new SimpleProduct("Тестовый товар", 500);
    }

    @Test
    @DisplayName("Добавление несуществующего товара выбрасывает исключение")
    void testAddNonExistentProductThrowsException() {
        // Arrange
        when(storageService.getProductById(productId))
                .thenReturn(Optional.empty());

        // Act  Assert
        NoSuchProductException exception = assertThrows(
                NoSuchProductException.class,
                () -> basketService.addProductToBasket(productId)
        );
        assertEquals("404_Product", exception.getCode());
        assertEquals("Товар не найден", exception.getMessage());
        verify(productBasket, never()).addProduct(any());
    }

    @Test
    @DisplayName("Добавление существующего товара вызывает метод addProduct у ProductBasket")
    void testAddExistentProductCallsAddProduct() {
        // Arrange
        when(storageService.getProductById(productId))
                .thenReturn(Optional.of(testProduct));

        // Act
        basketService.addProductToBasket(productId);

        // Assert
        verify(productBasket, times(1)).addProduct(productId);
    }

    @Test
    @DisplayName("Возвращает пустую корзину если ProductBasket пуст")
    void testGetUserBasketReturnsEmptyBasket() {
        // Arrange
        when(productBasket.getProducts())
                .thenReturn(new HashMap<>());

        // Act
        UserBasket basket = basketService.getUserBasket();

        // Assert
        assertNotNull(basket);
        assertTrue(basket.getItems().isEmpty());
        assertEquals(0, basket.getTotal());
    }

    @Test
    @DisplayName("Возвращает подходящую корзину если в ProductBasket есть товары")
    void testGetUserBasketReturnsPopulatedBasket() {
        // Arrange
        Map<UUID, Integer> products = new HashMap<>();
        products.put(productId, 2);
        when(productBasket.getProducts()).thenReturn(products);
        when(storageService.getProduct(productId)).thenReturn(testProduct);

        // Act
        UserBasket basket = basketService.getUserBasket();

        // Assert
        assertNotNull(basket);
        assertEquals(1, basket.getItems().size());
        BasketItem item = basket.getItems().get(0);
        assertEquals(testProduct, item.getProduct());
        assertEquals(2, item.getQuantity());
    }

    @Test
    @DisplayName("Корзина содержит несколько товаров")
    void testGetUserBasketWithMultipleProducts() {
        // Arrange
        UUID productId2 = UUID.randomUUID();
        Product product2 = new SimpleProduct("Второй товар", 1000);
        Map<UUID, Integer> products = new HashMap<>();
        products.put(productId, 1);
        products.put(productId2, 3);
        when(productBasket.getProducts()).thenReturn(products);
        when(storageService.getProduct(productId)).thenReturn(testProduct);
        when(storageService.getProduct(productId2)).thenReturn(product2);

        // Act
        UserBasket basket = basketService.getUserBasket();

        // Assert
        assertEquals(2, basket.getItems().size());
        assertEquals(500 * 1 + 1000 * 3, basket.getTotal());
    }
}
