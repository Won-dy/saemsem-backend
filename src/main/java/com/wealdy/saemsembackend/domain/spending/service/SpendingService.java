package com.wealdy.saemsembackend.domain.spending.service;

import com.wealdy.saemsembackend.domain.category.entity.Category;
import com.wealdy.saemsembackend.domain.category.service.CategoryService;
import com.wealdy.saemsembackend.domain.spending.entity.Spending;
import com.wealdy.saemsembackend.domain.spending.repository.SpendingRepository;
import com.wealdy.saemsembackend.domain.user.entity.User;
import com.wealdy.saemsembackend.domain.user.repository.UserRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SpendingService {

    private final SpendingRepository spendingRepository;
    private final CategoryService categoryService;
    private final UserRepository userRepository;

    @Transactional
    public Long createSpending(LocalDateTime date, int amount, String memo, boolean excludeTotal, String categoryName, String userId) {
        User user = userRepository.findById(userId).get(0);
        Category category = categoryService.findByName(categoryName);

        Spending spending = spendingRepository.save(Spending.createSpending(date, amount, memo, excludeTotal, user, category));
        return spending.getId();
    }
}
