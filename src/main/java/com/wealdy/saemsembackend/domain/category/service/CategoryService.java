package com.wealdy.saemsembackend.domain.category.service;

import com.wealdy.saemsembackend.domain.category.entity.Category;
import com.wealdy.saemsembackend.domain.category.repository.CategoryRepository;
import com.wealdy.saemsembackend.domain.category.service.dto.GetCategoryDto;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    /**
     * 카테고리 전체 목록 조회
     */
    public List<GetCategoryDto> getCategories() {
        return categoryRepository.findAll().stream()
            .map(GetCategoryDto::from)
            .collect(Collectors.toList());
    }

    /**
     * 카테고리 단일 조회 with name
     */
    public Category findByName(String name) {
        return categoryRepository.findByName(name);
    }
}
