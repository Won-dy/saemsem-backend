package com.wealdy.saemsembackend.domain.category.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowableOfType;

import com.wealdy.saemsembackend.domain.category.entity.Category;
import com.wealdy.saemsembackend.domain.category.mock.MockCategoryRepository;
import com.wealdy.saemsembackend.domain.category.repository.CategoryRepository;
import com.wealdy.saemsembackend.domain.category.service.dto.GetCategoryDto;
import com.wealdy.saemsembackend.domain.core.exception.NotFoundException;
import com.wealdy.saemsembackend.domain.core.response.ResponseCode;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CategoryServiceTest {

    CategoryRepository categoryRepository;
    CategoryService categoryService;


    @BeforeEach
    void init() {
        categoryRepository = new MockCategoryRepository();
        categoryService = new CategoryService(categoryRepository);
    }


    @DisplayName("[getCategoryList] 카테고리 목록 조회를 성공한다.")
    @Test
    void getCategoryList() {
        // given
        categoryRepository.save(Category.of(1L, "기타"));
        categoryRepository.save(Category.of(2L, "식비"));
        categoryRepository.save(Category.of(3L, "교통비"));

        // when
        List<GetCategoryDto> categoryList = categoryService.getCategoryList();

        // then
        assertThat(categoryList.size()).isEqualTo(3);
        assertThat(categoryList.get(0).getName()).isEqualTo("기타");
        assertThat(categoryList.get(1).getName()).isEqualTo("식비");
        assertThat(categoryList.get(2).getName()).isEqualTo("교통비");
    }

    @DisplayName("[getCategory] 카테고리 name 으로 조회를 성공한다.")
    @Test
    void getCategory() {
        // given
        categoryRepository.save(Category.of(1L, "기타"));
        categoryRepository.save(Category.of(2L, "식비"));
        categoryRepository.save(Category.of(3L, "교통비"));

        // when
        Category category1 = categoryService.getCategory("기타");
        Category category2 = categoryService.getCategory("식비");
        Category category3 = categoryService.getCategory("교통비");

        // then
        assertThat(category1.getName()).isEqualTo("기타");
        assertThat(category2.getName()).isEqualTo("식비");
        assertThat(category3.getName()).isEqualTo("교통비");
    }

    @DisplayName("[getCategoryFailedWithNoName] 없는 name 으로 카테고리 조회시 (NotFoundException) 발생한다.")
    @Test
    void getCategoryFailedWithNoName() {
        // given
        // when
        NotFoundException notFoundException = catchThrowableOfType(
            () -> categoryService.getCategory("기타"),
            NotFoundException.class
        );

        // then
        assertThat(notFoundException.getMessage()).isEqualTo(ResponseCode.NOT_FOUND_CATEGORY.getMessage());
    }
}