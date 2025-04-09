package duan.demo.ServiceLayer;

import duan.demo.RepositoryLayer.CategoryRepository;
import duan.demo.entity.Category;
import duan.demo.exception.CategoryNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Transactional(readOnly = true)
    public Page<Category> getAllCategories(Pageable pageable) {
        return categoryRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Category getCategoryById(Integer id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Không tìm thấy danh mục với ID: " + id));
    }

    @Transactional
    public Category createCategory(Category category) {
        return categoryRepository.save(category);
    }

    @Transactional
    public Category updateCategory(Integer id, Category categoryDetails) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Không tìm thấy danh mục với ID: " + id));

        if (categoryDetails.getName() != null) category.setName(categoryDetails.getName());
        if (categoryDetails.getDescription() != null) category.setDescription(categoryDetails.getDescription());
        if (categoryDetails.getCategoryCode() != null) category.setCategoryCode(categoryDetails.getCategoryCode());
        if (categoryDetails.getStatus() != null) category.setStatus(categoryDetails.getStatus());
        if (categoryDetails.getModifiedBy() != null) category.setModifiedBy(categoryDetails.getModifiedBy());

        return categoryRepository.save(category);
    }

    @Transactional
    public void deleteCategory(Integer id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Không thể xóa danh mục. ID không tồn tại: " + id));

        if (!category.getProducts().isEmpty()) {
            throw new IllegalStateException("Không thể xóa danh mục vì có sản phẩm liên quan.");
        }

        categoryRepository.delete(category);
    }
}
