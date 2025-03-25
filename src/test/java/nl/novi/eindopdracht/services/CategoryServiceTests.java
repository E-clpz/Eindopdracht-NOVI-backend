package nl.novi.eindopdracht.services;

import nl.novi.eindopdracht.dtos.CategoryDto;
import nl.novi.eindopdracht.exceptions.ResourceNotFoundException;
import nl.novi.eindopdracht.models.Category;
import nl.novi.eindopdracht.repositories.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTests {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    private Category category1;
    private CategoryDto categoryDto1;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        category1 = new Category("Test Category");
        category1.setId(1L);
        categoryDto1 = new CategoryDto(category1.getId(), category1.getName());
    }

    @Test
    void testGetAllCategories() {
        when(categoryRepository.findAll()).thenReturn(Collections.singletonList(category1));

        List<CategoryDto> result = categoryService.getAllCategories();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(categoryDto1.getName(), result.get(0).getName());

        verify(categoryRepository).findAll();
    }

    @Test
    void testGetCategoryById_Success() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category1));

        CategoryDto result = categoryService.getCategoryById(1L);

        assertNotNull(result);
        assertEquals(categoryDto1.getName(), result.getName());

        verify(categoryRepository).findById(1L);
    }

    @Test
    void testGetCategoryById_NotFound() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> categoryService.getCategoryById(1L));

        verify(categoryRepository).findById(1L);
    }

    @Test
    void testCreateCategory() {
        when(categoryRepository.save(any(Category.class))).thenReturn(category1);

        CategoryDto result = categoryService.createCategory(categoryDto1);

        assertNotNull(result);
        assertEquals(categoryDto1.getName(), result.getName());

        verify(categoryRepository).save(any(Category.class));
    }

    @Test
    void testUpdateCategory() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category1));
        when(categoryRepository.save(any(Category.class))).thenReturn(category1);

        CategoryDto result = categoryService.updateCategory(1L, categoryDto1);

        assertNotNull(result);
        assertEquals(categoryDto1.getName(), result.getName());

        verify(categoryRepository).findById(1L);
        verify(categoryRepository).save(any(Category.class));
    }

    @Test
    void testUpdateCategory_NotFound() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> categoryService.updateCategory(1L, categoryDto1));

        verify(categoryRepository).findById(1L);
    }
}
