package com.wealdy.saemsembackend.domain.category.dto;

import com.wealdy.saemsembackend.domain.category.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;


public class CategoryDto {

    @Getter
    @AllArgsConstructor
    public static class GetList {

        private Long id;
        private String name;

        public static GetList from(Category category) {
            return new GetList(category.getId(), category.getName());
        }
    }
}
