package com.wealdy.saemsembackend.domain.category.controller;

import com.wealdy.saemsembackend.domain.category.controller.response.CategoryListResponse;
import com.wealdy.saemsembackend.domain.category.service.CategoryService;
import com.wealdy.saemsembackend.domain.core.response.ListResponseDto;
import com.wealdy.saemsembackend.domain.core.response.Response;
import java.util.List;
import java.util.stream.Collectors;
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
    public Response<ListResponseDto<CategoryListResponse>> getCategories() {
        List<CategoryListResponse> categories = categoryService.getCategories().stream()
            .map(CategoryListResponse::from)
            .collect(Collectors.toList());

        return Response.of(ListResponseDto.from(categories));
    }
}
