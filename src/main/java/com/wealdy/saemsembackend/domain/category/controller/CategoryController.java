package com.wealdy.saemsembackend.domain.category.controller;

import com.wealdy.saemsembackend.domain.core.response.ListResponseDto;
import com.wealdy.saemsembackend.domain.core.response.Response;
import com.wealdy.saemsembackend.domain.category.dto.CategoryDto;
import com.wealdy.saemsembackend.domain.category.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    /**
     * 카테고리 목록 조회 API
     */
    @GetMapping("/category")
    public Response<ListResponseDto<CategoryDto.GetList>> getCategories() {
        return Response.of(categoryService.getCategories());
    }
}
