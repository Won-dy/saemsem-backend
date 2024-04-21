package com.wealdy.saemsembackend.domain.category.service;

import static com.wealdy.saemsembackend.domain.core.response.ResponseCode.NOT_FOUND_CATEGORY;

import com.wealdy.saemsembackend.domain.category.entity.Category;
import com.wealdy.saemsembackend.domain.category.repository.CategoryRepository;
import com.wealdy.saemsembackend.domain.category.service.dto.GetCategoryDto;
import com.wealdy.saemsembackend.domain.core.exception.NotFoundException;
import java.util.List;
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
            .toList();
    }

    /**
     * 카테고리 단일 조회 with name
     */
    public Category getCategoryByName(String name) {
        return categoryRepository.findByName(name)
            .orElseThrow(() -> new NotFoundException(NOT_FOUND_CATEGORY));
    }
}
