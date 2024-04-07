package com.wealdy.saemsembackend.domain.category.controller.response;

import com.wealdy.saemsembackend.domain.category.service.dto.GetCategoryDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CategoryListResponse {

    private Long id;
    private String name;

    public static CategoryListResponse from(GetCategoryDto category) {
        return new CategoryListResponse(category.getId(), category.getName());
    }
}
