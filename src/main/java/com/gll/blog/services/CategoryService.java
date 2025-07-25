package com.gll.blog.services;

import com.gll.blog.entities.CategoryEntity;
import com.gll.blog.exceptions.DataValidityException;
import com.gll.blog.exceptions.NotFoundException;
import com.gll.blog.mappers.CategoryMapper;
import com.gll.blog.repositories.CategoryRepository;
import com.gll.blog.requests.CategoryRequest;
import com.gll.blog.responses.CategoryResponse;
import com.gll.blog.utils.AppUtils;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    private final CategoryMapper categoryMapper;

    private static final Logger log = LoggerFactory.getLogger(CategoryService.class);

    public CategoryResponse createCategory(CategoryRequest request) {
        checkDataValidity(request);

        CategoryEntity category = CategoryEntity.builder()
                .name(request.name().toUpperCase())
                .description(request.description())
                .build();

        CategoryEntity savedCategory = categoryRepository.save(category);
        log.info("created a new category with name: {} and ID: {}",
                savedCategory.getName(), savedCategory.getId()
        );

        return categoryMapper.toResponse(savedCategory);
    }

    public List<CategoryResponse> readAll() {
        return categoryRepository.findAll().stream()
                .map(categoryMapper::toResponse)
                .toList();
    }

    public CategoryResponse update(UUID id, CategoryRequest request) {
        CategoryEntity category = categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("not found category with ID: " + id));

        if (AppUtils.isNotBlank(request.description())) {
            category.setDescription(request.description());
        }

        CategoryEntity updatedCategory = categoryRepository.save(category);
        log.info("updated a category with name: {} and ID: {}",
                updatedCategory.getName(), updatedCategory.getId()
        );

        return categoryMapper.toResponse(updatedCategory);
    }

    public void deleteById(UUID id) {
        CategoryEntity category = categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("not found category with ID: " + id));

        log.info("deleted category with name {} and ID {}",
                category.getName(), category.getId()
        );

        categoryRepository.delete(category);
    }

    private void checkDataValidity(CategoryRequest request) {
        if (AppUtils.isBlank(request.name()) || categoryRepository.existsByName(request.name().toUpperCase())) {
            throw new DataValidityException("The category name is invalid or already used");
        }

        if (AppUtils.isBlank(request.description())) {
            throw new DataValidityException("The description is blank.");
        }
    }
}
