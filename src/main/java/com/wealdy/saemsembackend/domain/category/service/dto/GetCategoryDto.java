package com.wealdy.saemsembackend.domain.category.service.dto;

import com.wealdy.saemsembackend.domain.category.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GetCategoryDto {

    private Long id;
    private String name;

    public static GetCategoryDto from(Category category) {
        return new GetCategoryDto(category.getId(), category.getName());
    }
}
