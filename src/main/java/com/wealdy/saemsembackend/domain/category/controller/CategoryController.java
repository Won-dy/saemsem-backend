package com.wealdy.saemsembackend.domain.category.controller;

import com.wealdy.saemsembackend.common.response.ListResponseDto;
import com.wealdy.saemsembackend.common.response.Response;
import com.wealdy.saemsembackend.domain.category.dto.CategoryDto;
import com.wealdy.saemsembackend.domain.category.service.CategoryService;
import java.util.concurrent.Callable;
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
    public Callable<Response<ListResponseDto<CategoryDto.GetList>>> getCategories() {
        return () -> Response.of(categoryService.getCategories());
    }
}
