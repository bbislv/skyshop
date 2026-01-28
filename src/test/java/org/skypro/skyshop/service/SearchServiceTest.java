package org.skypro.skyshop.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.skypro.skyshop.dto.SearchResult;
import org.skypro.skyshop.model.article.Article;
import org.skypro.skyshop.model.search.Searchable;
import org.skypro.skyshop.model.product.SimpleProduct;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Тесты для SearchService")
class SearchServiceTest {

    @Mock
    private StorageService storageService;

    @InjectMocks
    private SearchService searchService;

    @BeforeEach
    void setUp() {}

    @Test
    @DisplayName("Поиск в случае отсутствия объектов в StorageService")
    void testSearchWhenStorageIsEmpty() {
        // Arrange
        when(storageService.getAllSearchable())
                .thenReturn(new ArrayList<>());

        // Act
        Set<SearchResult> result = searchService.search("test");

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Поиск в случае если объект в StorageService есть но нет подходящего")
    void testSearchWhenNoMatchingObjects() {
        // Arrange
        Searchable product = new SimpleProduct("Книга", 500);
        List<Searchable> allItems = Collections.singletonList(product);
        when(storageService.getAllSearchable()).thenReturn(allItems);

        // Act
        Set<SearchResult> result = searchService.search("несуществующий");

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Поиск когда есть подходящий объект в StorageService")
    void testSearchWhenMatchingObjectExists() {
        // Arrange
        SimpleProduct product = new SimpleProduct("Книга", 500);
        List<Searchable> allItems = Collections.singletonList(product);
        when(storageService.getAllSearchable()).thenReturn(allItems);

        // Act
        Set<SearchResult> result = searchService.search("книга");

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        SearchResult firstItem = result.iterator().next();
        assertEquals("Книга", firstItem.getName());
    }

    @Test
    @DisplayName("Поиск не учитывает регистр букв")
    void testSearchIsCaseInsensitive() {
        // Arrange
        SimpleProduct product = new SimpleProduct("КНИГА", 500);
        List<Searchable> allItems = Collections.singletonList(product);
        when(storageService.getAllSearchable()).thenReturn(allItems);

        // Act
        Set<SearchResult> resultLower = searchService.search("книга");
        Set<SearchResult> resultUpper = searchService.search("КНИГА");
        Set<SearchResult> resultMixed = searchService.search("КнИгА");

        // Assert
        assertEquals(1, resultLower.size());
        assertEquals(1, resultUpper.size());
        assertEquals(1, resultMixed.size());
    }

    @Test
    @DisplayName("Поиск с несколькими подходящими объектами")
    void testSearchWithMultipleMatches() {
        // Arrange
        SimpleProduct product1 = new SimpleProduct("Книга 1", 500);
        SimpleProduct product2 = new SimpleProduct("Книга 2", 1000);
        Article article = new Article("Рецензия на книгу", "Хорошая книга...");
        List<Searchable> allItems = Arrays.asList(product1, product2, article);
        when(storageService.getAllSearchable()).thenReturn(allItems);

        // Act
        Set<SearchResult> result = searchService.search("книга");

        // Assert
        assertNotNull(result);
        assertEquals(3, result.size());
    }

    @Test
    @DisplayName("Результаты поиска сортируются по длине названия")
    void testSearchResultsSortedByNameLengthDescending() {
        // Arrange
        SimpleProduct shortName = new SimpleProduct("Кн", 500);
        SimpleProduct mediumName = new SimpleProduct("Книга", 1000);
        SimpleProduct longName = new SimpleProduct("Книга с длинным названием", 2000);
        List<Searchable> allItems = Arrays.asList(shortName, mediumName, longName);
        when(storageService.getAllSearchable()).thenReturn(allItems);

        // Act
        Set<SearchResult> result = searchService.search("книга");

        // Assert
        List<SearchResult> sortedList = new ArrayList<>(result);
        assertEquals(2, sortedList.size());
        assertEquals("Книга с длинным названием", sortedList.get(0).getName());
        assertEquals("Книга", sortedList.get(1).getName());
    }

    @Test
    @DisplayName("Результаты с одинаковой длиной названия сортируются алфавитно")
    void testSearchResultsSortedAlphabeticallyWhenSameLength() {
        // Arrange
        SimpleProduct bookA = new SimpleProduct("Астрология", 500);
        SimpleProduct bookB = new SimpleProduct("Биография", 1000);
        SimpleProduct bookC = new SimpleProduct("Химия", 1500);
        List<Searchable> allItems = Arrays.asList(bookA, bookB, bookC);
        when(storageService.getAllSearchable()).thenReturn(allItems);

        // Act
        Set<SearchResult> result = searchService.search("и");

        // Assert
        List<SearchResult> sortedList = new ArrayList<>(result);
        assertEquals(3, sortedList.size());
        assertEquals("Астрология", sortedList.get(0).getName());
        assertEquals("Биография", sortedList.get(1).getName());
        assertEquals("Химия", sortedList.get(2).getName());
    }

    @Test
    @DisplayName("Поиск с пустой строкой возвращает все объекты")
    void testSearchWithEmptyPatternReturnsAll() {
        // Arrange
        SimpleProduct product1 = new SimpleProduct("Книга", 500);
        SimpleProduct product2 = new SimpleProduct("Ручка", 100);
        Article article = new Article("Статья", "Описание");
        List<Searchable> allItems = Arrays.asList(product1, product2, article);
        when(storageService.getAllSearchable()).thenReturn(allItems);

        // Act
        Set<SearchResult> result = searchService.search("");

        // Assert
        assertEquals(3, result.size());
    }

    @Test
    @DisplayName("fromSearchable работает правильно")
    void testSearchResultFromSearchable() {
        // Arrange
        SimpleProduct product = new SimpleProduct("Тестовая книга", 500);

        // Act
        SearchResult result = SearchResult.fromSearchable(product);

        // Assert
        assertNotNull(result);
        assertEquals("Тестовая книга", result.getName());
    }
}
