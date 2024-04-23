package com.wealdy.saemsembackend.domain.category.controller;

import com.wealdy.saemsembackend.domain.category.controller.response.CategoryListResponse;
import com.wealdy.saemsembackend.domain.category.service.CategoryService;
import com.wealdy.saemsembackend.domain.core.response.ListResponseDto;
import com.wealdy.saemsembackend.domain.core.response.Response;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;

    /**
     * 카테고리 목록 조회 API
     */
    @GetMapping
    public Response<ListResponseDto<CategoryListResponse>> getCategoryList() {
        List<CategoryListResponse> categories = categoryService.getCategoryList().stream()
            .map(CategoryListResponse::from)
            .toList();

        return Response.of(ListResponseDto.from(categories));
    }
}
