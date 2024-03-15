package com.wealdy.saemsembackend.domain.category.service;

import com.wealdy.saemsembackend.common.response.ListResponseDto;
import com.wealdy.saemsembackend.domain.category.dto.CategoryDto.GetList;
import com.wealdy.saemsembackend.domain.category.repository.CategoryRepository;
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
     * 카테고리 전체 개수 조회
     */
    public Long getCategoryCount() {
        return categoryRepository.countAll();
    }

    /**
     * 카테고리 전체 목록 조회
     */
    public ListResponseDto<GetList> getCategories() {
        List<GetList> categories = categoryRepository.findAll().stream()
            .map(GetList::from).collect(Collectors.toList());
        return ListResponseDto.of(getCategoryCount(), categories);
    }
}
