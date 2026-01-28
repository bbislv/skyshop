package org.skypro.skyshop.service;

import org.springframework.stereotype.Service;
import org.skypro.skyshop.dto.SearchResult;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SearchService {
    private final StorageService storageService;

    public SearchService(StorageService storageService) {
        this.storageService = storageService;
    }

    public Set<SearchResult> search(String pattern) {
        String lowerPattern = pattern.toLowerCase();

        return storageService.getAllSearchable().stream()
                .filter(item -> item.getSearchTerm().toLowerCase().contains(lowerPattern))
                .map(SearchResult::fromSearchable)
                .collect(Collectors.toCollection(() -> new TreeSet<>(
                        Comparator.comparingInt((SearchResult s) -> s.getName().length())
                                .reversed()
                                .thenComparing(SearchResult::getName)
                )));
    }
}
