package nl.novi.eindopdracht.services;

import nl.novi.eindopdracht.dtos.CategoryDto;
import nl.novi.eindopdracht.exceptions.ResourceNotFoundException;
import nl.novi.eindopdracht.models.Category;
import nl.novi.eindopdracht.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public List<CategoryDto> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(category -> new CategoryDto(category.getId(), category.getName()))
                .collect(Collectors.toList());
    }

    public CategoryDto getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categorie niet gevonden"));
        return new CategoryDto(category.getId(), category.getName());
    }

    public CategoryDto createCategory(CategoryDto categoryDto) {
        Category category = new Category(categoryDto.getName());
        Category savedCategory = categoryRepository.save(category);
        return new CategoryDto(savedCategory.getId(), savedCategory.getName());
    }

    public CategoryDto updateCategory(Long id, CategoryDto categoryDto) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categorie niet gevonden"));
        category.setName(categoryDto.getName());
        Category updatedCategory = categoryRepository.save(category);
        return new CategoryDto(updatedCategory.getId(), updatedCategory.getName());
    }
}